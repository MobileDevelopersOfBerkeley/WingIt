package com.mdb.wingit.wingit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
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
            //FireBase logout
        }
        return super.onOptionsItemSelected(item);
    }

    public static class StartOptions extends Fragment implements View.OnClickListener {

        public StartOptions() {
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = getView();
            LinearLayout op1 = (LinearLayout) v.findViewById(R.id.option1);
            LinearLayout op2 = (LinearLayout) v.findViewById(R.id.option2);
            LinearLayout op3 = (LinearLayout) v.findViewById(R.id.option3);
            op1.setOnClickListener(this);
            op2.setOnClickListener(this);
            op3.setOnClickListener(this);
            return inflater.inflate(R.layout.fragment_start_options, container, false);
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

        public AdventureLog() {
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            if (position == 0)
                //replace with correct fragment
                return new StartOptions();
            else
                return new AdventureLog();
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
