package com.mdb.wingit.wingit;

import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
        new RequestTask().execute(searchRequest);
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

    //TODO: Clean up this code
    private class RequestTask extends AsyncTask<String, Void, ArrayList<Pin>> {
        protected ArrayList<Pin> doInBackground(String... params) {
            final String request = params[0];
            HttpURLConnection conn = null;
            StringBuilder jsonResults = new StringBuilder();
            ArrayList<Pin> result = new ArrayList<>();
            try {
                URL url = new URL(request);
                conn = (HttpURLConnection) url.openConnection();
                InputStreamReader in = new InputStreamReader(conn.getInputStream());

                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    jsonResults.append(buff, 0, read);
                    //Log.i("jsonResults length", jsonResults.length()+"");
                }
                in.close();
            } catch (IOException e) {
                //Log.e("Error", "Error connecting to Places API", e);
                return result;
            } finally {
                if (conn != null) conn.disconnect();
            }
            try {
                // Create a JSON object hierarchy from the results
                //Log.wtf("length of jsonresults", jsonResults.toString());
                JSONObject jsonObj = new JSONObject(jsonResults.toString());
                JSONArray predsJsonArray = jsonObj.getJSONArray("results");
                //Log.i("size of results", predsJsonArray.length()+"");
                // Extract the Place descriptions from the results
                result = new ArrayList<Pin>(predsJsonArray.length());
                for (int i = 0; i < predsJsonArray.length(); i++) {
                    //TODO: Need to construct Pin object properly
                    Pin activity = new Pin();
                    JSONObject geometry = predsJsonArray.getJSONObject(i).getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");

                    if(predsJsonArray.getJSONObject(i).has("photos")) {
                        JSONArray photos = predsJsonArray.getJSONObject(i).getJSONArray("photos");
                        if(photos.getJSONObject(0).length()!=0){
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
                //Log.e("Error", "Error processing JSON results", e);
            }
            //Log.wtf("wtf", result.size() + "");
            return result;
        }

        protected void onPostExecute(ArrayList<Pin> activityResult) {
            //TODO: Need to do something with the results returned by searchRequest
            //result = activityResult;
            //if(result.size() < 3) {
                Toast.makeText(CarouselActivity.this, "Could not find any activities at this time", Toast.LENGTH_SHORT).show();
                finish();
            //}
            //randomThrees(result);
        }
    }
}
