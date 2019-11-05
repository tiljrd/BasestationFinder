package com.baselabs.tiljo.basestationfinder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.ExecutionException;

@SuppressLint("ValidFragment")
public class MapPersonalFragment extends Fragment implements OnMapReadyCallback{

    private GoogleMap mMap;
    View view;
    Marker marker;
    String longitude, latitude;
    public static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    private FusedLocationProviderClient mFusedLocationClient;
    private int cellId;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(checkHasPermission()) {
            SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().
                    findFragmentById(R.id.map1);
            mapFragment.getMapAsync(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.maps_personal_layout, container, false);
    }

    @SuppressLint("ValidFragment")
    public MapPersonalFragment(int cellId) {
        this.cellId = cellId;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        TelephonyManager telephonyManager = (TelephonyManager)
                getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        TelephonyInformation telephonyInformation = new TelephonyInformation();
        telephonyInformation.updateAll(telephonyManager);

        SendRequest sendRequest = new SendRequest(telephonyInformation.getCellId(),
                telephonyInformation.getLocalAreaCode(),
                telephonyInformation.getMobileCountryCode(),
                telephonyInformation.getMobileNetworkCode(), telephonyInformation.getRadio());
        try {
            String result = sendRequest.execute
                    ("https://us1.unwiredlabs.com/v2/process.php").get(); // get data
            Log.i("Result", result);
            try {
                JSONObject jsonObjectResponse = new JSONObject(result); // get lat and lon
                latitude = jsonObjectResponse.getString("lat"); // from the response
                longitude = jsonObjectResponse.getString("lon");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(marker != null){
            marker.remove();
        }
        if(longitude != null && latitude != null) {
            LatLng latLngCellTower = new LatLng(
                    Double.parseDouble(latitude), Double.parseDouble(longitude));
            marker = mMap.addMarker(new MarkerOptions().position(latLngCellTower).title
                    ("Cell Tower").snippet(String.valueOf(cellId)));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngCellTower, 16));
        }else {
            Toast.makeText(getActivity(), "Kein Sendemast gefunden!", Toast.LENGTH_SHORT).show();
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            LatLng currentPosition = new LatLng(location.getLatitude(),
                                    location.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(currentPosition).
                                    title("Deine Position").icon(BitmapDescriptorFactory.defaultMarker
                                    (BitmapDescriptorFactory.HUE_AZURE)));
                        }
                    }
                });

        }

    // permission check
    public boolean checkHasPermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission
                (getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission
                (getActivity(), Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.READ_PHONE_STATE}, PERMISSIONS_MULTIPLE_REQUEST);
            return false;

        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 123: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(checkHasPermission()) {
                        SupportMapFragment mapFragment = (SupportMapFragment)this.getChildFragmentManager().
                                findFragmentById(R.id.map1);
                        mapFragment.getMapAsync(this);
                    }
                } else {
                    Toast.makeText(getActivity(), "Please grant permissions",
                            Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }
}
