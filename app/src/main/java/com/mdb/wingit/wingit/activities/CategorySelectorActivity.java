package com.mdb.wingit.wingit.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mdb.wingit.wingit.R;
import com.mdb.wingit.wingit.modelClasses.Adventure;
import com.mdb.wingit.wingit.modelClasses.User;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Retrieves user's current location
 * Allows user to choose between Food and Activities as their category
 */

public class CategorySelectorActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleApiClient client;
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 1;
    private LatLng currentLocation;
    private String currentName = "";
    private String adventureKey = "";
    SupportMapFragment mapFragment;
    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    private User currUser;
    private DatabaseReference userRef;
    private DrawerLayout drawerLayout;
    private boolean continueAdventure;
    private TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selector);

        //Connect to Places API
        client = new GoogleApiClient.Builder(this).addApi(Places.GEO_DATA_API).addApi(Places.PLACE_DETECTION_API).build();
        client.connect();

        //Get user's current location
        getPermissions();

        // Set up UI elements
        TextView title = (TextView) findViewById(R.id.title);
        ImageView food = (ImageView) findViewById(R.id.foodImage);
        ImageView activity = (ImageView) findViewById(R.id.activityImage);
        ImageView arrow = (ImageView) findViewById(R.id.arroworange);
        ImageView hamburger = (ImageView) findViewById(R.id.hamburger);
        TextView pastAdventures = (TextView) findViewById(R.id.past_adventures_text_view);
        TextView logoutView = (TextView) findViewById(R.id.logout_text_view);
        TextView activityText = (TextView) findViewById(R.id.activityText);
        TextView foodText = (TextView) findViewById(R.id.foodText);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        name = (TextView) findViewById(R.id.name_text_view);

        populateNameView();

        food.setOnClickListener(this);
        activity.setOnClickListener(this);
        arrow.setOnClickListener(this);
        hamburger.setOnClickListener(this);
        pastAdventures.setOnClickListener(this);
        logoutView.setOnClickListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        //Get information from intent
        Bundle intentExtras = getIntent().getExtras();
        if (intentExtras != null) {
            this.continueAdventure = true;
            adventureKey = intentExtras.getString("adventureKey", "");
            title.setText("Continue Your Adventure");
        } else {
            this.continueAdventure = false;
            title.setText("Start Your Adventure");
            arrow.setVisibility(View.GONE);
        }

        Typeface med = Typeface.createFromAsset(getAssets(),"fonts/Quicksand-Medium.ttf");
        Typeface reg = Typeface.createFromAsset(getAssets(),"fonts/Quicksand-Regular.ttf");
        title.setTypeface(med);
        activityText.setTypeface(reg);
        foodText.setTypeface(reg);
        pastAdventures.setTypeface(reg);
        logoutView.setTypeface(reg);
        name.setTypeface(med);

    }

    public void populateNameView() {

        userRef = dbRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currUser = dataSnapshot.getValue(User.class);
                name.setText(currUser.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Database Error", databaseError.toString());
                Toast.makeText(CategorySelectorActivity.this, "Failed to get current user", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.foodImage:
                startCarouselActivity(true);
                break;
            case R.id.activityImage:
                startCarouselActivity(false);
                break;
            case R.id.arroworange:
                Intent timeline = new Intent(getApplicationContext(), AdventureTimelineActivity.class);
                timeline.putExtra("adventureKey", adventureKey);
                startActivity(timeline);
                break;
            case R.id.hamburger:
                drawerLayout.openDrawer(Gravity.START);
                break;
            case R.id.past_adventures_text_view:
                startActivity(new Intent(getApplicationContext(), PastAdventuresActivity.class));
                break;
            case R.id.logout_text_view:
                FirebaseAuth.getInstance().signOut();
                Intent logoutIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(logoutIntent);
                break;
        }
    }

    /** Get list of likely places for user's current location from Places API */
    private void getPermissions() {
        //Check permissions to access user's location
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_ACCESS_FINE_LOCATION);

        } else {
            try {
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
                            mapFragment.getMapAsync(CategorySelectorActivity.this);
                        }

                        likelyPlaces.release();
                    }
                });
            } catch (SecurityException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode) {
            case MY_PERMISSION_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
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
                                    mapFragment.getMapAsync(CategorySelectorActivity.this);
                                }

                                likelyPlaces.release();
                            }
                        });
                    } catch (SecurityException e) {
                        System.out.println(e.getMessage());
                    }
                }
        }
    }

    /** Open Carousel Activity with choices based on category user selected */
    private void startCarouselActivity(boolean isFood) {
        Intent carousel = new Intent(getApplicationContext(), CarouselActivity.class);
        if (currentLocation == null) {
            notifyNoLocation();
        } else {
            carousel.putExtra("isFood", isFood);
            carousel.putExtra("location", currentLocation);
            if (adventureKey.equals("")) {
                adventureKey = startNewAdventure();
            }
            carousel.putExtra("adventureKey", adventureKey);
            startActivity(carousel);
        }
    }

    /** Notify user that app is unable to get their current location */
    private void notifyNoLocation() {
        Toast.makeText(this, "Unable to get your current location", Toast.LENGTH_SHORT).show();
    }

    /** Generate adventure key in database for user's first adventure */
    private String startNewAdventure() {
        Adventure adventure = new Adventure(currentName, getDate(), getImage());
        DatabaseReference adventureRef = dbRef.child("Adventures");
        String adventureKey = adventureRef.push().getKey();
        adventureRef.child(adventureKey).setValue(adventure);
        updateCurrUser(adventureKey);
        return adventureKey;
    }

    /** Add adventure key to Users node in database */
    private void updateCurrUser(final String adventureKey) {
        userRef = dbRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currUser = dataSnapshot.getValue(User.class);

                if (currUser != null) {
                    currUser.addAdventureKey(adventureKey);
                    userRef.setValue(currUser);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Database Error", databaseError.toString());
                Toast.makeText(CategorySelectorActivity.this, "Failed to get current user", Toast.LENGTH_SHORT).show();
            }
        });


    }

    /** Get current date when creating new Adventure */
    private String getDate() {
        Calendar cal = Calendar.getInstance();
        Date currentDate = cal.getTime();
        DateFormat formatter = new SimpleDateFormat("MMM d, yyyy");
        formatter.setTimeZone(cal.getTimeZone());
        return formatter.format(currentDate);
    }

    //TODO: Retrieve image for new adventure
    /** Get image for new Adventure */
    private String getImage() {
        return "";
    }

    /** Center background map on current location */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        float zoomLevel = 16;
        if (currentLocation != null) {
            if (continueAdventure) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, zoomLevel));
            } else {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, zoomLevel));
            }
        }
    }

}
