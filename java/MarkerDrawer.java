package com.baselabs.tiljo.basestationfinder;

import android.os.AsyncTask;
import com.google.maps.android.clustering.ClusterManager;
import java.util.List;

public class MarkerDrawer extends AsyncTask<ClusterManager<MyItem>,Void,ClusterManager<MyItem>> {

    List<Double> latitudeList;
    List<Double> longitudeList;
    ClusterManager<MyItem> myItemClusterManager;


    public MarkerDrawer(List<Double> latitudeList, List<Double> longitudeList) {
        this.latitudeList = latitudeList;
        this.longitudeList = longitudeList;
    }

    @Override
    protected ClusterManager<MyItem> doInBackground(ClusterManager<MyItem>...clusterManagers) {

        myItemClusterManager = clusterManagers[0];
        for (int i = 0; i < longitudeList.size(); i++) {
            MyItem Item = new MyItem(latitudeList.get(i), longitudeList.get(i),
                    "cell tower", "");
            myItemClusterManager.addItem(Item);
        }
        return myItemClusterManager;
    }
}
