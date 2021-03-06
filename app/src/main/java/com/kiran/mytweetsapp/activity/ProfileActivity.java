package com.kiran.mytweetsapp.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.kiran.mytweetsapp.R;
import com.kiran.mytweetsapp.TwitterApplication;
import com.kiran.mytweetsapp.TwitterClient;
import com.kiran.mytweetsapp.fragments.UserInfoFragment;
import com.kiran.mytweetsapp.fragments.UserTimelineFragment;
import com.kiran.mytweetsapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    TwitterClient client;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        client = TwitterApplication.getRestClient();
        

        Toolbar profileToolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(profileToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profileToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Get the screen name from the activity that launches it
        String screenName = getIntent().getStringExtra("screen_name");



        client.getUserInfo(screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJSon(response);
                //Current User's info
                getSupportActionBar().setTitle("@" + user.getScreenName());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", "User Info error: " + errorResponse);
            }
        });


        if(savedInstanceState == null) {
            //Create User Timeline Fragment
            UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screenName);
            //Create User Profile Header Fragment
            UserInfoFragment userInfoFragment = UserInfoFragment.newInstance(screenName);

            //Display user timeline fragment dynamically
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.replace(R.id.flContainer, userTimelineFragment);
            ft.replace(R.id.flUserHeader, userInfoFragment);
            ft.commit();
        }
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }
}
