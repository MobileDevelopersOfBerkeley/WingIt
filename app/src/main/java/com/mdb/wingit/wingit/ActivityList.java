package com.mdb.wingit.wingit;

import java.util.ArrayList;

/**
 * Created by reddy on 11/11/2016.
 */

public class ActivityList {
    public ArrayList<Activity> activityArrayList;

    public ActivityList() {
        this.activityArrayList = new ArrayList<>();
    }

    public ArrayList<Activity> getArrayList() {
        return this.activityArrayList;
    }

    public void addActivity(Activity activity) {
        this.activityArrayList.add(activity);
    }

    public static class Activity {
        String name;
        String time;
        String pictureURL;

        public Activity(String name, String time, String pictureURL) {
            this.name = name;
            this.time = time;
            this.pictureURL = pictureURL;
        }

        public String getName() {
            return this.name;
        }

        public String getTime() {
            return this.time;
        }

        public String getPictureURL() {
            return this.pictureURL;
        }
    }
}
