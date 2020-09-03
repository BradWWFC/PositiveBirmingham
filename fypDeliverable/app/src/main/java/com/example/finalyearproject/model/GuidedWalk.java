package com.example.finalyearproject.model;


public class GuidedWalk {

    private long numberOfLocations;
    private long GuidedWalkID;

    private String Name;
    private String Thumbnail;

    private String Description;

    public GuidedWalk(){}

    public GuidedWalk(long GuidedWalkID, String mName, long NumberOfLocations, String Thumbnail, String Description) {
        this.GuidedWalkID = GuidedWalkID;
        this.Name = mName;
        this.numberOfLocations = NumberOfLocations;
        this.Thumbnail = Thumbnail;
        this.Description = Description;
    }

    public GuidedWalk(String mName) {
        this.Name = mName;
    }

    public long getGuidedWalkID() {
        return GuidedWalkID;
    }

    public void setGuidedWalkID(long guidedWalkID) {
        this.GuidedWalkID = guidedWalkID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public long getNumberOfLocations() {
        return numberOfLocations;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public String getDescription() {
        return Description; }

    public void setDescription(String description) {
        Description = description; }
}
