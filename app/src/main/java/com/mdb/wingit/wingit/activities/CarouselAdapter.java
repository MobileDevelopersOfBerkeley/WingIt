package com.mdb.wingit.wingit.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
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
 * Created by KJ on 11/11/16.
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
        if (pins == null || pins.size() == 0) {
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
        CardView currCard;
        int position;

        public CustomViewHolder (View view) {
            super(view);
            this.pinTitle = (TextView) view.findViewById(R.id.pinTitle);
            this.pinRating = (TextView) view.findViewById(R.id.pinRating);
            this.pinPic = (ImageView) view.findViewById(R.id.pinPic);
            this.currCard = (CardView) view.findViewById(R.id.card_curr_option);
            this.currCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    position = getAdapterPosition();
                    String coordinates = pins.get(position).getLatitude()+","+pins.get(position).getLongitude();
                    /*Intent mapsIntent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?daddr=" + coordinates));
                    context.startActivity(mapsIntent);*/

                    Intent pinMapIntent = new Intent(context, PinMapActivity.class);
                    pinMapIntent.putExtra("coordinates", coordinates);
                    context.startActivity(pinMapIntent);
                }
            });
        }
    }
}
