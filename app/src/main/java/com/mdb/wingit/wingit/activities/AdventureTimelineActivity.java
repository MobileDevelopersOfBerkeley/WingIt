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
    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adventure_timeline);

        //Set up Timeline Recycler View
        RecyclerView rv = (RecyclerView) findViewById(R.id.timeline_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdventureTimelineAdapter(this, pinList);
        rv.setAdapter(adapter);

        //Get information from intent
        Bundle intentExtras = getIntent().getExtras();
        //FIXME it's a little confusing to people reading later that it can't be null. Use a final variable DEFAULT_STRING = ""
        String adventureKey = intentExtras.getString("adventureKey", "");

        //Read data from Firebase
        if (adventureKey.equals("")) {
            ArrayList<String> pinKeys = intentExtras.getStringArrayList("pinKeys");
            getPinList(pinKeys);
        } else {
            getFirebaseData(adventureKey);
        }

        //UI Elements
        //FIXME can be reduced to findViewById(R.id.closeButton).setOnClickListener(...)
        ImageView close = (ImageView) findViewById(R.id.closeButton);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
                        //TODO: Order pins by startTime
                        if (pin != null) {
                            pinList.add(pin);
                            adapter.notifyDataSetChanged();
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
}
