package com.mdb.wingit.wingit.modelClasses;

/**
 * Created by KJ on 2/22/17.
 */

public class Pin {
    private String name;
    private String placeID;
    private String latitude;
    private String longitude;
    private String rating;
    private String startTime;
    private String imageURL;

    public Pin(String name, String placeID, String latitude, String longitude, String rating, String startTime, String imageURL) {
        this.name = name;
        this.placeID = placeID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
        this.startTime = startTime;
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public String getPlaceID() {
        return placeID;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getRating() {
        return rating;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getImageURL() {
        return imageURL;
    }

    @Override
    public String toString() {
        return name + " " + placeID + " " + latitude + " " + longitude + " " + rating + " " + startTime;
    }
}
