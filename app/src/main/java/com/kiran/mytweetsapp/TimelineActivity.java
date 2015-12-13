package com.kiran.mytweetsapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.kiran.mytweetsapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    private TweetsArrayAdapter tweetsAdapter;
    private ListView lvTweets;
    private ArrayList<Tweet> tweets;
    private int lastMaxId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        client = TwitterApplication.getRestClient();

        lvTweets = (ListView)findViewById(R.id.lvTweets);

        tweets = new ArrayList<>();

        tweetsAdapter = new TweetsArrayAdapter(this, tweets);

        lvTweets.setAdapter(tweetsAdapter);

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi();
                // or customLoadMoreDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });
        
        populateTimeline(lastMaxId);
    }

    // Append more data into the adapter
    public void customLoadMoreDataFromApi() {
        populateTimeline(Tweet.getLastTweetId());
    }

    private void populateTimeline(long maxId) {
        client.getHomeTimeline(maxId, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                tweetsAdapter.addAll(Tweet.fromJSONArray(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            }

        });
    }
}
