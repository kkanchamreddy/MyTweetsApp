package com.kiran.mytweetsapp.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.kiran.mytweetsapp.R;
import com.kiran.mytweetsapp.TwitterClient;
import com.kiran.mytweetsapp.fragments.HomeTimelineFragment;
import com.kiran.mytweetsapp.fragments.MentionsTimelineFragment;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;

    private int lastMaxId = 0;


    private final int REQUEST_CODE = 1;

    @Override
    public void onResume() {
        super.onResume();
        //populateTimeline(lastMaxId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(myToolbar);


        getSupportActionBar().setLogo(R.drawable.ic_twitter_logo);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Get the view pager
        ViewPager vpPager = (ViewPager)findViewById(R.id.viewpager);

        //Set the view pager adapter for the pager
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));

        //Find the Page sliding tabstrip
        PagerSlidingTabStrip psTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        //Attach the pager tabs to the view pager
       psTabStrip.setViewPager(vpPager);

    }


    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_compose:
                Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
                startActivityForResult(i, REQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK  && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            //fetchTimelineAsync(0);
            // Toast the name to display temporarily on screen
            Toast.makeText(this, "Tweet success!!", Toast.LENGTH_SHORT).show();
        }
    }



    //Return the order of fragments in the view page
    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = {"Home", "Mentions"};


        //Adapter gets the manager to insert or remove fragments from the activity
        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        //The order and creation of fragments with in the pager
        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                return new HomeTimelineFragment();
            } else if(position == 1) {
                return new MentionsTimelineFragment();
            }
            return null;
        }

        //Returns the tab title
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        //How many fragments are there to swipe between
        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }

}
