package com.example.finalyearproject;

import com.example.finalyearproject.model.Location;
import com.mapbox.mapboxsdk.geometry.LatLng;

public class haversine {

    private double lat2;
    private double lon2;
    private double lat1;
    private double lon1;


    public haversine(double lat1, double lon1, double lat2, double lon2) {
        this.lat1 = lat1;
        this.lon1 = lon1;
        this.lat2 = lat2;
        this.lon2 = lon2;
    }


    public double calculate(){
        // distance between latitudes and longitudes
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        // convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // apply formulae
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(lat1) *
                        Math.cos(lat2);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        return rad * c;
    }

}
