package com.mdb.wingit.wingit;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Carousel extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rv;
    private CarouselAdapter adapter;
    private GoogleApiClient client;
    private Button go;
    private ArrayList<ActivityList.Activity> activities, three_acts;
    private ArrayList<Place> currentLocations;
    private int indexPlace = 0;
    final String API_KEY = "AIzaSyCSQY63gh8Br0X8ZzasqS67OlQLYO0Yi08";
    final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true);
    private LatLng current;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel);

        adapter = new CarouselAdapter(getApplicationContext(), three_acts);
        activities = new ArrayList<>();
        three_acts = new ArrayList<>();
        currentLocations = new ArrayList<>();
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

                double likelihood = 0;
                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    currentLocations.add(placeLikelihood.getPlace());
                }
                for (PlaceLikelihood placeLikelihood : likelyPlaces){
                    if(placeLikelihood.getLikelihood()>likelihood){
                        likelihood = placeLikelihood.getLikelihood();
                        indexPlace = currentLocations.indexOf(placeLikelihood.getPlace());
                    }
                }
                current = currentLocations.get(indexPlace).getLatLng();
                likelyPlaces.release();
            }
        });
        //if food:
            randomThrees(getNearbyFood());
    }

    public int getRandom(){
        int temp = (int) (Math.random()*activities.size());
        return temp;
    }

    //Adds three activities from list into three_acts.
    public void randomThrees(ArrayList<ActivityList.Activity> list){
        int one, two, three;
        one = getRandom();
        two = getRandom();
        while(two==one){
            two = getRandom();
        }
        three = getRandom();
        while(two==three||one==three){
            three = getRandom();
        }
        three_acts.add(list.get(one));
        three_acts.add(list.get(two));
        three_acts.add(list.get(three));
    }


    public ArrayList<ActivityList.Activity> getNearbyFood(){

        String searchRequest = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+current.latitude+","+current.longitude+"&radius=8000&type=restaurant&opennow=true&key="+API_KEY;
        return sendRequest(searchRequest);
    }
    public ArrayList<ActivityList.Activity> getNearbyActivity(){
        String[] types = {"amusement_park", "aquarium", "art_gallery", "bowling_alley", "clothing_store", "department_store", "zoo", "shopping_mall", "park", "museum", "movie_theater"};
        int random = (int)(Math.random()*types.length);
        String searchRequest = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+current.latitude+","+current.longitude+"&radius=50000&type="+types[random]+"&opennow=true&key="+API_KEY;
        return sendRequest(searchRequest);
    }

    public ArrayList<ActivityList.Activity> sendRequest(String request){
        ArrayList<ActivityList.Activity> result = new ArrayList<>();
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();

        try {
            URL url = new URL(request);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
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
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("results");

            // Extract the Place descriptions from the results
            result = new ArrayList<ActivityList.Activity>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                ActivityList.Activity activity = new ActivityList.Activity();
                activity.setName(predsJsonArray.getJSONObject(i).getString("name"));
                activity.setPlaceID(predsJsonArray.getJSONObject(i).getString("place_id"));
                activity.setRating(predsJsonArray.getJSONObject(i).getString("rating"));
                placePhotosTask(activity,predsJsonArray.getJSONObject(i).getString("place_id"));
                result.add(activity);
            }
        } catch (JSONException e) {
            Log.e("Error", "Error processing JSON results", e);
        }

        return result;
    }

    private void placePhotosTask(final ActivityList.Activity activity, String placeId) {
        // Create a new AsyncTask that displays the bitmap and attribution once loaded.
        new PhotoTask() {
            @Override
            protected void onPreExecute() {
            }

            @Override
            protected void onPostExecute(AttributedPhoto attributedPhoto) {
                if (attributedPhoto != null) {
                    // Photo has been loaded, display it.
                    activity.setBitmap(attributedPhoto.bitmap);
                    //Todo: set attribution somewhere

                }
            }
        }.execute(placeId);
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

    abstract class PhotoTask extends AsyncTask<String, Void, PhotoTask.AttributedPhoto> {

        public PhotoTask() {
        }

        /**
         * Loads the first photo for a place id from the Geo Data API.
         * The place id must be the first (and only) parameter.
         */
        @Override
        protected AttributedPhoto doInBackground(String... params){
            if (params.length != 1) {
                return null;
            }
            final String placeId = params[0];
            AttributedPhoto attributedPhoto = null;

            PlacePhotoMetadataResult result = Places.GeoDataApi
                    .getPlacePhotos(client, placeId).await();

            if (result.getStatus().isSuccess()) {
                PlacePhotoMetadataBuffer photoMetadataBuffer = result.getPhotoMetadata();
                if (photoMetadataBuffer.getCount() > 0 && !isCancelled()) {
                    // Get the first bitmap and its attributions.
                    PlacePhotoMetadata photo = photoMetadataBuffer.get(0);
                    CharSequence attribution = photo.getAttributions();
                    // Load a scaled bitmap for this photo.
                    Bitmap image = photo.getScaledPhoto(client, photo.getMaxWidth(), photo.getMaxHeight()).await()
                            .getBitmap();

                    attributedPhoto = new AttributedPhoto(attribution, image);
                }
                // Release the PlacePhotoMetadataBuffer.
                photoMetadataBuffer.release();
            }
            return attributedPhoto;
        }

        /**
         * Holder for an image and its attribution.
         */
        class AttributedPhoto {

            public final CharSequence attribution;

            public final Bitmap bitmap;

            public AttributedPhoto(CharSequence attribution, Bitmap bitmap) {
                this.attribution = attribution;
                this.bitmap = bitmap;
            }
        }
    }




}
