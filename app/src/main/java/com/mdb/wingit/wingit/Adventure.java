package com.mdb.wingit.wingit;

import java.util.ArrayList;

/**
 * Created by KJ on 11/27/16.
 */

public class Adventure {

    private String startloc;
    private String date;

    public Adventure() {

    }
    public Adventure(String startloc, String date) {
        this.startloc = startloc;
        this.date = date;
    }

    public String getStartloc() {
        return this.startloc;
    }

    public String getDate() {
        return this.date;
    }

    public void setStartloc(String loc) {
        startloc = loc;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
