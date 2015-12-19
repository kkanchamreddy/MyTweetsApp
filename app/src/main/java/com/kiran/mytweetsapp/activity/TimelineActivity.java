package com.kiran.mytweetsapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.kiran.mytweetsapp.R;
import com.kiran.mytweetsapp.TwitterClient;

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
        getSupportActionBar().setTitle(R.string.home);


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

}
