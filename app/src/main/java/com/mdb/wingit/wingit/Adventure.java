package com.mdb.wingit.wingit;

import java.util.ArrayList;

/**
 * Created by KJ on 11/27/16.
 */

public class Adventure {

    private String first;
    private String last;
    private String date;
    private ArrayList<String> activityKeyList;
    private String mapURL;

    public Adventure() {

    }
    public Adventure(String date, String mapURL) {
        this.date = date;
        this.mapURL = mapURL;
        this.activityKeyList = new ArrayList<>();
    }

    public String getFirst() {
        return this.first;
    }

    public String getLast() {
        return this.last;
    }

    public void setFirst(String newName) {
        this.first = newName;
    }

    public void setLast(String newName) {
        this.last = newName;
    }

    public String getDate() {
        return this.date;
    }

    public ArrayList<String> getActivityKeyList() {
        return this.activityKeyList;
    }

    public void addActivity(String activityKey) {
        activityKeyList.add(activityKey);
    }

    public String getMapURL() {
        return this.mapURL;
    }
}
