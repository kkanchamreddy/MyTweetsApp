package com.kiran.mytweetsapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kiran.mytweetsapp.R;
import com.kiran.mytweetsapp.TweetsArrayAdapter;
import com.kiran.mytweetsapp.models.Tweet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kkanchamreddy on 12/18/15.
 */
public class TweetsList extends Fragment {

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
}
