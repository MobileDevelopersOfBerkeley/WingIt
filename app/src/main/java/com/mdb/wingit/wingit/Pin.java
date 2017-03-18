package com.mdb.wingit.wingit;

/**
 * Created by KJ on 2/22/17.
 */

public class Pin {
    String name;
    String placeID;
    String latitude;
    String longitude;
    String rating;
    String startTime;
    String imageURL;

    public Pin(String name, String placeID, String latitude, String longitude, String rating, String startTime, String imageURL) {
        this.name = name;
        this.placeID = placeID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
        this.startTime = startTime;
        this.imageURL = imageURL;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhotoRef(String iRef) {
        this.imageURL = iRef;
    }

    public void setPlaceID(String pID) {
        this.placeID = pID;
    }

    public void setLat(String lat) {
        this.latitude = lat;
    }

    public void setLon(String lon) {
        this.longitude = lon;
    }

    public void setRating(String rat) {
        this.rating = rat;
    }

    public void setStartTime(String time) {
        this.startTime = time;
    }
}
