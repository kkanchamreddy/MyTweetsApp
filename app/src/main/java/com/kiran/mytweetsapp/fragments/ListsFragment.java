package com.kiran.mytweetsapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kiran.mytweetsapp.FragmentChangeListener;
import com.kiran.mytweetsapp.R;
import com.kiran.mytweetsapp.TwitterApplication;
import com.kiran.mytweetsapp.TwitterClient;
import com.kiran.mytweetsapp.adapters.ListArrayAdapter;
import com.kiran.mytweetsapp.models.List;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kkanchamreddy on 1/2/16.
 */
public class ListsFragment extends Fragment {

    private ListArrayAdapter listAdapter;
    protected ListView lvList;
    private ArrayList<List> lists;
    TwitterClient client;
    static FragmentChangeListener fragmentChangeListener;


    // Creates a new fragment given an int and title
    public static ListsFragment newInstance(  FragmentChangeListener listener) {
        ListsFragment listFragment = new ListsFragment();
        fragmentChangeListener = listener;
        return listFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_lists, container, false);
        lvList = (ListView)v.findViewById(R.id.lvLists);
        lvList.setAdapter(listAdapter);

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List tweetList = lists.get(position);
                fragmentChangeListener.onFragmentChange(tweetList.getId());
                /*
                Fragment listTimelineFragment = ListsTimelineFragment.newInstance(tweetList.getId());
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.vTweetListContainer, listTimelineFragment).commit();*/

               /* Fragment fr= ListsTimelineFragment.newInstance(tweetList.getId());
                FragmentChangeListener fc=(FragmentChangeListener)getContext();
                fc.replaceFragment(fr);*/



            }
        });

        return  v;
    }

    //Creation Lifecycle Event
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lists = new ArrayList<>();
        listAdapter = new ListArrayAdapter(getActivity(), lists);
        client = TwitterApplication.getRestClient();
        populateLists();
    }

    public void addAllLists(java.util.List<List> list) {
        listAdapter.addAll(list);
    }

    private void populateLists() {
        client.getLists(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                try {
                    JSONArray lists =(JSONArray) response.getJSONArray("lists");
                    addAllLists(List.fromJSONArray(lists));
                }catch(JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", "Home timeline fetch error: " + errorResponse);
            }
        });
    }
}
