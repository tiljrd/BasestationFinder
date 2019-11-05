package com.baselabs.tiljo.basestationfinder;


import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class CustomClusterRenderer extends DefaultClusterRenderer<MyItem> {

    private final Context mContext;
    private BitmapDescriptor bitmapDescriptor;

    public CustomClusterRenderer(Context context, GoogleMap map,
                                 ClusterManager<MyItem> clusterManager, BitmapDescriptor bitmapDescriptor) {
        super(context, map, clusterManager);
        this.bitmapDescriptor = bitmapDescriptor;

        mContext = context;
    }

    @Override
    protected void onBeforeClusterItemRendered(MyItem item,
                                                         MarkerOptions markerOptions) {
        final BitmapDescriptor markerDescriptor = bitmapDescriptor;

        markerOptions.icon(markerDescriptor).snippet(item.mTitle);

    }
}
