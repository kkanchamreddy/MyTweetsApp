package com.kiran.mytweetsapp.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kkanchamreddy on 12/11/15.
 */
public class User {
    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;
    private String bgImageUrl;
    private String tagLine;
    private int tweetCount;
    private int followersCount;
    private int followingCount;



    public String getName() {return name;}
    public long getUid() {
        return uid;
    }
    public String getScreenName() {
        return screenName;
    }
    public String getProfileImageUrl() {return profileImageUrl;}
    public String getBackgroundImageUrl() {return bgImageUrl;}
    public String getTagLine() { return tagLine; }
    public int getFollowersCount() { return followersCount; }
    public int getFollowingCount() { return followingCount;}
    public int getTweetCount() {return tweetCount;}

    public static User fromJSon(JSONObject json) {
        User user = new User();

        try {
            user.name = json.getString("name");
            user.uid = json.getLong("id");
            user.screenName = json.getString("screen_name");
            user.profileImageUrl = json.getString("profile_image_url");
            user.tagLine = json.getString("description");
            user.followersCount = json.getInt("followers_count");
            user.followingCount = json.getInt("friends_count");
            user.tweetCount = json.getInt("statuses_count");
            user.bgImageUrl =  json.getString("profile_background_image_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;


    }


}
