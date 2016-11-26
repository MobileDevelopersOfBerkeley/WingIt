package com.mdb.wingit.wingit;

import java.util.ArrayList;

/**
 * Created by reddy on 11/11/2016.
 */

public class AdventureList {
    public ArrayList<Adventure> adventureArrayList;

    public AdventureList() {
        this.adventureArrayList = new ArrayList<>();
    }

    public ArrayList<Adventure> getArrayList() {
        return this.adventureArrayList;
    }

    public void addAdventure(Adventure adventure) {
        this.adventureArrayList.add(adventure);
    }

    public static class Adventure {
        String first;
        String last;
        String date;
        ArrayList<String> activityKeyList;
        String mapURL;

        public Adventure(String name, String date, ArrayList<String> activityKeyList, String mapURL) {
            this.first = name;
            this.date = date;
            this.activityKeyList = activityKeyList;
            this.mapURL = mapURL;
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

        public String getMapURL() {
            return this.mapURL;
        }
    }
}
