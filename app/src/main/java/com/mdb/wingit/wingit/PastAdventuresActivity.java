package com.mdb.wingit.wingit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class PastAdventuresActivity extends AppCompatActivity {

    public PastAdventuresAdapter pastAdventuresAdapater;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_adventures);

        RecyclerView pastAdventureRecyclerView = (RecyclerView) findViewById(R.id.past_adventures_recycler_view);
        pastAdventureRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // TODO: Load past activities from Firebase into an ArrayList
        Adventure temp1 = new Adventure("Montreal", "3/4/17", "hi", null);
        Adventure temp2 = new Adventure("Los Angeles", "3/5/17", "hi", null);
        Adventure temp3 = new Adventure("Grand Canyon", "3/6/17", "hi", null);
        Adventure temp4 = new Adventure("Niagara Falls", "3/7/17", "hi", null);

        ArrayList<Adventure> tempList = new ArrayList<>();
        tempList.add(temp1);
        tempList.add(temp2);
        tempList.add(temp3);
        tempList.add(temp4);

        pastAdventuresAdapater = new PastAdventuresAdapter(getApplicationContext(), tempList);

         pastAdventureRecyclerView.setAdapter(pastAdventuresAdapater);

        // Firebase nodal setup
//        Pin pin = new Pin("Yo", "This", "Is", "Another", "Test", "Please", "Work");
//        dbRef = FirebaseDatabase.getInstance().getReference();
//        String dbKey = dbRef.child("Pin").push().getKey();
//        dbRef.child("Pin").child(dbKey).setValue(pin);
//
//        ArrayList<Pin> pins = new ArrayList<>();
//        pins.add(pin);
//        Adventure adventure = new Adventure("Hello", "This", "Is", dbRef.);
//        String dbKey2 = dbRef.child("Adventure").push().getKey();
//        dbRef.child("Adventure").child(dbKey2).setValue(adventure);

    }
}
