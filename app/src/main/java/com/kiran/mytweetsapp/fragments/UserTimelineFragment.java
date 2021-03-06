package com.kiran.mytweetsapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kiran.mytweetsapp.EndlessScrollListener;
import com.kiran.mytweetsapp.models.Tweet;

/**
 * Created by kkanchamreddy on 12/19/15.
 */
public class UserTimelineFragment extends TweetsListFragment {

    // Creates a new fragment given an int and title
    public static UserTimelineFragment newInstance( String screen_name) {
        UserTimelineFragment userFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screen_name);
        userFragment.setArguments(args);
        return userFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                populateTimeline(Tweet.getLastTweetId() - 1);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateTimeline(lastMaxId);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isPaused) {
            isPaused = false;
            fetchTimelineAsync();
        }
    }

    private void populateTimeline(long maxId) {
        String screenName = getArguments().getString("screen_name");
        client.getUserTimeline(maxId, 0, screenName, new TimelineResponseHandler());
    }

    private void fetchTimelineAsync() {
        String screenName = getArguments().getString("screen_name");
        client.getUserTimeline(0, Tweet.getLatestTweetId(),screenName, new TimelineSwipeResponseHandler());
    }
}
