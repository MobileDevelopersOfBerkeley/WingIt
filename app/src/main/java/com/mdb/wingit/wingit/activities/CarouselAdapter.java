package com.mdb.wingit.wingit.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mdb.wingit.wingit.R;
import com.mdb.wingit.wingit.modelClasses.Pin;

import java.util.ArrayList;

/**
 * Recycler View of pins that populate Carousel Activity
 */

class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.CustomViewHolder> {

    private Context context;
    private ArrayList<Pin> pins = new ArrayList<>();

    CarouselAdapter(Context context, ArrayList<Pin> pins) {
        this.context = context;
        this.pins = pins;
    }

    @Override
    public int getItemCount() {
        if (pins == null) {
            return 0;
        }
        return pins.size();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_carousel, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        Pin pin = pins.get(position);
        holder.pinTitle.setText(pin.getName());
        holder.pinRating.setText(pin.getRating());
        String pinPicURL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                + pin.getImageURL() + "&key=" + CarouselActivity.API_KEY_UNRESTRICTED;
        Glide.with(context).load(pinPicURL).into(holder.pinPic);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView pinTitle;
        TextView pinRating;
        ImageView pinPic;
        FloatingActionButton pinSelect;
        int position;

        public CustomViewHolder (View view) {
            super(view);
            this.pinTitle = (TextView) view.findViewById(R.id.pinTitle);
            this.pinRating = (TextView) view.findViewById(R.id.pinRating);
            this.pinPic = (ImageView) view.findViewById(R.id.pinPic);
            this.pinSelect = (FloatingActionButton) view.findViewById(R.id.go);
            this.pinSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    position = getAdapterPosition();
                    String coordinates = pins.get(position).getLatitude()+","+pins.get(position).getLongitude();
                    Intent pinMapIntent = new Intent(context, PinMapActivity.class);
                    pinMapIntent.putExtra("coordinates", coordinates);
                    pinMapIntent.putExtra("name", pins.get(position).getName());
                    context.startActivity(pinMapIntent);
                }
            });
        }
    }
}
