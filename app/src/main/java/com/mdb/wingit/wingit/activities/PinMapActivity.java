package com.mdb.wingit.wingit.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mdb.wingit.wingit.R;

/**
 * Displays pins of locations user has visited on his/her current adventure
 */

public class PinMapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_map);

        //UI elements
        FloatingActionButton continueAdventure = (FloatingActionButton) findViewById(R.id.continueAdventure);
        ImageView arrow = (ImageView) findViewById(R.id.uparrow);
        TextView name = (TextView) findViewById(R.id.pinName);

        //Get information from intent
        Bundle intentExtras = getIntent().getExtras();
        name.setText(intentExtras.getString("name"));
        String coordinates = intentExtras.getString("coordinates");

        continueAdventure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CategorySelectorActivity.class));
            }
        });

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AdventureTimelineActivity.class));
            }
        });

        //Navigate user to destination using Google Maps
        Intent mapsIntent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr=" + coordinates));
        startActivity(mapsIntent);
    }
}
