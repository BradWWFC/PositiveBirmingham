package com.example.finalyearproject;

import android.util.Log;

import com.example.finalyearproject.model.Location;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class NearestNeighbour {


    private ArrayList<Location> parameter;

    public ArrayList<Location> calculate(ArrayList<Location> parameter, double[] latlng) {

        ArrayList<Integer> playlist = new ArrayList<>();
        this.parameter = parameter;
        ArrayList<Location> finalList = new ArrayList<>();


        //get the locations coordinates and put them into double arrays, stored in arraylists
        ArrayList<double[]> arrays = new ArrayList<>();
        arrays.add(latlng);
        for (Location x : parameter) {
            double doubleLong = Double.parseDouble(x.getLONGITUDE());
            double doubleLat = Double.parseDouble(x.getLATITUDE());

            double[] coords = {doubleLat, doubleLong};
            arrays.add(coords);
        }


        ArrayList<ArrayList<Double>> distanceMatrix = new ArrayList<>();


        //calculate the distance between each location and store them in 'distanceMatrix'
        //example: [0.0, 7876.454782088205, 7877.133704884506, 7876.888681199999, 7876.454782088205, 7877.133704884506, 7876.888681199999]

        for (double[] x : arrays) {
            ArrayList<Double> locationDistances = new ArrayList<>();
            double[] locationX = x;
            for (double[] y : arrays) {
                haversine hav = new haversine(locationX[0], locationX[1], y[0], y[1]);
                locationDistances.add(hav.calculate());

            }
            distanceMatrix.add(locationDistances);
        }


        //selectedLoc records which array to go to/is currently on
        //for loop starts in the users location, goes through the first arrayList to find the shortest distance
        //the index of the shortest distance (stored in smallestDistancePos during the loop) is then stored in 'selectedLoc'
        //then all positions in the other arrays with that index are replaced with '0.0', meaning the loop will skip those positions,
        //this prevents the user visiting the same place twice
        //in addition, after the first selectedLoc, all distances with a index of 0 are replaced with 0, so the user doesn't get directed
        //back to their starting point during the trip.
        int selectedLoc = 0;
        for (int i = 0; i < distanceMatrix.size() - 1; i++) {
            ArrayList<Double> location = distanceMatrix.get(selectedLoc);

            double smallestDistance = 1000000000;
            int smallestDistancePos = 0;

            int count = 0;
            for (int distance = 0; distance < location.size(); distance++) {
                Double x = location.get(distance);
                if (x > 0.0 && x < smallestDistance) {
                    smallestDistance = x;
                    smallestDistancePos = count;
                }
                count++;
            }
            playlist.add(smallestDistancePos);
            selectedLoc = smallestDistancePos;


            for (List<Double> x : distanceMatrix) {
                x.set(selectedLoc, 0.0);

                if (playlist.size() == 1) {
                    x.set(0, 0.0);
                }
            }
        }

        for (int i = 0; i < playlist.size(); i++) {
            finalList.add(parameter.get(playlist.get(i) - 1));
        }

        return finalList;
    }


}
