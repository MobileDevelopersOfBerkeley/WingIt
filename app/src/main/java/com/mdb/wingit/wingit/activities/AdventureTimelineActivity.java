package com.mdb.wingit.wingit.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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
 * Displays timeline with all the pins of the user's current adventure
 */

public class AdventureTimelineActivity extends AppCompatActivity {

    private AdventureTimelineAdapter adapter;
    private ArrayList<Pin> pinList = new ArrayList<>();
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adventure_timeline);

        //Set up Timeline Recycler View
        RecyclerView rv = (RecyclerView) findViewById(R.id.timeline_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdventureTimelineAdapter(this, pinList);

        //Read data from Firebase
        //TODO: Get current adventure key from intent extra
        String adventureKey = "";
        dbRef = FirebaseDatabase.getInstance().getReference();
        getFirebaseData(adventureKey);

        //UI Elements
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

    /** Retrieve list of pins from Firebase for specified adventure */
    private void getFirebaseData(String adventureKey) {
        DatabaseReference adventureRef = dbRef.child("Adventures").child(adventureKey);
        adventureRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Adventure currAdventure = dataSnapshot.getValue(Adventure.class);
                ArrayList<String> pinKeys = currAdventure.getPinKeysList();
                getPinList(pinKeys);
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
                        pinList.add(pin);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Database Error", databaseError.toString());
            }
        });
        adapter.notifyDataSetChanged();
    }
}
