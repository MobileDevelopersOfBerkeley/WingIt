package com.mdb.wingit.wingit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kedar Thakkar on 2/25/17.
 */

public class PastAdventuresAdapter extends RecyclerView.Adapter<PastAdventuresAdapter.CustomViewHolder> {

    private Context context;
    private ArrayList<Adventure> pastAdventures;

    public PastAdventuresAdapter(Context context, ArrayList<Adventure> pastAdventures) {
        this.context = context;
        this.pastAdventures = pastAdventures;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.past_adventure_row_view, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        Adventure currAdventure = pastAdventures.get(position);

        holder.locationName.setText(currAdventure.getStartloc());
        // TODO: Load image into holder.locationImage

    }

    @Override
    public int getItemCount() {
        return pastAdventures.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView locationImage;
        TextView locationName;

        public CustomViewHolder(View view) {
            super(view);

            this.locationImage = (ImageView) view.findViewById(R.id.past_adventure_row_view_image);
            this.locationName = (TextView) view.findViewById(R.id.past_adventure_row_view_text);

            // TODO: Set up response to rows being clicked
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}
