package com.mdb.wingit.wingit.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.mdb.wingit.wingit.R;

/**
 * Displays pins of locations user has visited on his/her current adventure
 */

public class PinMapActivity extends AppCompatActivity {

    private FloatingActionButton continueAdventure;
    private ImageView upArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_map);

        continueAdventure = (FloatingActionButton) findViewById(R.id.continueAdventure);
        upArrow = (ImageView) findViewById(R.id.uparrow);

        continueAdventure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CategorySelectorActivity.class));
            }
        });

        upArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AdventureTimelineActivity.class));
            }
        });
    }
}
