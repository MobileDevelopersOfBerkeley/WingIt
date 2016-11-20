package com.mdb.wingit.wingit;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private FirebaseAuth mAuth;
    private GoogleApiClient client;
    private int MY_PERMISSION_ACCESS_FINE_LOCATION = 1;
    static LatLng current;
    static int indexPlace = 0;
    static String[] topFive = new String[5];
    static ArrayList<Place> currentLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        client = new GoogleApiClient.Builder(this).addApi(Places.GEO_DATA_API).build();
        currentLocations = new ArrayList<>();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,
                    new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION },
                    MY_PERMISSION_ACCESS_FINE_LOCATION);
            Log.i("Permissions???", "rip");
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
                for(int i=0; i<5; i++){
                    topFive[i] = currentLocations.get(i).getName().toString();
                }
                current = currentLocations.get(indexPlace).getLatLng();
                likelyPlaces.release();
            }
        });
        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            mAuth.signOut();
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public static class StartOptions extends Fragment implements View.OnClickListener {

        TextView location;
        FirebaseDatabase database;
        DatabaseReference db = database.getReference().child("adventures");

        public static StartOptions newInstance(int page) {
            Bundle args = new Bundle();
            args.putInt("pagenumber", page);
            StartOptions fragment = new StartOptions();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_start_options, container, false);

            CardView food = (CardView) v.findViewById(R.id.food);
            CardView activity = (CardView) v.findViewById(R.id.activity);
            location = (TextView) v.findViewById(R.id.location2);
            TextView change = (TextView) v.findViewById(R.id.change);


            location.setText("Current location: "+currentLocations.get(indexPlace).getName().toString());


            change.setOnClickListener(this);
            food.setOnClickListener(this);
            activity.setOnClickListener(this);
            return v;
        }

        //Intent intent = new Intent(getActivity(), Carousel.class);

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.food:
                    String key = db.push().getKey();

                    Intent foodIntent = new Intent(getActivity(), Carousel.class);
                    foodIntent.putExtra("food", true);
                    foodIntent.putExtra("current", current);
                    startActivity(foodIntent);
                    break;
                case R.id.activity:
                    String key1 = db.push().getKey();
                    Intent activityIntent = new Intent(getActivity(), Carousel.class);
                    activityIntent.putExtra("food", false);
                    activityIntent.putExtra("current",current);
                    startActivity(activityIntent);
                    break;
                case R.id.change:
                    // 1. Instantiate an AlertDialog.Builder with its constructor

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Pick a better location:")
                            .setItems(topFive, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // The 'which' argument contains the index position
                                    // of the selected item
                                    String name = topFive[which];
                                    current = findFuckinObject(name);
                                    location.setText("Current location: "+name);
                                }
                            });
                    // 2. Get the AlertDialog from create()
                    AlertDialog dialog = builder.create();

            }
        }

        public LatLng findFuckinObject(String name){
            LatLng ans = new LatLng(0,0);
            for(Place p : currentLocations){
                if(p.getName().equals(name))
                    ans = p.getLatLng();
            }
            return ans;
        }


    }

    public static class AdventureLog extends Fragment {

        private RecyclerView rv;
        private AdventureAdapter adapter;
        private AdventureList adventureList;
        private ArrayList<AdventureList.Adventure> adventures;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            adventureList = new AdventureList();
            adventures = adventureList.getArrayList();
            View view = inflater.inflate(R.layout.activity_adventure_log, container, false);

            rv = (RecyclerView) view.findViewById(R.id.adventureLogRv);
            rv.setLayoutManager(new LinearLayoutManager(getContext()));

            adapter = new AdventureAdapter(getContext(), adventures);
            rv.setAdapter(adapter);

            return view;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            switch (position) {
                case 0:
                    AdventureLog tab2 = new AdventureLog();
                    //StartOptions tab1 = new StartOptions();
                    return tab2;
                case 1:
                    StartOptions tab1 = new StartOptions();
                    return tab1;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "My Past Trips";
                case 1:
                    return "Begin Adventure";
            }
            return null;
        }
    }

}
