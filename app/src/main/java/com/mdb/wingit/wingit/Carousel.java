package com.mdb.wingit.wingit;

import android.content.Intent;
import android.media.Image;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Carousel extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rv;
    private CarouselAdapter adapter;
    private Button go;
    private ArrayList<Object> activities;
    final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true);;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel);

        adapter = new CarouselAdapter(getApplicationContext(), activities);
        activities = new ArrayList<>();
        go = (Button) findViewById(R.id.go);
        rv = (RecyclerView) findViewById(R.id.rv);

        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);
        rv.addOnScrollListener(new CenterScrollListener());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.go:
                Intent intent = new Intent(Carousel.this, Details.class);
                startActivity(intent);
                break;
        }
    }


}
