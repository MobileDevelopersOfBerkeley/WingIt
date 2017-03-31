package com.mdb.wingit.wingit.modelClasses;

import java.util.ArrayList;

/**
 * Created by KJ on 11/27/16.
 */

public class Adventure {

    private String startLoc;
    private String startDate;
    private String imageURL;
    private ArrayList<Pin> pins;

    public Adventure() {

    }

    public Adventure(String startLoc, String date, String imageURL, ArrayList<Pin> pins) {
        this.startLoc = startLoc;
        this.startDate = date;
        this.imageURL = imageURL;
        this.pins = pins;
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

    public ArrayList<Pin> getPins() {
        return pins;
    }

    // TODO: Are these setter methods really necessary?
    public void setStartLoc(String loc) {
        startLoc = loc;
    }

    public void setDate(String date) {
        this.startDate = date;
    }

}
