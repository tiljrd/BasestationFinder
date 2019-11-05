package com.baselabs.tiljo.basestationfinder;

import android.annotation.SuppressLint;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class TelephonyInformation {

    private int cellId = 0;
    private int localAreaCode = 0;
    private int mobileCountryCode = 0;
    private int mobileNetworkCode = 0;
    private GsmCellLocation cellLocation;
    private TelephonyManager telephonyManager;
    private String networkOperatorName = "Unknown";
    private String networkOperator = "Unknown";
    private String networkType = "Unknown";
    private String radio= "Unknown";
    private String subscriberId = "Unknown";

   // update methods


    @SuppressLint("MissingPermission")
    public void updateAll(TelephonyManager telephonyManager){
        this.telephonyManager = telephonyManager;
        networkOperatorName  = telephonyManager.getNetworkOperatorName();
        networkOperator = telephonyManager.getNetworkOperator();
        cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();

        updateCellId();
        updateLocalAreaCode();
        updateMobileCountryCode();
        updateMobileNetworkCode();
        updateNetworkType();
        updateSubscriberId();
    }

    private void updateCellId() {
        try {
            cellId = cellLocation.getCid();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void updateLocalAreaCode() {
        try {
            localAreaCode = cellLocation.getLac();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void updateMobileCountryCode() {
        if (!TextUtils.isEmpty(networkOperator)) {
            mobileCountryCode = Integer.parseInt(networkOperator.substring(0, 3));
        }
    }

    private void updateMobileNetworkCode() {
        if (!TextUtils.isEmpty(networkOperator)) {
            mobileNetworkCode = Integer.parseInt(networkOperator.substring(3));
        }
    }

    private void updateNetworkType() {
try {
    int i = telephonyManager.getNetworkType();
    switch (i) {
        case TelephonyManager.NETWORK_TYPE_1xRTT:
            networkType = "1xRTT";
            radio = "cdma";
            break;
        case TelephonyManager.NETWORK_TYPE_CDMA:
            networkType = "CDMA";
            radio = "cdma";
            break;
        case TelephonyManager.NETWORK_TYPE_EDGE:
            networkType = "EDGE";
            radio = "gsm";
            break;
        case TelephonyManager.NETWORK_TYPE_EHRPD:
            networkType = "eHRPD";
            radio = "cdma";
            break;
        case TelephonyManager.NETWORK_TYPE_EVDO_0:
            networkType = "EVDO rev. 0";
            radio = "cdma";
            break;
        case TelephonyManager.NETWORK_TYPE_EVDO_A:
            networkType = "EVDO rev. A";
            radio = "cdma";
            break;
        case TelephonyManager.NETWORK_TYPE_EVDO_B:
            networkType = "EVDO rev. B";
            radio = "cdma";
            break;
        case TelephonyManager.NETWORK_TYPE_GPRS:
            networkType = "GPRS";
            radio = "gsm";
            break;
        case TelephonyManager.NETWORK_TYPE_HSDPA:
            networkType = "HSDPA";
            radio = "umts";
            break;
        case TelephonyManager.NETWORK_TYPE_HSPA:
            networkType = "HSPA";
            radio = "umts";
            break;
        case TelephonyManager.NETWORK_TYPE_HSPAP:
            networkType = "HSPA+";
            radio = "umts";
            break;
        case TelephonyManager.NETWORK_TYPE_HSUPA:
            networkType = "HSUPA";
            radio = "umts";
            break;
        case TelephonyManager.NETWORK_TYPE_IDEN:
            networkType = "iDen";
            radio = "umts";
            break;
        case TelephonyManager.NETWORK_TYPE_LTE:
            networkType = "LTE";
            radio = "lte";
            break;
        case TelephonyManager.NETWORK_TYPE_UMTS:
            networkType = "UMTS";
            break;
        case TelephonyManager.NETWORK_TYPE_UNKNOWN:
            networkType = "Unknown";
            break;
    }
}catch (Exception e){
    e.printStackTrace();
}
    }
    @SuppressLint("MissingPermission")
    private void updateSubscriberId() {
        try {
            subscriberId = telephonyManager.getSubscriberId();
        }catch(Exception e){
            e.printStackTrace();
        }

    }


// Getter methods

    public int getCellId() {
        return cellId;
    }

    public int getLocalAreaCode() {
        return localAreaCode;
    }

    public int getMobileCountryCode() {
        return mobileCountryCode;
    }

    public int getMobileNetworkCode() {
        return mobileNetworkCode;
    }

    public String getNetworkOperatorName() {
        return networkOperatorName;
    }

    public String getNetworkType() {
        return networkType;
    }

    public String getRadio() {
        return radio;
    }

    public String getSubscriberId() {
        return subscriberId;
    }
}
