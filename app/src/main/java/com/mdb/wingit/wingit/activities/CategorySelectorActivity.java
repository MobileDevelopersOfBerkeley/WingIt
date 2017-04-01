package com.mdb.wingit.wingit.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.mdb.wingit.wingit.R;

public class CategorySelectorActivity extends AppCompatActivity {

    private TextView title;
    private ImageView food, activity, arrow;
    private Button logout;
    private GoogleApiClient client;
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 1;
    static LatLng currentLocation; // current location in lat and long
    static String currentName = "";
    private Intent carousel;
    TextView tempView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selector);

        //Connect to Places API
        client = new GoogleApiClient.Builder(this).addApi(Places.GEO_DATA_API).addApi(Places.PLACE_DETECTION_API).build();
        client.connect();

        // Set up UI elements
        String pageTitle = getIntent().getStringExtra("title");
        title = (TextView) findViewById(R.id.title);
        title.setText(pageTitle);
        food = (ImageView) findViewById(R.id.foodImage);
        activity = (ImageView) findViewById(R.id.activityImage);
        arrow = (ImageView) findViewById(R.id.arrow);

        logout = (Button) findViewById(R.id.temp_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent logoutIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(logoutIntent);
            }
        });
        tempView = (TextView) findViewById(R.id.temp_location);
        getCurrentLocations();

        carousel = new Intent(getApplicationContext(), CarouselActivity.class);

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carousel.putExtra("isFood", true);
                carousel.putExtra("location", currentLocation);
                startActivity(carousel);
            }
        });

        activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carousel.putExtra("isFood", false);
                carousel.putExtra("location", currentLocation);
                startActivity(carousel);
            }
        });

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pastAdventures = new Intent(getApplicationContext(), PastAdventuresActivity.class);
                startActivity(pastAdventures);
            }
        });
    }

    /** Get list of likely places for user's current location from Places API */
    private void getCurrentLocations() {
        //Check permissions to access user's location
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_ACCESS_FINE_LOCATION);
        }

        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace(client, null);
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {

                double likelihood = 0;
                PlaceLikelihood p = null;

                for (PlaceLikelihood placeLikelihood : likelyPlaces) {

                    if (placeLikelihood.getLikelihood() > likelihood) {
                        likelihood = placeLikelihood.getLikelihood();
                        p = placeLikelihood;
                    }
                }

                if (p != null) {
                    currentLocation = p.getPlace().getLatLng();
                    currentName = p.getPlace().getName().toString();
                    tempView.setText("Location: " + currentName);
                }

                likelyPlaces.release();
            }
        });
    }

}
