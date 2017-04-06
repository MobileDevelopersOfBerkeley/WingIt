package com.mdb.wingit.wingit.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mdb.wingit.wingit.R;
import com.mdb.wingit.wingit.modelClasses.Adventure;
import com.mdb.wingit.wingit.modelClasses.User;

import java.util.ArrayList;

/**
 * Displays user's past adventures
 */

public class PastAdventuresActivity extends AppCompatActivity {

    private PastAdventuresAdapter adapter;
    private ArrayList<Adventure> adventureList = new ArrayList<>();
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_adventures);

        //Set up Past Adventures Recycler View
        RecyclerView rv = (RecyclerView) findViewById(R.id.past_adventures_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PastAdventuresAdapter(this, adventureList);
        rv.setAdapter(adapter);

        //Read data from Firebase
        getFirebaseData();

        //UI Elements
        ImageView arrow = (ImageView) findViewById(R.id.arrow);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /** Retrieve list of past adventures from Firebase for current user */
    private void getFirebaseData() {
        DatabaseReference userRef = dbRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currUser = dataSnapshot.getValue(User.class);
                if (currUser != null) {
                    ArrayList<String> adventureKeys = currUser.getAdventureKeysList();
                    if (adventureKeys != null) {
                        getAdventureList(adventureKeys);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Database Error", databaseError.toString());
            }
        });
    }

    /** Update recycler view of adventures according to list stored in Firebase */
    private void getAdventureList(final ArrayList<String> adventureKeys) {
        DatabaseReference adventureRef = dbRef.child("Adventures");
        adventureRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (adventureKeys.contains(ds.getKey())) {
                        Adventure adventure = ds.getValue(Adventure.class);
                        //TODO: Order adventures by startDate
                        if (adventure != null) {
                            adventureList.add(adventure);
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
