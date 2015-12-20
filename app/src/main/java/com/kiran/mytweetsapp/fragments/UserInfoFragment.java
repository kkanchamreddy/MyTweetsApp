package com.kiran.mytweetsapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kiran.mytweetsapp.R;
import com.kiran.mytweetsapp.TwitterApplication;
import com.kiran.mytweetsapp.TwitterClient;
import com.kiran.mytweetsapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by kkanchamreddy on 12/20/15.
 */
public class UserInfoFragment extends Fragment {

    private TwitterClient client;


    // Creates a new fragment given an int and title
    public static UserInfoFragment newInstance() {
        //UserInfoFragment userFragment = new UserInfoFragment();
        return new UserInfoFragment();
    }

    //Infaltion Logic

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_user_info, container,false);
        return v;
    }

    //Creation Lifecycle Event


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient();

        populateUserInfo();
    }
    
    private void populateUserInfo() {
        client.getUserInfo(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                User user = User.fromJSon(response);
                setUserInfo(user);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            }

        });
    }

    private void setUserInfo(User user) {

        ImageView ivProfileImage = (ImageView)getView().findViewById(R.id.ivProfileImage);
        TextView tvUserName = (TextView)getView().findViewById(R.id.tvUserName);
        TextView tvTagLine = (TextView)getView().findViewById(R.id.tvTagline);

        TextView tvTweetCount = (TextView)getView().findViewById(R.id.tvTweetCount);
        TextView tvFollowingCount = (TextView)getView().findViewById(R.id.tvFollowingCount);
        TextView tvFollowersCount = (TextView)getView().findViewById(R.id.tvFollowersCount);

        Picasso.with(getContext()).load(user.getProfileImageUrl()).into(ivProfileImage);
        tvUserName.setText(user.getName());
        tvTagLine.setText(user.getTagLine());
        Log.d("User count", String.valueOf(user.getFollowingCount()));
        tvTweetCount.setText(user.getTweetCount() + " Tweets");
        tvFollowingCount.setText(user.getFollowingCount() + " Following");
        tvFollowersCount.setText(user.getFollwersCount() + " Followers");
    }

}
