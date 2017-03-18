package com.mdb.wingit.wingit;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private GoogleApiClient client;
    private int MY_PERMISSION_ACCESS_FINE_LOCATION = 1;
    static LatLng current; // current location in lat and long
    static String currentName = "";
    static int indexPlace = 0;
    private static ArrayList<Adventure> adventures;
    static ArrayList<String> otherFive = new ArrayList<>();
    static ArrayList<Place> currentLocations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Connect to Places API
        client = new GoogleApiClient.Builder(this).addApi(Places.GEO_DATA_API).addApi(Places.PLACE_DETECTION_API).build();
        client.connect();

        adventures = new ArrayList<Adventure>();
        getCurrentLocations();

        mAuth = FirebaseAuth.getInstance();
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

        currentLocations = new ArrayList<>();
        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace(client, null);
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                double likelihood = 0;
                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    Log.i("Place", placeLikelihood.getPlace().getName().toString());
                    currentLocations.add(placeLikelihood.getPlace());
                    Log.i("Likelihood", String.format("Place '%s' has likelihood: %g",
                            placeLikelihood.getPlace().getName(),
                            placeLikelihood.getLikelihood()));
                }
                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    if (placeLikelihood.getLikelihood() > likelihood) {
                        likelihood = placeLikelihood.getLikelihood();
                        indexPlace = currentLocations.indexOf(placeLikelihood.getPlace());
                    }
                }
                if (currentLocations.size() != 0) {
                    for (int i = 0; i < currentLocations.size(); i++) {
                        otherFive.add(currentLocations.get(i).getName().toString());
                    }
                    current = currentLocations.get(indexPlace).getLatLng();
                    currentName = currentLocations.get(indexPlace).getName().toString();
                }
                likelyPlaces.release();
            }
        });
    }


}
