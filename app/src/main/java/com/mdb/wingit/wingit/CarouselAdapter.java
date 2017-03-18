package com.mdb.wingit.wingit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by KJ on 11/11/16.
 */

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.CustomViewHolder> {

    private Context context;
    private ArrayList<Pin> pins = new ArrayList();

    public CarouselAdapter(Context context, ArrayList<Pin> activities) {
        this.context = context;
        this.pins = pins;
    }

    @Override
    public int getItemCount() {
        if (pins == null){
            return 0;
        }
        else {
            return pins.size();
        }
        //return 4;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_carousel, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        Pin pin = pins.get(position);
        holder.activityTitle.setText(pin.name);
        Glide.with(context).load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+pin.photoRef+"&key="+ CarouselActivity.API_KEY_NONRESTRICTED).into(holder.activityPic);

    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView activityTitle;
        ImageView activityPic;
        CardView currCard;
        int position;

        public CustomViewHolder (View view) {
            super(view);
            this.activityTitle = (TextView) view.findViewById(R.id.activityTitle);
            this.activityPic = (ImageView) view.findViewById(R.id.activityPic);
            this.currCard = (CardView) view.findViewById(R.id.card_curr_option);
            currCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    position = getAdapterPosition();
                    String coordinates = pins.get(position).latitude+","+pins.get(position).longitude;
                    /*Intent mapsIntent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?daddr=" + coordinates));
                    context.startActivity(mapsIntent);*/

                    Intent pinMapIntent = new Intent(context, PinMapActivity.class);
                    pinMapIntent.putExtra("coordinates", coordinates);
                    context.startActivity(pinMapIntent);

                    /*final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent detailIntent = new Intent(context, DetailScreen.class);
                            detailIntent.putExtra("name", activities.get(position).getName());
                            detailIntent.putExtra("place_id", activities.get(position).getPlaceID());
                            detailIntent.putExtra("coordinates", activities.get(position).getLat()+","+activities.get(position).getLon());
                            detailIntent.putExtra("photoRef", activities.get(position).getPhotoRef());
                            context.startActivity(detailIntent);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    builder.setMessage("Are you sure you want to go to " + activities.get(position).getName() + "?");

                    AlertDialog dialog = builder.create();
                    dialog.show();*/
                }
            });
        }


    }



}
