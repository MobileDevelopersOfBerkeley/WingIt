package com.mdb.wingit.wingit.activities;

import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mdb.wingit.wingit.R;
import com.mdb.wingit.wingit.modelClasses.Pin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * Displays 3 choices of pins nearby the user according to the previously specified category
 */

public class CarouselActivity extends AppCompatActivity implements OnMapReadyCallback {

    private CarouselAdapter adapter;
    public static View backgroundtint;
    public static FragmentManager fragmentManager;

    public static final String API_KEY_UNRESTRICTED = "AIzaSyDrzZ5f9o0ZAZbeCStRN87tAqKaugi-0iI";
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 1;
    private LatLng currentLocation;
    private ArrayList<Pin> pinList = new ArrayList<>();
    private boolean isFoodCategory;
    private final String[] types = {"amusement_park", "aquarium", "art_gallery", "bowling_alley",
            "clothing_store", "department_store", "zoo", "shopping_mall", "park",
            "museum", "movie_theater"};
    private ArrayList<String> typesList = new ArrayList<>();
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel);
        fragmentManager = getSupportFragmentManager();

        //Connect to Places API
        GoogleApiClient client = new GoogleApiClient.Builder(this).addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API).build();
        client.connect();

        //Check permissions to access user's location
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_ACCESS_FINE_LOCATION);
        }

        //Get information from intent
        Bundle intentExtras = getIntent().getExtras();
        isFoodCategory = intentExtras.getBoolean("isFood");
        currentLocation = (LatLng) intentExtras.get("location");
        String adventureKey = intentExtras.getString("adventureKey");

        //Set up map background
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Set up Carousel Recycler View
        this.constraintLayout = (ConstraintLayout) findViewById(R.id.activity_carousel);
        RecyclerView rv = (RecyclerView) findViewById(R.id.carouselrv);
        adapter = new CarouselAdapter(CarouselActivity.this, pinList, adventureKey, currentLocation);
        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.VERTICAL, true);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);
        rv.addOnScrollListener(new CenterScrollListener());

        //Compose and send searchRequest based on category user selected
        typesList.addAll(Arrays.asList(types));
        String[] searchRequests;
        if (isFoodCategory) {
            searchRequests = new String[]{createRequestURL()};
        } else {
            searchRequests = new String[3];
            for (int i = 0; i < 3; i++) {
                searchRequests[i] = createRequestURL();
            }
        }
        for (String request : searchRequests) {
            new RequestTask().execute(request);
        }

        this.backgroundtint = findViewById(R.id.backgroundtint);

    }

    @Override
    public void onBackPressed() {
        Snackbar snackbar = Snackbar.make(constraintLayout, "Please continue your adventure", Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    /** Create request URL based on search radius and type associated with selected category */
    private String createRequestURL() {
        String initMapsURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
        String locationURL = "location=" + currentLocation.latitude + "," + currentLocation.longitude;
        String endMapsURL = "&opennow=true&key=" + API_KEY_UNRESTRICTED;

        int radius;
        String type;
        if (isFoodCategory) {
            radius = 8000;
            type = "restaurant";
        } else {
            //TODO: Make search request for activities with 3 different types
            radius = 50000;
            type = getRandomType();
        }

        String radiusURL = "&radius=" + radius;
        String typesURL = "&type=" + type;

        return initMapsURL + locationURL + radiusURL + typesURL + endMapsURL;
    }

    /** Get random element from an array */
    private String getRandomType() {
        int random = (int)(Math.random()*typesList.size());
        return typesList.remove(random);
    }

    /** Pick 3 random pins to populate carousel from results of search request */
    private void pickNumRandom(int num, ArrayList<Pin> list) {
        Collections.shuffle(list);
        for (int i = 0; i < num; i++) {
            pinList.add(list.get(i));
        }
    }

    //TODO: Make more efficient by creating Pin objects after picking random elements from jsonArray
    /** AsyncTask that processes search request based on category and creates ArrayList of Pins */
    private class RequestTask extends AsyncTask<String, Void, ArrayList<Pin>> {
        @Override
        protected ArrayList<Pin> doInBackground(String... params) {
            HttpURLConnection conn = null;
            ArrayList<Pin> result = new ArrayList<>();
            try {
                URL url = new URL(params[0]);
                conn = (HttpURLConnection) url.openConnection();
                InputStreamReader in = new InputStreamReader(conn.getInputStream());

                int read;
                char[] buff = new char[1024];
                StringBuilder jsonResults = new StringBuilder();
                while ((read = in.read(buff)) != -1) {
                    jsonResults.append(buff, 0, read);
                }
                in.close();

                //Create a JSON object hierarchy from the results of the search request
                JSONObject jsonObj = new JSONObject(jsonResults.toString());
                JSONArray jsonArray = jsonObj.getJSONArray("results");

                //Create an array of Pin objects from the JSON array
                String time = getCurrentTime();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject currObj = jsonArray.getJSONObject(i);
                    Pin pin = composePin(currObj, time);
                    result.add(pin);
                }
            } catch (IOException e) {
                //Log.e("Error", "Error connecting to Places API", e);
            } catch (JSONException e) {
                //Log.e("Error", "Error processing JSON results", e);
            } finally {
                if (conn != null) conn.disconnect();
            }
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<Pin> taskResult) {
            int numResults = 1;
            if (isFoodCategory) {
                numResults = 3;
            }
            if (taskResult.size() >= numResults) {
                pickNumRandom(numResults, taskResult);
                adapter.notifyDataSetChanged();
            }
        }
    }

    /** Create Pin object based on JSON Object that is currently being processed */
    private Pin composePin(JSONObject jsonObj, String time) throws JSONException {
        //Name, Place ID, and Rating
        String[] pinFields = new String[]{"name", "place_ID", "rating", "vicinity"};
        String[] pinDetails = new String[4];
        for (int i = 0; i < 4; i++) {
            if (jsonObj.has(pinFields[i])) {
                pinDetails[i] = jsonObj.getString(pinFields[i]);
            } else {
                pinDetails[i] = "";
            }
        }

        //Latitude and Longitude
        JSONObject location = jsonObj.getJSONObject("geometry").getJSONObject("location");
        String latitude = location.getString("lat");
        String longitude = location.getString("lng");

        //Image URL
        String imgURL = "";
        if (jsonObj.has("photos")) {
            JSONArray photos = jsonObj.getJSONArray("photos");
            if (photos.getJSONObject(0).length() != 0) {
                JSONObject photoObject = photos.getJSONObject(0);
                imgURL = photoObject.getString("photo_reference");
            }
        }

        //Create Pin object
        return new Pin(pinDetails[0], pinDetails[1], latitude, longitude, pinDetails[2], time, imgURL, pinDetails[3]);
    }

    /** Get current time which stays constant for all Pin objects created */
    private String getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        Date currentTime = cal.getTime();
        DateFormat formatter = new SimpleDateFormat("HH:mm a");
        formatter.setTimeZone(cal.getTimeZone());
        return formatter.format(currentTime);
    }

    /** Center background map on current location */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        float zoomLevel = 16;
        if (currentLocation != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, zoomLevel));
        }
    }
}
