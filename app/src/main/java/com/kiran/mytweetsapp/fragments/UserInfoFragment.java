package com.kiran.mytweetsapp.fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kiran.mytweetsapp.R;
import com.kiran.mytweetsapp.TwitterApplication;
import com.kiran.mytweetsapp.TwitterClient;
import com.kiran.mytweetsapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by kkanchamreddy on 12/20/15.
 */
public class UserInfoFragment extends Fragment {

    private TwitterClient client;
    View rootView;


    // Creates a new fragment given an int and title
    public static UserInfoFragment newInstance(String screenName) {
        UserInfoFragment userFragment = new UserInfoFragment();

        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        userFragment.setArguments(args);

        return  userFragment;
    }

    //Infaltion Logic

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        rootView = inflater.inflate(R.layout.fragment_user_info, container,false);
        //populateMockUserInfo();
        return rootView;
    }

    //Creation Lifecycle Event


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();

        populateUserInfo();
    }
    /*
    private void populateMockUserInfo() {
        User user = new User();
        user.screenName = "kkanchamreddy";
        user.name = "Kiran Kanchamreddy";
        user.followingCount = 120;
        user.tweetCount = 101;
        user.followersCount  = 200;
        user.profileImageUrl = "https://pbs.twimg.com/profile_images/3247033648/3699e17afe7763d7c1428715d000c67b_bigger.jpeg";
        user.bgImageUrl = "http://abs.twimg.com/images/themes/theme4/bg.gif";
        setUserInfo(user);
    }
    */
    private void populateUserInfo() {
        String screenName = getArguments().getString("screen_name");
        client.getUserInfo(screenName, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                User user = User.fromJSon(response);
                Log.d("PROFILE_IMAGE", user.getBackgroundImageUrl());
                setUserInfo(user);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            }

        });
    }

    private void setUserInfo(final User user) {
        final RelativeLayout rlUserHeader = (RelativeLayout)rootView.findViewById(R.id.rlUserInfoHeader);
        ImageView ivProfileImage = (ImageView)rootView.findViewById(R.id.ivProfileImage);
        TextView tvUserName = (TextView)rootView.findViewById(R.id.tvUserName);
        TextView tvTagLine = (TextView)rootView.findViewById(R.id.tvTagline);

        TextView tvTweetCount = (TextView)rootView.findViewById(R.id.tvTweetCount);
        TextView tvFollowingCount = (TextView)rootView.findViewById(R.id.tvFollowingCount);
        TextView tvFollowersCount = (TextView)rootView.findViewById(R.id.tvFollowersCount);

        Picasso.with(getContext()).load(user.getProfileImageUrl()).into(ivProfileImage);
        tvUserName.setText(user.getName());
        tvTagLine.setText(user.getTagLine());
        tvTweetCount.setText(String.valueOf(user.getTweetCount()));
        tvFollowingCount.setText(String.valueOf(user.getFollowingCount()));
        tvFollowersCount.setText(String.valueOf(user.getFollowersCount()));

        Picasso.with(getContext()).load(user.getBackgroundImageUrl()).into(new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                rlUserHeader.setBackground(new BitmapDrawable(getContext().getResources(), bitmap));
            }

            @Override
            public void onBitmapFailed(final Drawable errorDrawable) {
                Log.d("TAG", "FAILED");
            }

            @Override
            public void onPrepareLoad(final Drawable placeHolderDrawable) {
                Log.d("TAG", "Prepare Load");
            }
        });
    }

}
