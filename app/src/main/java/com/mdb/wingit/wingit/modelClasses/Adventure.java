package com.mdb.wingit.wingit.modelClasses;

import java.util.ArrayList;

/**
 * Created by KJ on 11/27/16.
 */

public class Adventure {

    private String startLoc;
    private String startDate;
    private String imageURL;
    private ArrayList<String> pinKeysList;

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
        return pinKeysList;
    }

    public void addPinKey(String key) {
        this.pinKeysList.add(key);
    }
}
