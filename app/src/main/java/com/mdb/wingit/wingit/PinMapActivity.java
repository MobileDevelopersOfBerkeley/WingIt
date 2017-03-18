package com.mdb.wingit.wingit;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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
