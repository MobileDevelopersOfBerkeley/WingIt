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
        String name;
        String date;
        ArrayList<String> activityKeyList;
        String mapURL;

        public Adventure(String name, String date, ArrayList<String> activityKeyList, String mapURL) {
            this.name = name;
            this.date = date;
            this.activityKeyList = activityKeyList;
            this.mapURL = mapURL;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String newName) {
            this.name = newName;
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
