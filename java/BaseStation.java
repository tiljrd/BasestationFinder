package com.baselabs.tiljo.basestationfinder;

public class BaseStation {

    String radio;
    Long locationAreaCode;
    Long networkCode;
    Long cellId;
    Double longitude;
    Double latitude;

    public String getRadio() {
        return radio;
    }

    public void setRadio(String radio) {
        this.radio = radio;
    }

    public Long getLocationAreaCode() {
        return locationAreaCode;
    }

    public void setLocationAreaCode(Long locationAreaCode) {
        this.locationAreaCode = locationAreaCode;
    }

    public Long getNetworkCode() {
        return networkCode;
    }

    public void setNetworkCode(Long networkCode) {
        this.networkCode = networkCode;
    }

    public Long getCellId() {
        return cellId;
    }

    public void setCellId(Long cellId) {
        this.cellId = cellId;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
