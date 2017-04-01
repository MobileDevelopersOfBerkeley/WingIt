package com.mdb.wingit.wingit.activities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mdb.wingit.wingit.R;
import com.mdb.wingit.wingit.modelClasses.Adventure;

import java.util.ArrayList;

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
        // TODO: Load image into holder.locationImage

    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView locationImage;
        TextView locationName;

        CustomViewHolder(View view) {
            super(view);
            this.locationImage = (ImageView) view.findViewById(R.id.past_adventure_row_view_image);
            this.locationName = (TextView) view.findViewById(R.id.past_adventure_row_view_text);

            //TODO: Set up response to rows being clicked
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}
