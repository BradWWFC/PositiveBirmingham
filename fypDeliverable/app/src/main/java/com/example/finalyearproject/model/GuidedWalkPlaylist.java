package com.example.finalyearproject.model;

import java.util.ArrayList;

public class GuidedWalkPlaylist {

    public static ArrayList<Location> getWalkLocations() {
        return walkLocations;
    }

    public static void setWalkLocations(ArrayList<Location> walkLocations) {
        GuidedWalkPlaylist.walkLocations = walkLocations;
    }

    public static Location getWalkLocationsItem(int i) {
        return walkLocations.get(i);
    }

    public static void deleteWalkLocationsItem(int i){
        walkLocations.remove(i);
    }

    public static int getPlaylistLength(){
        return walkLocations.size();
    }

    public static void addWalkLocationsItem(Location x){
        walkLocations.add(x);
    }

    private static ArrayList<Location> walkLocations = new ArrayList<>();

    public static void addArrayToPlayList(ArrayList<com.example.finalyearproject.model.Location> locationArrayList) {
        for (com.example.finalyearproject.model.Location x : locationArrayList) {
            GuidedWalkPlaylist.addWalkLocationsItem(x);
        }
    }




}
