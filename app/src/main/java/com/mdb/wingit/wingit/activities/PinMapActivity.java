package com.mdb.wingit.wingit.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mdb.wingit.wingit.R;

/**
 * Displays pins of locations user has visited on his/her current adventure
 */

public class PinMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private String adventureKey;
    private String pinKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_map_example);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_map_example);

//        //UI elements
//        FloatingActionButton continueAdventure = (FloatingActionButton) findViewById(R.id.continueAdventure);
//        ImageView arrow = (ImageView) findViewById(R.id.uparrow);
//        TextView name = (TextView) findViewById(R.id.pinName);
//
//        //Get information from intent
//        Bundle intentExtras = getIntent().getExtras();
//        name.setText(intentExtras.getString("name"));
//        String coordinates = intentExtras.getString("coordinates");
//        adventureKey = intentExtras.getString("adventureKey");
//        //TODO: Consider whether this is necessary information
//        pinKey = intentExtras.getString("pinKey");
//
//        continueAdventure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent selectorIntent = new Intent(getApplicationContext(), CategorySelectorActivity.class);
//                selectorIntent.putExtra("adventureKey", adventureKey);
//                startActivity(selectorIntent);
//            }
//        });
//
//        arrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent timelineIntent = new Intent(getApplicationContext(), AdventureTimelineActivity.class);
//                timelineIntent.putExtra("adventureKey", adventureKey);
//                startActivity(timelineIntent);
//            }
//        });
//
//        //Navigate user to destination using Google Maps
//        Intent mapsIntent = new Intent(android.content.Intent.ACTION_VIEW,
//                Uri.parse("http://maps.google.com/maps?daddr=" + coordinates));
//        startActivity(mapsIntent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
