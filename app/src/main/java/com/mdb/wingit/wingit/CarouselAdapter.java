package com.mdb.wingit.wingit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.Places;

import java.net.URI;
import java.util.ArrayList;

/**
 * Created by KJ on 11/11/16.
 */

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.CustomViewHolder> {

    private Context context;

    private ArrayList<ActivityList.Activity> activities = new ArrayList();

    public CarouselAdapter(Context context, ArrayList<ActivityList.Activity> activities) {
        this.context = context;
        this.activities = activities;
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_carousel, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, int position) {
        ActivityList.Activity activity = activities.get(position);
        holder.activityTitle.setText(activity.name);
        // holder.activityPic.setImageURI((URI) activity.pictureURL);

    }



    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView activityTitle;
        ImageView activityPic;

        public CustomViewHolder (View view) {
            super(view);
            this.activityTitle = (TextView) view.findViewById(R.id.activityTitle);
            this.activityPic = (ImageView) view.findViewById(R.id.activityPic);

        }
    }



}
