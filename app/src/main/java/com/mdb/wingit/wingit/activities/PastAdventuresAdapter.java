package com.mdb.wingit.wingit.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mdb.wingit.wingit.R;
import com.mdb.wingit.wingit.modelClasses.Adventure;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Recycler View of adventures that populate Past Adventures Activity
 */

class PastAdventuresAdapter extends RecyclerView.Adapter<PastAdventuresAdapter.CustomViewHolder> {

    private Context context;
    private ArrayList<Adventure> adventures;

    PastAdventuresAdapter(Context context, ArrayList<Adventure> adventures) {
        this.context = context;
        this.adventures = adventures;
    }

    @Override
    public int getItemCount() {
        if (adventures == null) {
            return 0;
        }
        return adventures.size();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_past_adventure, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Adventure adventure = adventures.get(position);
        holder.locationName.setText(adventure.getStartLoc());
        holder.locationDate.setText(adventure.getDate());
        //TODO: Verify that getImageURL() returns a valid URL
        Glide.with(context).load(adventure.getImageURL()).into(holder.locationImage);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView locationImage;
        TextView locationName;
        TextView locationDate;
        CardView locationCard;

        CustomViewHolder(View view) {
            super(view);
            locationImage = (ImageView) view.findViewById(R.id.locationImage);
            locationName = (TextView) view.findViewById(R.id.locationText);
            locationDate = (TextView) view.findViewById(R.id.locationDate);
            locationCard = (CardView) view.findViewById(R.id.adventureCard);
            locationCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Adventure adventure = adventures.get(position);
                    ArrayList<String> pinKeys = adventure.getPinKeysList();
                    Intent timelineIntent = new Intent(context, AdventureTimelineActivity.class);
                    timelineIntent.putExtra("pinKeys", pinKeys);
                    timelineIntent.putExtra("fromPastAdventure", true);
                    context.startActivity(timelineIntent);
                }
            });
        }
    }
}
