package com.kiran.mytweetsapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kiran.mytweetsapp.R;
import com.kiran.mytweetsapp.TweetsArrayAdapter;
import com.kiran.mytweetsapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kkanchamreddy on 12/18/15.
 */
public class TweetsListFragment extends Fragment {

    private TweetsArrayAdapter tweetsAdapter;
    protected ListView lvTweets;
    private ArrayList<Tweet> tweets;
    protected SwipeRefreshLayout swipeContainer;

    //Infaltion Logic

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_tweets_list, container,false);

        lvTweets = (ListView)v.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(tweetsAdapter);

        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);

        return v;
    }


    //Creation Lifecycle Event


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweets = new ArrayList<>();

        tweetsAdapter = new TweetsArrayAdapter(getActivity(), tweets);
    }

    public void addAll(List<Tweet> tweetList) {
        tweetsAdapter.addAll(tweetList);
    }

    public void clear() {
        tweetsAdapter.clear();
    }


   /*
       Inner class to handle timeline responses( first page and also infinite scrolling) both for Home Timeline & Mentions
    */
    public class TimelineResponseHandler extends JsonHttpResponseHandler{

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            addAll(Tweet.fromJSONArray(response));
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            Log.d("DEBUG", "Home timeline fetch error: " + errorResponse);
        }

    }


    /*
        Inner class to handle timeline responses(on Swipe down to fetch latest tweets) both for Home Timeline & Mentions
    */
    public class TimelineSwipeResponseHandler extends JsonHttpResponseHandler{

        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            // Remember to CLEAR OUT old items before appending in the new ones
            clear();
            // ...the data has come back, add new items to your adapter...
            addAll(Tweet.fromJSONArray(response));
            // Now we call setRefreshing(false) to signal refresh has finished
            swipeContainer.setRefreshing(false);
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            Log.d("DEBUG", "Fetch timeline error: " + errorResponse);
        }


    }
}
