package com.baselabs.tiljo.basestationfinder;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class InformationFragment extends Fragment {




   TelephonyManager telephonyManager;
    public static final int PERMISSIONS_MULTIPLE_REQUEST = 123;

    TextView cellIdTextView;
    TextView locationAreaCodeTextView;
    TextView typeTextView;
    TextView mobileCountryCodeTextView;
    TextView operatorTextView;
    TextView mobileNetworkCodeTextView;
    TextView subscriberIdTextView;

    TelephonyInformation telephonyInformation;

    private int cellId;

    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.information_layout, container, false);

        telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);

        cellIdTextView = view.findViewById(R.id.cellIdTextView);
        locationAreaCodeTextView = view.findViewById(R.id.locationAreaCodeTextView);
        typeTextView = view.findViewById(R.id.typeTextView);
        mobileCountryCodeTextView = view.findViewById(R.id.mobileCountryCodeTextView);
        operatorTextView = view.findViewById(R.id.operatorTextView);
        mobileNetworkCodeTextView = view.findViewById(R.id.mobileNetworkCodeTextView);
        subscriberIdTextView = view.findViewById(R.id.subscriberIdTextView);

        if(checkHasPermission()){
            setUp();
        }

        return view;
    }

    private void setUp() {
        if(telephonyManager != null) {
            telephonyInformation = new TelephonyInformation();
            telephonyInformation.updateAll(telephonyManager);
            updateTextViews();
            cellId = telephonyInformation.getCellId();
            ((MainActivity)getActivity()).setCellId(cellId);
        }
    }



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
                        if(telephonyManager != null) {
                            setUp();
                        }
                    }

                } else {
                    Toast.makeText(getActivity(), "Please grant permissions",
                            Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

    private void updateTextViews(){

        if(telephonyInformation.getCellId() != 0 &&
                telephonyInformation.getLocalAreaCode() != 0 &&
                telephonyInformation.getMobileCountryCode() != 0
                && telephonyInformation.getMobileNetworkCode() != 0){
            cellIdTextView.setText(String.valueOf(telephonyInformation.getCellId()));
            locationAreaCodeTextView.setText(
                    String.valueOf(telephonyInformation.getLocalAreaCode()));
            typeTextView.setText(telephonyInformation.getNetworkType());
            mobileCountryCodeTextView.setText(
                    String.valueOf(telephonyInformation.getMobileCountryCode()));
            operatorTextView.setText(telephonyInformation.getNetworkOperatorName());
            mobileNetworkCodeTextView.setText(
                    String.valueOf(telephonyInformation.getMobileNetworkCode()));
            subscriberIdTextView.setText(telephonyInformation.getSubscriberId());
        }

    }
}
