package com.example.eartquake2;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;



import java.util.ArrayList;

public class AsyncLoader extends AsyncTaskLoader<ArrayList<Eathquakehelperclass>> {
    String url;
    public AsyncLoader(Context context, String url) {
        super(context);
        url = url;
    }

    @Override
    public ArrayList<Eathquakehelperclass> loadInBackground() {
        if(url==null) return null;

        ArrayList<Eathquakehelperclass> earthquakes=QueryUtils.FetchEarthquakeData(url);
        return earthquakes;
    }

    @Override
    protected void onStartLoading() {
//        super.onStartLoading();
        forceLoad();
    }
}
