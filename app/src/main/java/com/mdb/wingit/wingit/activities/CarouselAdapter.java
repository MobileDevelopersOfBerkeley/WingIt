package com.mdb.wingit.wingit.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mdb.wingit.wingit.R;
import com.mdb.wingit.wingit.modelClasses.Adventure;
import com.mdb.wingit.wingit.modelClasses.Pin;

import java.util.ArrayList;

/**
 * Recycler View of pins that populate Carousel Activity
 */

class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.CustomViewHolder> {

    private Context context;
    private ArrayList<Pin> pins;
    private String adventureKey;
    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    private Adventure currAdventure;
    DatabaseReference adventureRef;

    CarouselAdapter(Context context, ArrayList<Pin> pins, String key) {
        this.context = context;
        this.pins = pins;
        this.adventureKey = key;
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
        //FIXME i crie everytim, use the final var from earlier
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
                    Pin pin = pins.get(position);
                    double pinLat = Double.parseDouble(pin.getLatitude());
                    double pinLong = Double.parseDouble(pin.getLongitude());
                    String coordinates = pin.getLatitude() + "," + pin.getLongitude();
                    //FIXME delet this
                    String pinKey = startNewPin(pin);

                    //Open PinMapActivity
                    Intent pinMapIntent = new Intent(context, PinMapActivity.class);
                    pinMapIntent.putExtra("coordinates", coordinates);
                    pinMapIntent.putExtra("pinLat", pinLat);
                    pinMapIntent.putExtra("pinLong", pinLong);
                    pinMapIntent.putExtra("name", pin.getName());
                    pinMapIntent.putExtra("adventureKey", adventureKey);
                    context.startActivity(pinMapIntent);
                }
            });
        }
    }

    /** Generate pin key in database for user's selected pin */
    private String startNewPin(Pin pin) {
        DatabaseReference pinRef = dbRef.child("Pins");
        String pinKey = pinRef.push().getKey();
        pinRef.child(pinKey).setValue(pin);
        updateCurrAdventure(pinKey);
        return pinKey;
    }

    /** Add pin key to Adventures node in database */
    private void updateCurrAdventure(final String pinKey) {
        adventureRef = dbRef.child("Adventures").child(adventureKey);
        adventureRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currAdventure = dataSnapshot.getValue(Adventure.class);

                if (currAdventure != null) {
                    currAdventure.addPinKey(pinKey);
                    adventureRef.setValue(currAdventure);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Database Error", databaseError.toString());
                Toast.makeText(context, "Failed to get current adventure", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
