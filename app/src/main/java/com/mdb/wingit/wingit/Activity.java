package com.mdb.wingit.wingit;

import android.graphics.Bitmap;

/**
 * Created by KJ on 11/27/16.
 */

public class Activity {
    private String name;
    private String time;
    private String photoRef;
    private String placeID;
    private String rating;
    private Bitmap image;

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
