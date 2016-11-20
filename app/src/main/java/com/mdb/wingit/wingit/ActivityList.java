package com.mdb.wingit.wingit;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;

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
        String photoRef;
        String placeID;
        String rating;
        Bitmap image;

        public Activity(){}

        public Activity(String name, String time, String pictureURL) {
            this.name = name;
            this.time = time;
        }

        public String getName() {
            return this.name;
        }
        public String getTime() {
            return this.time;
        }
        public Bitmap getImage(){ return this.image; }

        public void setName(String name){ this.name = name;}
        public void setPhotoRef(String ref){ this.photoRef = ref;}
        public void setPlaceID(String id){this.placeID = id;}
        public void setRating(String rating){this.rating = rating;}
        public void setBitmap(Bitmap image){this.image=image;}
    }
}
