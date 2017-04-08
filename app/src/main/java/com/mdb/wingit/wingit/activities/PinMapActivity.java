package com.mdb.wingit.wingit.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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

public class PinMapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private String adventureKey;
    private String pinKey;
    private double pinLat;
    private double pinLong;
    private FloatingActionButton continueAdventure;
    private ImageView arrow;
    private TextView name;
    private String pinLocName;
    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    private ArrayList<Pin> pinList = new ArrayList<>();

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
        //TODO: Consider whether this is necessary information
        pinKey = intentExtras.getString("pinKey");

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //UI elements
        continueAdventure = (FloatingActionButton) findViewById(R.id.continueAdventure);
        arrow = (ImageView) findViewById(R.id.uparrow);
        name = (TextView) findViewById(R.id.pinName);
        name.setText(intentExtras.getString("name"));

        // on clicks
        continueAdventure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selectorIntent = new Intent(getApplicationContext(), CategorySelectorActivity.class);
                selectorIntent.putExtra("adventureKey", adventureKey);
                startActivity(selectorIntent);
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
                    if (pinKeys != null) {
                        getPinList(pinKeys);
                    }
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
                        //TODO: Order pins by startTime
                        if (pin != null) {
                            pinList.add(pin);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Database Error", databaseError.toString());
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        Log.e("Pin", "TEST");
        getFirebaseData(adventureKey);
        Log.i("pinList", pinList.toString());
        for (Pin pin : this.pinList) {
            Log.e("Pin", "ADDING PIN");
            double lat = Double.parseDouble(pin.getLatitude());
            double lon = Double.parseDouble(pin.getLongitude());
            LatLng loc = new LatLng(lat, lon);
            googleMap.addMarker(new MarkerOptions().position(loc).title(pin.getName())
                    .icon(BitmapDescriptorFactory.defaultMarker(199)));
        }
        LatLng pinLoc = new LatLng(this.pinLat, this.pinLong);
        googleMap.addMarker(new MarkerOptions().position(pinLoc)
                .title(this.pinLocName).icon(BitmapDescriptorFactory.defaultMarker(24)));
        float zoomLevel = 16;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pinLoc, zoomLevel));

        // Set a listener for marker click.
        googleMap.setOnMarkerClickListener(this);
    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(this,
                    marker.getTitle() +
                            " has been clicked " + clickCount + " times.",
                    Toast.LENGTH_SHORT).show();
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }
}
