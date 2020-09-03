package com.example.finalyearproject.model;

public class Location {

    private String EASTING;

    private String AMENDDATE;

    private String CAPTURESCA;

    private String GRADE;

    private String HYPERLINK;

    private String LEGACYUID;

    private String LISTENTRY;

    private String LOCATION;

    private String NAME;

    private String NGR;

    private String NORTHING;

    private String NORTHEASTINGING;

    private String LONGITUDE;

    private String LATITUDE;

    private String THUMBNAIL;

    private long LocationID;


    public Location(String AMENDDATE, String CAPTURESCA, String GRADE, String HYPERLINK, String LEGACYUID,
                    String LISTENTRY, String LOCATION, String NAME, String NGR, String NORTHING, String EASTING,
                    String NORTHEASTINGING, String LONGITUDE, String LATITUDE, String THUMBNAIL, long LocationID) {
        this.AMENDDATE = AMENDDATE;
        this.CAPTURESCA = CAPTURESCA;
        this.GRADE = GRADE;
        this.HYPERLINK = HYPERLINK;
        this.LEGACYUID = LEGACYUID;
        this.LISTENTRY = LISTENTRY;
        this.LOCATION = LOCATION;
        this.NAME = NAME;
        this.NGR = NGR;
        this.NORTHING = NORTHING;
        this.EASTING = EASTING;
        this.NORTHEASTINGING = NORTHEASTINGING;
        this.LONGITUDE = LONGITUDE;
        this.LATITUDE = LATITUDE;
        this.THUMBNAIL = THUMBNAIL;
        this.LocationID = LocationID;
    }

    public Location() {

    }

    public Location(double[] latlng){
        LATITUDE = String.valueOf(latlng[0]);
        LONGITUDE = String.valueOf(latlng[1]);
    }

    public Location(String AMENDDATE) {
        this.AMENDDATE = AMENDDATE;
    }

    public String getAMENDDATE() {
        return AMENDDATE;
    }

    public void setAMENDDATE(String AMENDDATE) {
        this.AMENDDATE = AMENDDATE;
    }

    public String getCAPTURESCA() {
        return CAPTURESCA;
    }

    public void setCAPTURESCA(String CAPTURESCA) {
        this.CAPTURESCA = CAPTURESCA;
    }

    public String getGRADE() {
        return GRADE;
    }

    public void setGRADE(String GRADE) {
        this.GRADE = GRADE;
    }

    public String getHYPERLINK() {
        return HYPERLINK;
    }

    public void setHYPERLINK(String HYPERLINK) {
        this.HYPERLINK = HYPERLINK;
    }

    public String getLEGACYUID() {
        return LEGACYUID;
    }

    public void setLEGACYUID(String LEGACYUID) {
        this.LEGACYUID = LEGACYUID;
    }

    public String getLISTENTRY() {
        return LISTENTRY;
    }

    public void setLISTENTRY(String LISTENTRY) {
        this.LISTENTRY = LISTENTRY;
    }

    public String getLOCATION() {
        return LOCATION;
    }

    public void setLOCATION(String LOCATION) {
        this.LOCATION = LOCATION;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getNGR() {
        return NGR;
    }

    public void setNGR(String NGR) {
        this.NGR = NGR;
    }

    public String getNORTHING() {
        return NORTHING;
    }

    public void setNORTHING(String NORTHING) {
        this.NORTHING = NORTHING;
    }

    public String getNORTHEASTINGING() {
        return NORTHEASTINGING;
    }

    public void setNORTHEASTINGING(String NORTHEASTINGING) {
        this.NORTHEASTINGING = NORTHEASTINGING;
    }

    public String getEASTING() {
        return EASTING;
    }

    public void setEASTING(String EASTING) {
        this.EASTING = EASTING;
    }

    public String getLONGITUDE() {
        return LONGITUDE;
    }

    public void setLONGITUDE(String LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public String getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(String LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public String getTHUMBNAIL() {
        return THUMBNAIL;
    }

    public void setTHUMBNAIL(String THUMBNAIL) {
        this.THUMBNAIL = THUMBNAIL;
    }

    public long getLocationID() {
        return LocationID;
    }

    public void setLocationID(long locationID) {
        LocationID = locationID;
    }
}
