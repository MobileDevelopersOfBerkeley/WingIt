package com.mdb.wingit.wingit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class PastAdventuresActivity extends AppCompatActivity {

    public PastAdventuresAdapter pastAdventuresAdapater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_adventures);

        RecyclerView pastAdventureRecyclerView = (RecyclerView) findViewById(R.id.past_adventures_recycler_view);
        pastAdventureRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // TODO: Load past activities from Firebase into an ArrayList
        Adventure temp1 = new Adventure("Montreal", "3/4/17");
        Adventure temp2 = new Adventure("Los Angeles", "3/5/17");
        Adventure temp3 = new Adventure("Grand Canyon", "3/6/17");
        Adventure temp4 = new Adventure("Niagara Falls", "3/7/17");

        ArrayList<Adventure> tempList = new ArrayList<>();
        tempList.add(temp1);
        tempList.add(temp2);
        tempList.add(temp3);
        tempList.add(temp4);
        // TODO: Bind this ArrayList to pastAdventuresAdapter
        pastAdventuresAdapater = new PastAdventuresAdapter(getApplicationContext(), tempList);

        // TODO: Uncomment this last line to bind pastAdventuresAdapter to the recycler view
         pastAdventureRecyclerView.setAdapter(pastAdventuresAdapater);

    }
}
