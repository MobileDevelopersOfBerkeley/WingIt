package com.mdb.wingit.wingit.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mdb.wingit.wingit.R;
import com.mdb.wingit.wingit.modelClasses.Pin;

import java.util.ArrayList;

/**
 * Displays timeline with all the pins of the user's current adventure
 */

public class AdventureTimelineActivity extends AppCompatActivity {

    private AdventureTimelineAdapter adapter;
    private ArrayList<Pin> pinList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adventure_timeline);

        RecyclerView rv = (RecyclerView) findViewById(R.id.timeline_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdventureTimelineAdapter(this, pinList);

        getFirebaseData();

        ImageView close = (ImageView) findViewById(R.id.closeButton);
        Button endTrip = (Button) findViewById(R.id.endtrip);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PinMapActivity.class));
            }
        });

        endTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CategorySelectorActivity.class));
            }
        });
    }

    private void getFirebaseData() {
        //TODO: Get current adventure key from intent extra
        String adventureKey = "";
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference adventureRef = dbRef.child("Adventures").child(adventureKey);
    }
}
