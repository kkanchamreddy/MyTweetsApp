package com.kiran.mytweetsapp.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.kiran.mytweetsapp.R;
import com.kiran.mytweetsapp.TwitterApplication;
import com.kiran.mytweetsapp.TwitterClient;
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
        client.getUserInfo(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                user = User.fromJSon(response);
                //Current User's info
                getSupportActionBar().setTitle(user.getScreenName());

            }
        });

        //Get the screen name from the activity that launches it
        String screenName = getIntent().getStringExtra("screen_name");

        if(savedInstanceState == null) {
            //Create User Timeline Fragment
            UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screenName);

            //Display user timeline fragment dynamically
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer,userTimelineFragment);
            ft.commit();
        }


    }
}
