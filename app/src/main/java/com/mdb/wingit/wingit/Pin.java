package com.mdb.wingit.wingit;

/**
 * Created by KJ on 2/22/17.
 */

public class Pin {
    String name;
    String photoRef;
    String placeID;
    String latitude;
    String longitude;

    public void setName(String name) {
        this.name = name;
    }

    public void setPhotoRef(String pRef) {
        this.photoRef = pRef;
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
}
