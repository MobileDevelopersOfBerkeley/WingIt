package com.mdb.wingit.wingit.modelClasses;

import java.util.ArrayList;

/**
 * Collection of pins centered around a general location
 */

public class Adventure {
    private String startLoc;
    private String startDate;
    private String imageURL;
    private ArrayList<String> pinKeysList;

    public Adventure() {

    }

    public Adventure(String startLoc, String date, String imageURL) {
        this.startLoc = startLoc;
        this.startDate = date;
        this.imageURL = imageURL;
        this.pinKeysList = new ArrayList<>();
    }

    public String getStartLoc() {
        return startLoc;
    }

    public String getDate() {
        return startDate;
    }

    public String getImageURL() {
        return imageURL;
    }

    public ArrayList<String> getPinKeysList() {
        if (pinKeysList == null) {
            return new ArrayList<>();
        }
        return pinKeysList;
    }

    public void addPinKey(String key) {
        if(pinKeysList == null) {
            pinKeysList = new ArrayList<>();
        }
        this.pinKeysList.add(key);
    }

    @Override
    public String toString() {
        if (pinKeysList != null) {
            return startLoc + " " + startDate + " " + imageURL + " " + pinKeysList.toString();
        }

        return startLoc + " " + startDate + " " + imageURL;

    }
}
