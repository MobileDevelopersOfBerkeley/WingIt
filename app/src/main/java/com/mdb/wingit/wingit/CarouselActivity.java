package com.mdb.wingit.wingit;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class CarouselActivity extends AppCompatActivity {

    private RecyclerView rv;
    private CarouselAdapter adapter;

    private GoogleApiClient client;
    public static final String API_KEY_UNRESTRICTED = "AIzaSyDrzZ5f9o0ZAZbeCStRN87tAqKaugi-0iI";
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 1;

    private LatLng currentLocation;
    private ArrayList<Pin> pinList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel);

        //Connect to Places API
        client = new GoogleApiClient.Builder(this).addApi(Places.GEO_DATA_API).addApi(Places.PLACE_DETECTION_API).build();
        client.connect();

        //Check permissions to access user's location
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_ACCESS_FINE_LOCATION);
        }

        //Set up Carousel Recycler View
        rv = (RecyclerView) findViewById(R.id.carouselrv);
        adapter = new CarouselAdapter(CarouselActivity.this, pinList);
        CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.VERTICAL, true);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);
        rv.addOnScrollListener(new CenterScrollListener());

        //Get information from intent
        Bundle intentExtras = getIntent().getExtras();
        boolean isFoodCategory = intentExtras.getBoolean("isFood");
        currentLocation = (LatLng) intentExtras.get("location");

        //Compose and send searchRequest based on category user selected
        String searchRequest = createRequestURL(isFoodCategory);
        sendRequestTask(searchRequest);
    }

    /** Create request URL based on search radius and type associated with selected category */
    private String createRequestURL(boolean isFood) {
        String initMapsURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
        String locationURL = "location=" + currentLocation.latitude + "," + currentLocation.longitude;
        String endMapsURL = "&opennow=true&key=" + API_KEY_UNRESTRICTED;

        int radius;
        String type;
        if (isFood) {
            radius = 8000;
            type = "restaurant";
        } else {
            radius = 50000;
            String[] types = {"amusement_park", "aquarium", "art_gallery", "bowling_alley", "clothing_store", "department_store", "zoo", "shopping_mall", "park", "museum", "movie_theater"};
            int random = (int)(Math.random()*types.length);
            type = types[random];
        }

        String radiusURL = "&radius=" + radius;
        String typesURL = "&type=" + type;

        return initMapsURL + locationURL + radiusURL + typesURL + endMapsURL;
    }

    private void sendRequestTask(String request) {

    }
}
