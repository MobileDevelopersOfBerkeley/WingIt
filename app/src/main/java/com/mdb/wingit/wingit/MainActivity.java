package com.mdb.wingit.wingit;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //
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
            startActivity(new Intent(getApplicationContext(),Login.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public static class StartOptions extends Fragment implements View.OnClickListener {

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
            LinearLayout op1 = (LinearLayout) v.findViewById(R.id.option1);
            LinearLayout op2 = (LinearLayout) v.findViewById(R.id.option2);
            LinearLayout op3 = (LinearLayout) v.findViewById(R.id.option3);
            op1.setOnClickListener(this);
            op2.setOnClickListener(this);
            op3.setOnClickListener(this);
            return v;
        }

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.option1:
                    //open food stuff
                    break;
                case R.id.option2:
                    //open activity stuff
                    break;
                case R.id.option3:
                    //open sightseeing stuff
                    break;
            }
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
