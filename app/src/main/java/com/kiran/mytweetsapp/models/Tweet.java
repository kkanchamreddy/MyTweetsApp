package com.kiran.mytweetsapp.models;

import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by kkanchamreddy on 12/11/15.
 */

//Parse the JSON & Store the data
public class Tweet {
    private String body;
    private long uid; //unique tweet id
    private String createdAt;
    private User user;
    private static long lastTweetId;

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    //Deserialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSon(jsonObject.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tweet;

    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray response) {
        ArrayList<Tweet> tweetList = new ArrayList<>();
        int tweetCount = response.length();
        for(int i = 0; i< tweetCount; i++) {
            JSONObject tweetJSON = null;
            try {
                tweetJSON = response.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJSON);
                if(tweet!=null) {
                    tweetList.add(tweet);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }

        }

        lastTweetId = tweetList.get(tweetCount -1).getUid();
        return tweetList;
    }

    //Get Relative Time ago
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public static long getLastTweetId() {
        return lastTweetId;
    }

}
