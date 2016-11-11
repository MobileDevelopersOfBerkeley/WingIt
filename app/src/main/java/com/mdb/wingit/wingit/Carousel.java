package com.mdb.wingit.wingit;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Carousel extends AppCompatActivity {

    private RecyclerView rv;
    private CarouselAdapter adapter;
    private Button go;
    private TextView activityName;
    private ImageView activityPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel);
    }
}
