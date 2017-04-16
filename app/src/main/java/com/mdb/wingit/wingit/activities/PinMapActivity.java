package com.mdb.wingit.wingit.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
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
 * Displays pins of locations user has visited on his/her current adventure
 */

public class PinMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private String adventureKey;
    private double pinLat;
    private double pinLong;
    private String pinLocName;
    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    private ArrayList<Pin> pinList = new ArrayList<>();
    private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_map);

        //Get information from intent
        Bundle intentExtras = getIntent().getExtras();
        pinLocName = intentExtras.getString("name");
        String coordinates = intentExtras.getString("coordinates");
        pinLat = intentExtras.getDouble("pinLat");
        pinLong = intentExtras.getDouble("pinLong");
        adventureKey = intentExtras.getString("adventureKey");

        //Set up map of pins in current adventure
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        getFirebaseData(adventureKey);

        //UI elements
        ImageView arrow = (ImageView) findViewById(R.id.uparrow);
        TextView name = (TextView) findViewById(R.id.pinName);
        name.setText(intentExtras.getString("name"));

        FloatingActionButton continueFab = (FloatingActionButton) findViewById(R.id.continueFab);
        continueFab.setIconDrawable(getResources().getDrawable(R.drawable.ic_arrow_forward_black_24dp));

        FloatingActionButton endAdventureFab = (FloatingActionButton) findViewById(R.id.endAdventureFab);
        endAdventureFab.setIconDrawable(getResources().getDrawable(R.drawable.ic_check_black_24dp));

        //Configure UI buttons
        continueFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selectorIntent = new Intent(getApplicationContext(), CategorySelectorActivity.class);
                selectorIntent.putExtra("adventureKey", adventureKey);
                startActivity(selectorIntent);
            }
        });

        endAdventureFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CategorySelectorActivity.class));
            }
        });

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent timelineIntent = new Intent(getApplicationContext(), AdventureTimelineActivity.class);
                timelineIntent.putExtra("adventureKey", adventureKey);
                startActivity(timelineIntent);
            }
        });

        //Navigate user to destination using Google Maps
        Intent mapsIntent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr=" + coordinates));
        startActivity(mapsIntent);
    }

    /** Retrieve list of pins from Firebase for specified adventure */
    private void getFirebaseData(String adventureKey) {
        DatabaseReference adventureRef = dbRef.child("Adventures").child(adventureKey);
        adventureRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Adventure currAdventure = dataSnapshot.getValue(Adventure.class);
                if (currAdventure != null) {
                    ArrayList<String> pinKeys = currAdventure.getPinKeysList();
                    getPinList(pinKeys);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Database Error", databaseError.toString());
            }
        });
    }

    /** Update recycler view of pins according to list stored in Firebase */
    private void getPinList(final ArrayList<String> pinKeys) {
        DatabaseReference pinRef = dbRef.child("Pins");
        pinRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (pinKeys.contains(ds.getKey())) {
                        Pin pin = ds.getValue(Pin.class);
                        if (pin != null) {
                            pinList.add(pin);
                        }
                    }
                }
                mapFragment.getMapAsync(PinMapActivity.this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Database Error", databaseError.toString());
            }
        });
    }

    /** Updates map to include all the pins in the user's current adventure */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Pin pin : this.pinList) {
            double lat = Double.parseDouble(pin.getLatitude());
            double lon = Double.parseDouble(pin.getLongitude());
            LatLng loc = new LatLng(lat, lon);
            builder.include(loc);
            googleMap.addMarker(new MarkerOptions().position(loc).title(pin.getName())
                    .icon(BitmapDescriptorFactory.defaultMarker(199)));
        }
        LatLng pinLoc = new LatLng(this.pinLat, this.pinLong);
        builder.include(pinLoc);
        googleMap.addMarker(new MarkerOptions().position(pinLoc)
                .title(this.pinLocName).icon(BitmapDescriptorFactory.defaultMarker(24)));

        LatLngBounds bounds = builder.build();
        int padding = 5;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.moveCamera(cu);
    }
}
