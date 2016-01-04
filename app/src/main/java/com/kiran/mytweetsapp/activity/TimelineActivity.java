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
import com.kiran.mytweetsapp.FragmentChangeListener;
import com.kiran.mytweetsapp.R;
import com.kiran.mytweetsapp.TwitterClient;
import com.kiran.mytweetsapp.fragments.ComposeFragment;
import com.kiran.mytweetsapp.fragments.HomeTimelineFragment;
import com.kiran.mytweetsapp.fragments.LikesTimelineFragment;
import com.kiran.mytweetsapp.fragments.ListsFragment;
import com.kiran.mytweetsapp.fragments.ListsTimelineFragment;
import com.kiran.mytweetsapp.fragments.MentionsTimelineFragment;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;

    private int lastMaxId = 0;
    private TweetsPagerAdapter tweetsPagerAdapter;


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
        tweetsPagerAdapter = new TweetsPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(tweetsPagerAdapter);

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
                //Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
                //startActivityForResult(i, REQUEST_CODE);
                showComposeDialog();
                return true;
            case R.id.action_profile:
                Intent profileIntent = new Intent(TimelineActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void showComposeDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ComposeFragment frag = ComposeFragment.newInstance(null);
        frag.show(fm, "fragment_edit_name");

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

    /*
    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.viewpager, fragment, "fragment");
        fragmentTransaction.addToBackStack("fragment");
        fragmentTransaction.commit();
        tweetsPagerAdapter.notifyDataSetChanged();
    }
*/

    //Return the order of fragments in the view page
    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = {"Home", "Mentions", "Likes", "Lists"};
        private Fragment mFragmentAtPos3;
        private final FragmentManager mFragmentManager;
        FragmentChangeListener listener = new PageListener();

        private final class PageListener implements
                FragmentChangeListener {
            public void onFragmentChange(long id) {
                mFragmentManager.beginTransaction().remove(mFragmentAtPos3).commit();
                if (mFragmentAtPos3 instanceof ListsFragment){
                    mFragmentAtPos3 = ListsTimelineFragment.newInstance(id, listener);
                }else{ // Instance of NextFragment
                    mFragmentAtPos3 =  ListsFragment.newInstance(listener);
                }
                notifyDataSetChanged();
            }
        }

        //Adapter gets the manager to insert or remove fragments from the activity
        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragmentManager = fm;
        }

        //The order and creation of fragments with in the pager
        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                return new HomeTimelineFragment();
            } else if(position == 1) {
                return new MentionsTimelineFragment();
            } else if(position == 2) {
                return new LikesTimelineFragment();
            } else if(position == 3) {
                if (mFragmentAtPos3 == null)
                {
                    mFragmentAtPos3 = ListsFragment.newInstance(listener);;
                }
                return mFragmentAtPos3;
            }
            return null;
        }

       @Override
        public int getItemPosition(Object object)
        {
           // Log.d("OBJECT -INSTANCE", String.valueOf(object instanceof ListsFragment));
            // Log.d("mFragmentAtPos3", String.valueOf(mFragmentAtPos3 instanceof ListsTimelineFragment));
            if (object instanceof ListsFragment &&
                    mFragmentAtPos3 instanceof ListsTimelineFragment) {
                return POSITION_NONE;
            }
            if (object instanceof ListsTimelineFragment &&
                    mFragmentAtPos3 instanceof ListsFragment) {
                return POSITION_NONE;
            }
            return POSITION_UNCHANGED;
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
