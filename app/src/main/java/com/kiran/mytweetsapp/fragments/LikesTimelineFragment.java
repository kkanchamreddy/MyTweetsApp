package com.kiran.mytweetsapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kiran.mytweetsapp.EndlessScrollListener;
import com.kiran.mytweetsapp.models.Tweet;

/**
 * Created by kkanchamreddy on 12/24/15.
 */
public class LikesTimelineFragment extends TweetsListFragment {


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
        Log.d("LIKES-TIMELINE", "RESUMED");
        populateTimeline(lastMaxId);
    }

    private void populateTimeline(long maxId) {
        client.getLikesTimeline(maxId, 0, new TimelineResponseHandler());
    }


    private void fetchTimelineAsync() {
        client.getLikesTimeline(0, Tweet.getLatestTweetId(), new TimelineSwipeResponseHandler());
    }
}
