package com.safetylifeproperty.slpro.slp;

/**
 * Created by admin on 2017-09-03.
 */

public class MarkerItem {
    double lat;
    double lon;
    String status;
    public MarkerItem(double lat, double lon, String status) {
        this.lat = lat;
        this.lon = lon;
        this.status = status;
    }
    public double getLat() {
        return lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
    public double getLon() {
        return lon;
    }
    public void setLon(double lon) {
        this.lon = lon;
    }
    public String getSatus() {
        return status;
    }
    public void setPrice(String status) {
        this.status = status;
    }
}

