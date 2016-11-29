package com.mdb.wingit.wingit;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.mdb.wingit.wingit.MainActivity.current;

public class DetailScreen extends AppCompatActivity implements View.OnClickListener{

    Toolbar toolbar;
    CollapsingToolbarLayout toolbarLayout;
    ImageView imageView;
    TextView textView;
    FloatingActionButton fab;
    DatabaseReference dbRef;
    String place_id;
    TextView r1, r2, r3, r4, r5;
    ArrayList<String> result = new ArrayList<String>();
    ArrayList<String> reviews = new ArrayList<>();
    ArrayList<TextView> reviewBoxes;
    Button nextButton;
    Button endButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_screen);

        dbRef = FirebaseDatabase.getInstance().getReference();

        place_id = getIntent().getExtras().getString("place_id");
        getReviews();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle(getIntent().getStringExtra("name"));

        imageView = (ImageView) findViewById(R.id.imageView);
        String photoRef = getIntent().getStringExtra("photoRef");
        Glide.with(getApplicationContext()).load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+photoRef+"&key="+Carousel.API_KEY_NONRESTRICTED).into(imageView);
        r1 = (TextView) findViewById(R.id.r1);
        r2 = (TextView) findViewById(R.id.r2);
        r3 = (TextView) findViewById(R.id.r3);
        r4 = (TextView) findViewById(R.id.r4);
        r5 = (TextView) findViewById(R.id.r5);
        reviewBoxes = new ArrayList<TextView>();
        reviewBoxes.add(r1);
        reviewBoxes.add(r2);
        reviewBoxes.add(r3);
        reviewBoxes.add(r4);
        reviewBoxes.add(r5);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String coordinates = getIntent().getStringExtra("coordinates");
                Intent mapsIntent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr=" + coordinates));
                startActivity(mapsIntent);
            }
        });

        nextButton = (Button) findViewById(R.id.nextActivityButton);
        endButton = (Button) findViewById(R.id.endTripButton);

        nextButton.setOnClickListener(this);
        endButton.setOnClickListener(this);
    }

    public void getReviews(){

        String searchRequest = "https://maps.googleapis.com/maps/api/place/details/json?placeid="+place_id+"&key="+Carousel.API_KEY_NONRESTRICTED;
        new DetailTask() {
            @Override
            protected void onPreExecute() {

            }
            @Override
            protected void onPostExecute(ArrayList<String> activityResult) {
                reviews = activityResult;
                if(reviews.size()<5){
                    setFirstReview(5-reviews.size());
                }
                setReviews();
            }
        }.execute(searchRequest);
    }

    public void setFirstReview(int num){
        for(int i=num; i<5; i++){
            reviewBoxes.get(i).setText("No review yet.");
        }
    }

    public void setReviews(){
        for(int i=0; i<5; i++){
            reviewBoxes.get(i).setText("\"" + reviews.get(i) + "\"");
        }
    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.fab:
                Intent activityIntent = getIntent();
                //TODO: pass coordinate value of destination through intent
                //String coordinates = activityIntent.getStringExtra("coordinates");
                String coordinates = "20.5666,45.345";
                Intent mapsIntent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr=" + coordinates));
                startActivity(mapsIntent);
                break;

            case R.id.nextActivityButton:
                //TODO: insert dialog to choose between food and activity
                Dialog dialog = new Dialog(DetailScreen.this);
                dialog.setContentView(R.layout.dialog_layout);
                dialog.setTitle("Continue your adventure");
                CardView activity = (CardView) dialog.findViewById(R.id.activity);
                activity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MainActivity.StartOptions t = new MainActivity.StartOptions();
                        t.createAdventure();

                        Intent activityIntent = new Intent(getApplicationContext(), Carousel.class);
                        activityIntent.putExtra("food", false);
                        activityIntent.putExtra("current",current);
                        activityIntent.putExtra("adventureKey", MainActivity.StartOptions.adventureKey);
                        startActivity(activityIntent);
                    }
                });

                CardView food = (CardView) dialog.findViewById(R.id.food);
                food.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MainActivity.StartOptions.createAdventure();

                        Intent foodIntent = new Intent(getApplicationContext(), Carousel.class);
                        foodIntent.putExtra("food", true);
                        foodIntent.putExtra("current", current);
                        foodIntent.putExtra("adventureKey", MainActivity.StartOptions.adventureKey);
                        startActivity(foodIntent);
                    }
                });

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.show();
                dialog.getWindow().setAttributes(lp);

                break;
            case R.id.endTripButton:
                //TODO: save trip in log
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    abstract class DetailTask extends AsyncTask<String, Void, ArrayList<String>>{
        public DetailTask(){}
        @Override
        protected ArrayList<String> doInBackground(String... params){
            if(params.length!=1){
                return null;
            }
            final String request = params[0];
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
                    Log.i("jsonResults length", jsonResults.length()+"");
                }
            } catch (IOException e) {
                Log.e("Error", "Error connecting to Places API", e);
                return null;
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            try {
                // Create a JSON object hierarchy from the results
                JSONObject jsonObj = new JSONObject(jsonResults.toString());
                JSONObject predsJsonArray = jsonObj.getJSONObject("result");
                JSONArray reviews = predsJsonArray.getJSONArray("reviews");
                // Extract the Place descriptions from the results
                Log.wtf("json", predsJsonArray.toString());

                for (int i = 0; i < reviews.length(); i++) {
                    result.add(reviews.getJSONObject(i).getString("text"));
                }
            } catch (JSONException e) {
                Log.e("Error", "Error processing JSON results", e);
            }
            return result;
        }
    }
}