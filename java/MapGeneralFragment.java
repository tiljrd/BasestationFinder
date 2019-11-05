package com.baselabs.tiljo.basestationfinder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MapGeneralFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    Double longitude, latitude;
    private List<Double> longitudeList = new ArrayList<>();
    private List<Double> latitudeList = new ArrayList<>();

    private ClusterManager<MyItem> myItemClusterManager;

    private MarkerDrawer markerDrawer;
    private Marker marker;

    @SuppressLint("ValidFragment")
    public MapGeneralFragment(Double longitude, Double latitude,
                              List<Double> longitudeList, List<Double> latitudeList) {
        if(longitude != null && latitude != null && longitudeList.size() != 0 &&
                latitudeList.size() != 0){

            this.longitude = longitude;
            this.latitude = latitude;
            this.longitudeList = longitudeList;
            this.latitudeList = latitudeList;
        }
    }
    public MapGeneralFragment() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (checkHasPermission()) {
            SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().
                    findFragmentById(R.id.map2);
            mapFragment.getMapAsync(this);

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // inflate search_layout
        return inflater.inflate(R.layout.maps_general_layout, container, false);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if(longitude != null && latitude != null) {
            LatLng selectedPlace = new LatLng(latitude, longitude);
            marker = mMap.addMarker(new MarkerOptions().position(selectedPlace)
                    .title("gewählter Ort"));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedPlace, 5));

            myItemClusterManager = new ClusterManager<>(getActivity(), mMap);
            markerDrawer = new MarkerDrawer(latitudeList, longitudeList);
            try {
                myItemClusterManager = markerDrawer.execute(myItemClusterManager).get();
                setUpClusterer();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void setUpClusterer() {

        final CustomClusterRenderer renderer = new CustomClusterRenderer(getActivity(), mMap,
                myItemClusterManager, BitmapDescriptorFactory.defaultMarker
                (BitmapDescriptorFactory.HUE_AZURE));

        myItemClusterManager.setRenderer(renderer);
        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(myItemClusterManager);
        mMap.setOnMarkerClickListener(myItemClusterManager);

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
                    if (checkHasPermission()) {
                        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().
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
