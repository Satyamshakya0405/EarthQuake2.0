package com.example.eartquake2;

public class Eathquakehelperclass {
    private String mLocation;
    private long time;
    private double mMag;
    private String url;



    public Eathquakehelperclass(double mMag, String mLocation, long time, String url) {
        this.mMag = mMag;
        this.mLocation = mLocation;
        this.time = time;
        this.url=url;
    }

    // gettters
    public double getmMag() {
        return mMag;
    }
    public String getmLocation() {
        return mLocation;
    }
    public long getTime() {
        return time;
    }
    public String getUrl() {
        return url;
    }
}
