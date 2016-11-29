package com.mdb.wingit.wingit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.mdb.wingit.wingit.MainActivity.currentLocations;
import static com.mdb.wingit.wingit.MainActivity.indexPlace;

public class Carousel extends AppCompatActivity implements RecyclerViewClickListener {

    private RecyclerView rv;
    private CarouselAdapter adapter;
    private ArrayList<ActivityList.Activity> result, three_acts;
    private ActivityList.Activity final_pick;
    public static final String API_KEY = "AIzaSyAEBSZ7TmXI6QHRx4zrQwMP5ZR7me3pThI";
    public static final String API_KEY_NONRESTRICTED = "AIzaSyDrzZ5f9o0ZAZbeCStRN87tAqKaugi-0iI";
    final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true);
    private LatLng current;
    private GoogleApiClient client;
    private int selectedPos;
    private RecyclerViewClickListener itemListener;
    private final int MY_PERMISSION_ACCESS_FINE_LOCATION = 1;
    private boolean checkedPermission = false;
    private boolean isHighlighted = false; // if the current choice is highlighted
    private ConstraintLayout choice;

    private AdventureList adventures = new AdventureList();
    private DatabaseReference dbref;

    private String adventureKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel);
        choice = (ConstraintLayout) findViewById(R.id.choice);
        dbref = FirebaseDatabase.getInstance().getReference();
        adventureKey = (String) getIntent().getExtras().get("adventureKey");

        itemListener = new RecyclerViewClickListener() {
            @Override
            public void recyclerViewListClicked(View v, int position) {
                selectedPos = position;
                if (isHighlighted) {
                    choice.setBackgroundResource(R.drawable.carouselborderoff);
                    isHighlighted = false;
                } else {
                    choice.setBackgroundResource(R.drawable.carouselborderon);
                    isHighlighted = true;
                }
            }
        };

        client = new GoogleApiClient.Builder(this).addApi(Places.GEO_DATA_API).addApi(Places.PLACE_DETECTION_API).build();
        result = new ArrayList<>();
        final_pick = new ActivityList.Activity();
        three_acts = new ArrayList<>();
        rv = (RecyclerView) findViewById(R.id.carouselrv);
        current = (LatLng) getIntent().getExtras().get("current");
//        rv.setLayoutManager(layoutManager);
//        rv.setHasFixedSize(true);
//        rv.setAdapter(adapter);
//        rv.addOnScrollListener(new CenterScrollListener());

        Log.i("Layout", "before declaring adapter");
        adapter = new CarouselAdapter(Carousel.this, three_acts, itemListener);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                isHighlighted = false;

            }
        });
        rv.addOnScrollListener(new CenterScrollListener());

        client = new GoogleApiClient.Builder(this).addApi(Places.GEO_DATA_API).addApi(Places.PLACE_DETECTION_API).build();
        client.connect();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    MY_PERMISSION_ACCESS_FINE_LOCATION);
            Log.i("Permissions???", "rip");
            checkedPermission = true;
            return;
        }

        //changed from !checkedPermission
        Log.i("Does", checkedPermission+"");
        if (!checkedPermission)  {

            getCurrentPlaces();
        }
//        Intent intent = getIntent();
//        boolean activityType = intent.getBooleanExtra("food", true);
//        if (activityType) {
//            randomThrees(getNearbyFood());
//            Log.i("random thress", "function called");
//        } else {
//            randomThrees(getNearbyActivity());
//        }

    }

    public int getRandom(int size){
        int temp = (int) (Math.random()*size);
        return temp;
    }

    //Adds three activities from list into three_acts.
    public void randomThrees(ArrayList<ActivityList.Activity> list){
        int one, two, three;
        int listSize = list.size();
        one = getRandom(listSize);
        two = getRandom(listSize);
        while(two==one){
            two = getRandom(listSize);
        }
        three = getRandom(listSize);
        while(two==three||one==three){
            three = getRandom(listSize);
        }
        Log.i("Three Acts", list.get(one).getName());
        Log.i("Three Acts", list.get(two).getName());
        Log.i("Three Acts", list.get(three).getName());
        three_acts.add(list.get(one));
        three_acts.add(list.get(two));
        three_acts.add(list.get(three));
        adapter.notifyDataSetChanged();
    }


    public ArrayList<ActivityList.Activity> getNearbyFood(){
        Log.i("food log", "food send request");
        String searchRequest = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+current.latitude+","+current.longitude+"&radius=8000&type=restaurant&opennow=true&key="+API_KEY_NONRESTRICTED;

        sendRequestTask(searchRequest);
        Log.i("food log", "food send request done");
        Log.i("search", searchRequest);
        return result;

    }
    public ArrayList<ActivityList.Activity> getNearbyActivity(){
        String[] types = {"amusement_park", "aquarium", "art_gallery", "bowling_alley", "clothing_store", "department_store", "zoo", "shopping_mall", "park", "museum", "movie_theater"};
        int random = (int)(Math.random()*types.length);
        String searchRequest = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+current.latitude+","+current.longitude+"&radius=50000&type="+types[random]+"&opennow=true&key="+API_KEY_NONRESTRICTED;
        Log.i("activity type", types[random]+"");

        sendRequestTask(searchRequest);
        return result;

    }


    private void sendRequestTask(final String request) {
        new RequestTask() {
            @Override
            protected void onPreExecute() {

            }
            @Override
            protected void onPostExecute(ArrayList<ActivityList.Activity> activityResult) {
                result = activityResult;
                if(result.size() == 0) {
                    getNearbyActivity();
                }
                randomThrees(result);
            }
        }.execute(request);
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        this.selectedPos = position;
    }


    abstract class RequestTask extends AsyncTask<String, Void, ArrayList<ActivityList.Activity>> {
        public RequestTask() {
        }
        protected ArrayList<ActivityList.Activity> doInBackground(String... params) {
            if (params.length != 1) {
                return null;
            }
            final String request = params[0];
            HttpURLConnection conn = null;
            StringBuilder jsonResults = new StringBuilder();
            Log.i("async task","is working?");
            ArrayList<ActivityList.Activity> result = new ArrayList<>();
            try {
                URL url = new URL(request);
                conn = (HttpURLConnection) url.openConnection();
                InputStreamReader in = new InputStreamReader(conn.getInputStream());

                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    jsonResults.append(buff, 0, read);
                    Log.i("jsonResults length", jsonResults.length()+"");
                }
            } catch (IOException e) {
                Log.e("Error", "Error connecting to Places API", e);
                return result;
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            try {
                // Create a JSON object hierarchy from the results
                Log.wtf("length of jsonresults", jsonResults.toString());
                JSONObject jsonObj = new JSONObject(jsonResults.toString());
                JSONArray predsJsonArray = jsonObj.getJSONArray("results");
                Log.i("size of results", predsJsonArray.length()+"");
                // Extract the Place descriptions from the results
                result = new ArrayList<ActivityList.Activity>(predsJsonArray.length());
                for (int i = 0; i < predsJsonArray.length(); i++) {
                    ActivityList.Activity activity = new ActivityList.Activity();
                    JSONObject geometry = predsJsonArray.getJSONObject(i).getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");

                    if(predsJsonArray.getJSONObject(i).has("photos")) {
                        Log.i("getting", "photos");
                        JSONArray photos = predsJsonArray.getJSONObject(i).getJSONArray("photos");
                        if(photos.getJSONObject(0).length()!=0){
                            Log.i("why","pls");
                            JSONObject photoObject = photos.getJSONObject(0);
                            activity.setPhotoRef(photoObject.getString("photo_reference"));
                        }

                    }
                    activity.setName(predsJsonArray.getJSONObject(i).getString("name"));
                    activity.setPlaceID(predsJsonArray.getJSONObject(i).getString("place_id"));
                    activity.setLat(location.getString("lat"));
                    activity.setLon(location.getString("lng"));
                    result.add(activity);
                }
            } catch (JSONException e) {
                Log.e("Error", "Error processing JSON results", e);
            }
            Log.wtf("wtf", result.size() + "");
            return result;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.i("Layout", "before getCurrentPlaces()");
                    getCurrentPlaces();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void getCurrentPlaces() {
        try {
            PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                    .getCurrentPlace(client, null);
             result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                @Override
                public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                    Log.i("ERROR HELP", "Code: " + likelyPlaces.getStatus().getStatusCode());

                    Log.i("ERROR HELP", "Message: " + likelyPlaces.getStatus().getStatusMessage());
                    double likelihood = 0;
                    for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                        currentLocations.add(placeLikelihood.getPlace());
                        Log.i("Error", String.format("Place '%s' has likelihood: %g",
                                placeLikelihood.getPlace().getName(),
                                placeLikelihood.getLikelihood()));
                    }
                    for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                        if (placeLikelihood.getLikelihood() > likelihood) {
                            likelihood = placeLikelihood.getLikelihood();
                            indexPlace = currentLocations.indexOf(placeLikelihood.getPlace());
                        }
                    }
                    current = currentLocations.get(indexPlace).getLatLng();
                    likelyPlaces.release();
                    Log.i("API callback", "successful");
                    Intent intent = getIntent();
                    boolean activityType = intent.getBooleanExtra("food", true);
                    if (activityType) {
                        //randomThrees(getNearbyFood());
                        getNearbyFood();
                        Log.i("random threes", "function called");
                    } else {
                        // randomThrees(getNearbyActivity());
                        getNearbyActivity();
                    }

                }
            });
        }
        catch (SecurityException e) {

            Log.i("Security Exception", "Allergic");
        }
    }
}
