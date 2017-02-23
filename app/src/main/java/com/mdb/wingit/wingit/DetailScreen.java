package com.mdb.wingit.wingit;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.mdb.wingit.wingit.CategorySelectorActivity.current;


/** USE THIS ACTIVITY FOR DETAILS IN ADVENTURE TIMELINE THEN DELETE */
public class DetailScreen extends AppCompatActivity implements View.OnClickListener{

    Toolbar toolbar;
    CollapsingToolbarLayout toolbarLayout;
    ImageView imageView;
    // TextView textView;
    FloatingActionButton fab;
    DatabaseReference dbRef;
    String place_id;
    TextView r1, r2, r3, r4, r5;
    CardView c1, c2, c3, c4, c5;
    ArrayList<String> result = new ArrayList<String>();
    ArrayList<String> reviews = new ArrayList<>();
    ArrayList<TextView> reviewBoxes;
    ArrayList<CardView> cards;
    Button nextButton;
    Button endButton;
    private static Adventure adventure;
    private static String date;
    private static ArrayList<Adventure> adventures = new ArrayList<>();
    private static DatabaseReference mDatabase;
    private static FirebaseUser user;
    EditText title = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_screen);

        dbRef = FirebaseDatabase.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        adventure = new Adventure();
        date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        place_id = getIntent().getExtras().getString("place_id");
        getReviews();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle(getIntent().getStringExtra("name"));

        imageView = (ImageView) findViewById(R.id.imageView);
        String photoRef = getIntent().getStringExtra("photoRef");
        Glide.with(getApplicationContext()).load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+photoRef+"&key="+ CarouselActivity.API_KEY_NONRESTRICTED).into(imageView);
        r1 = (TextView) findViewById(R.id.r1);
        r2 = (TextView) findViewById(R.id.r2);
        r3 = (TextView) findViewById(R.id.r3);
        r4 = (TextView) findViewById(R.id.r4);
        r5 = (TextView) findViewById(R.id.r5);
        c1 = (CardView) findViewById(R.id.c1);
        c2 = (CardView) findViewById(R.id.c2);
        c3 = (CardView) findViewById(R.id.c3);
        c4 = (CardView) findViewById(R.id.c4);
        c5 = (CardView) findViewById(R.id.c5);
        reviewBoxes = new ArrayList<TextView>();
        cards = new ArrayList<>();
        reviewBoxes.add(r1);
        reviewBoxes.add(r2);
        reviewBoxes.add(r3);
        reviewBoxes.add(r4);
        reviewBoxes.add(r5);
        cards.add(c1);
        cards.add(c2);
        cards.add(c3);
        cards.add(c4);
        cards.add(c5);

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

        String searchRequest = "https://maps.googleapis.com/maps/api/place/details/json?placeid="+place_id+"&key="+ CarouselActivity.API_KEY_NONRESTRICTED;
        new DetailTask() {
            @Override
            protected void onPreExecute() {

            }
            @Override
            protected void onPostExecute(ArrayList<String> activityResult) {
                reviews = activityResult;
                if(reviews.size()<5){
                    setFirstReview(reviews.size());
                }
                setReviews(reviews.size());
            }
        }.execute(searchRequest);
    }

    public void setFirstReview(int num){
        if (reviews.size() == 0) {
            reviewBoxes.get(0).setText("No reviews available");
            for (int i = 1; i < 5; i ++) {
                reviewBoxes.get(i).setVisibility(View.GONE);
                cards.get(i).setVisibility(View.GONE);
            }
        } else {
            for(int i = num; i<5; i++){
                reviewBoxes.get(i).setVisibility(View.GONE);
                cards.get(i).setVisibility(View.GONE);
            }
        }

    }

    public void setReviews(int num){
        for(int i=0; i<num; i++){
            if (!reviewBoxes.get(i).equals("")) {
                reviewBoxes.get(i).setText("\"" + reviews.get(i) + "\"");
            }

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
                        // CategorySelectorActivity.StartOptions t = new CategorySelectorActivity.StartOptions();
                        // t.createAdventure();

                        Intent activityIntent = new Intent(getApplicationContext(), CarouselActivity.class);
                        activityIntent.putExtra("food", false);
                        activityIntent.putExtra("current",current);
                        // activityIntent.putExtra("adventureKey", CategorySelectorActivity.StartOptions.adventureKey);
                        startActivity(activityIntent);
                    }
                });

                CardView food = (CardView) dialog.findViewById(R.id.food);
                food.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // CategorySelectorActivity.StartOptions.createAdventure();

                        Intent foodIntent = new Intent(getApplicationContext(), CarouselActivity.class);
                        foodIntent.putExtra("food", true);
                        foodIntent.putExtra("current", current);
                        // foodIntent.putExtra("adventureKey", CategorySelectorActivity.StartOptions.adventureKey);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailScreen.this);
                builder.setTitle("Enter a title for your adventure");
                final EditText editText = new EditText(builder.getContext());
                LinearLayout.LayoutParams endtriplp;
                endtriplp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                editText.setLayoutParams(endtriplp);
                builder.setView(editText);

                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createAdventure(editText.getText().toString());
                        startActivity(new Intent(DetailScreen.this, CategorySelectorActivity.class));
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog2 = builder.create();
                dialog2.show();
                break;
            default:
                break;
        }

    }

    public static void createAdventure(String name) {
        // Create a new adventure and add to db
        adventure.setStartloc(name);
        adventure.setDate(date);
        final DatabaseReference adventureDB = mDatabase.child("Adventures").push();
        adventureDB.setValue(adventure);
        adventures.add(adventure);
        // adapter.notifyDataSetChanged();

        // Add adventure to user's adventurelist
        final String uid = user.getUid();
        DatabaseReference userAdventureList = mDatabase.child("Users").child(uid);
        userAdventureList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                user.addAdventureKey(adventureDB.getKey());
                mDatabase.child("Users").child(uid).child("adventureKeysList").setValue(user.getAdventureKeysList());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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