package com.baselabs.tiljo.basestationfinder;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MyItem implements ClusterItem {
    private final LatLng mPosition;
    public final String mTitle;
    private final String mSnippet;

    public MyItem(double lat, double lng, String mTitle, String mSnippet) {
        this.mTitle = mTitle;
        this.mSnippet = mSnippet;
        mPosition = new LatLng(lat, lng);
    }
    
    public LatLng getPosition() {
        return mPosition;
    }
    
    public String getTitle() {
        return mTitle;
    }

    public String getSnippet() {
        return mSnippet;
    }
}

