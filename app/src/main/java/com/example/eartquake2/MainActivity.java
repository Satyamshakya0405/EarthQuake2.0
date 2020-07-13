package com.example.eartquake2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Eathquakehelperclass>> {

    private static final int EARTHQUAKE_LOADER_ID = 1;
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=3&maxmag=9";
    private static final String USGS_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    EarthquakeAdapter adapter;
    TextView emptyview;
    ProgressBar mProggbar;
    SwipeRefreshLayout mSwipeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView mlistview = findViewById(R.id.list);
         emptyview=findViewById(R.id.emptyview);
        mProggbar=findViewById(R.id.Progressbar);
        mSwipeLayout=findViewById(R.id.mswipelayout);
        adapter = new EarthquakeAdapter(this, new ArrayList<Eathquakehelperclass>());
        mlistview.setAdapter(adapter);
        mlistview.setEmptyView(findViewById(R.id.emptyview));
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("EarthQuakes");
//        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
//        task.execute(USGS_REQUEST_URL);
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        LoaderManager.LoaderCallbacks<ArrayList<Eathquakehelperclass>> callback = MainActivity.this;
        if(networkInfo==null)
        {
            mProggbar.setVisibility(View.INVISIBLE);
            emptyview.setText("No Internet");
        }
        else
        getSupportLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null, callback);

        // OnItem Click Listner for List View
        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Eathquakehelperclass eathquakehelperclass = adapter.getItem(position);

                Uri uri = Uri.parse(eathquakehelperclass.getUrl());
                Intent webintent = new Intent(Intent.ACTION_VIEW, uri);
                if (webintent.resolveActivity(getPackageManager()) != null) {
                    startActivity(webintent);
                }
            }
        });
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               mSwipeLayout.setRefreshing(true);
                updateUi();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       int id=item.getItemId();
       switch (id)
       {
           case R.id.settings:
               startActivity(new Intent(MainActivity.this,SettingsActivity.class));
               break;
       }
        return true;
    }

    @NonNull
    @Override
    public Loader<ArrayList<Eathquakehelperclass>> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new AsyncTaskLoader<ArrayList<Eathquakehelperclass>>(this) {
            @Nullable
            @Override
            public ArrayList<Eathquakehelperclass> loadInBackground() {
                SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                String minMagnitude=preferences.getString(getString(R.string.settings_min_magnitude_key),
                        getString(R.string.settings_min_magnitude_default));
                String orderBy = preferences.getString(
                        getString(R.string.settings_order_by_key),
                        getString(R.string.settings_order_by_default)
                );
                Uri baseUri = Uri.parse(USGS_URL);
                Uri.Builder uriBuilder = baseUri.buildUpon();
                uriBuilder.appendQueryParameter("format", "geojson");
//                uriBuilder.appendQueryParameter("limit", "100");
                uriBuilder.appendQueryParameter("minmag", minMagnitude);
                uriBuilder.appendQueryParameter("orderby", orderBy);
                ArrayList<Eathquakehelperclass> result = QueryUtils.FetchEarthquakeData(uriBuilder.toString());
                return result;
            }
            @Override
            protected void onStartLoading() {
//        super.onStartLoading();
                forceLoad();
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Eathquakehelperclass>> loader, ArrayList<Eathquakehelperclass> data) {
        adapter.clear();
        mSwipeLayout.setRefreshing(false);
        if(data==null||data.isEmpty())
        {
            mProggbar.setVisibility(View.INVISIBLE);
            emptyview.setText("No Data Found");
            Log.d("sattu","No data");
        }
        else if(data!=null){
            mProggbar.setVisibility(View.INVISIBLE);
            adapter.addAll(data);

        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Eathquakehelperclass>> loader) {
        adapter.clear();
    }


    private class EarthquakeAsyncTask extends AsyncTask<String,Void,ArrayList<Eathquakehelperclass>> {

        @Override
        protected ArrayList<Eathquakehelperclass> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            ArrayList<Eathquakehelperclass> result = QueryUtils.FetchEarthquakeData(urls[0]);
            return result;
        }


        @Override
        protected void onPostExecute(ArrayList<Eathquakehelperclass> data) {
            adapter.clear();
            if(data!=null)
                adapter.addAll(data);
        }
    }

    public void updateUi()
    {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        LoaderManager.LoaderCallbacks<ArrayList<Eathquakehelperclass>> callback = MainActivity.this;
        if(networkInfo==null)
        {
            mSwipeLayout.setRefreshing(false);
            mProggbar.setVisibility(View.INVISIBLE);
            adapter.clear();
            emptyview.setText("No Internet");
            Log.d("sattu","NO INTERNET");
        }
        else
            getSupportLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null, callback);

    }
}