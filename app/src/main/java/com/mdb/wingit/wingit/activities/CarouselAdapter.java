package com.mdb.wingit.wingit.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
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
    private static ArrayList<Pin> pins;
    private static String adventureKey;
    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    private Adventure currAdventure;
    private DatabaseReference adventureRef;
    private static LatLng currLoc;


    CarouselAdapter(Context context, ArrayList<Pin> pinsList, String key, LatLng location) {
        this.context = context;
        pins = pinsList;
        adventureKey = key;
        currLoc = location;
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
        // regular
        holder.pinTitle.setText(pin.getName());
        holder.pinRating.setText(pin.getRating());
        holder.ratingBar.setEnabled(false);
        float rating = Float.parseFloat(pin.getRating());
        holder.ratingBar.setRating(rating);
        String pinPicURL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                + pin.getImageURL() + "&key=" + CarouselActivity.API_KEY_UNRESTRICTED;
        Glide.with(context).load(pinPicURL).into(holder.pinPic);
        Typeface reg = Typeface.createFromAsset(context.getAssets(),"fonts/Quicksand-Regular.ttf");
        holder.pinTitle.setTypeface(reg);
        holder.pinRating.setTypeface(reg);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        // Regular Card
        CardView card;
        TextView pinTitle;
        TextView pinRating;
        ImageView pinPic;
        RatingBar ratingBar;

        public CustomViewHolder (View view) {
            super(view);
            // regular
            this.pinTitle = (TextView) view.findViewById(R.id.pinTitle);
            this.pinRating = (TextView) view.findViewById(R.id.pinRating);
            this.pinPic = (ImageView) view.findViewById(R.id.pinPic);
            this.ratingBar = (RatingBar) view.findViewById(R.id.ratingbar);
            this.card = (CardView) view.findViewById(R.id.card_curr_option);
            this.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args = new Bundle();
                    args.putInt("position", getAdapterPosition());
                    CustomDialogFragment dialogFragment = new CustomDialogFragment();
                    dialogFragment.setArguments(args);
                    dialogFragment.show(CarouselActivity.fragmentManager, "CustomFragment");
                }
            });
        }

    }

    public static class CustomDialogFragment extends android.support.v4.app.DialogFragment {

        TextView expandTitle;
        TextView expandRating;
        TextView expandAddress;
        TextView expandPhone;
        ImageView pinMap;
        FloatingActionButton go2;
        int position;
        Context context;
        RatingBar ratingBar;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            View view = inflater.inflate(R.layout.fragment_dialog, null);
            builder.setView(view);
            this.expandTitle = (TextView) view.findViewById(R.id.expanded_title);
            this.expandRating = (TextView) view.findViewById(R.id.expanded_rating);
            this.expandAddress = (TextView) view.findViewById(R.id.expanded_address);
            this.expandPhone = (TextView) view.findViewById(R.id.expanded_phone);
            this.pinMap = (ImageView) view.findViewById(R.id.pinMap);
            this.go2 = (FloatingActionButton) view.findViewById(R.id.go2);
            Bundle args = getArguments();
            this.position = (int) args.get("position");
            this.context = getContext();
            this.ratingBar = (RatingBar) view.findViewById(R.id.ratingbarexpanded);

            Pin pin = pins.get(position);
            expandTitle.setText(pin.getName());
            expandRating.setText(pin.getRating());
            expandAddress.setText(pin.getAddress());
            expandPhone.setText(pin.getPhone());
            ratingBar.setEnabled(false);
            float rating = Float.parseFloat(pin.getRating());
            ratingBar.setRating(rating);
            String pinLat = pin.getLatitude();
            String pinLong = pin.getLongitude();
            double currLat = currLoc.latitude;
            double currLong = currLoc.longitude;
            String pinMapURL = "https://maps.googleapis.com/maps/api/staticmap?size=512x512&maptype=roadmap&markers=size:mid%7Ccolor:orange%7Clabel:B%7C"
                    + pinLat + "," + pinLong + "&markers=size:mid%7Ccolor:blue%7Clabel:A%7C" + currLat + "," + currLong + "&key=" + CarouselActivity.API_KEY_UNRESTRICTED;
            Glide.with(context).load(pinMapURL).into(pinMap);
            go2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Pin pin = pins.get(position);
                    double pinLat = Double.parseDouble(pin.getLatitude());
                    double pinLong = Double.parseDouble(pin.getLongitude());
                    String coordinates = pin.getLatitude() + "," + pin.getLongitude();

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

            Typeface reg = Typeface.createFromAsset(context.getAssets(),"fonts/Quicksand-Regular.ttf");
            expandTitle.setTypeface(reg);
            expandRating.setTypeface(reg);
            expandAddress.setTypeface(reg);
            expandPhone.setTypeface(reg);

            return builder.create();
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

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

}
