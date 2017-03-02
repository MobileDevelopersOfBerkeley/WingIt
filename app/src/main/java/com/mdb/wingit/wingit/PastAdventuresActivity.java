package com.mdb.wingit.wingit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class PastAdventuresActivity extends AppCompatActivity {

    public PastAdventuresAdapter pastAdventuresAdapater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_adventures);

        RecyclerView pastAdventureRecyclerView = (RecyclerView) findViewById(R.id.past_adventures_recycler_view);
        pastAdventureRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // TODO: Load past activities from Firebase into an ArrayList
        // TODO: Bind this ArrayList to pastAdventuresAdapter

        // TODO: Uncomment this last line to bind pastAdventuresAdapter to the recycler view
        // pastAdventureRecyclerView.setAdapter(pastAdventuresAdapater);

    }
}
