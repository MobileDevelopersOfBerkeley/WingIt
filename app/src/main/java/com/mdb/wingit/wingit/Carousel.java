package com.mdb.wingit.wingit;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.GeoDataApi;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Carousel extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rv;
    private CarouselAdapter adapter;
    private GoogleApiClient client;
    private Button go;
    private ArrayList<ActivityList.Activity> activities;
    private ArrayList<Place> currentLocations;
    final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel);

        adapter = new CarouselAdapter(getApplicationContext(), activities);
        activities = new ArrayList<>();
        go = (Button) findViewById(R.id.go);
        rv = (RecyclerView) findViewById(R.id.carouselrv);

        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);
        rv.addOnScrollListener(new CenterScrollListener());

        client = new GoogleApiClient.Builder(this).addApi(Places.GEO_DATA_API).build();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                .getCurrentPlace(client, null);
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    currentLocations.add(placeLikelihood.getPlace());
                }
                likelyPlaces.release();
            }
        });

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.go:
                Intent intent = new Intent(Carousel.this, DetailScreen.class);
                startActivity(intent);
                break;
        }
    }


}
