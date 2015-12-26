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
    private String embeddedImageUrl;
    private int retweetCount;
    private int likeCount;
    private User user;
    private boolean liked;
    private static long lastTweetId;
    private static long latestTweetId;

    public String getBody() {
        return body;
    }
    public long getUid() {
        return uid;
    }
    public String getCreatedAt() {
        return createdAt;
    }
    public String getEmbeddedImageUrl() {
        return embeddedImageUrl;
    }
    public int getRetweetCount() {return retweetCount;}
    public int getLikeCount(){return  likeCount;}
    public User getUser() {
        return user;
    }
    public boolean isliked(){return liked;}

    public void setLiked(boolean liked ) {
        this.liked = liked;
    }

    //Deserialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.retweetCount = jsonObject.getInt("retweet_count");
            tweet.liked =  jsonObject.getBoolean("favorited");
            //tweet.likeCount =jsonObject.getInt("favourites_count");
            JSONObject entities = jsonObject.getJSONObject("entities");
            if(entities != null && entities.has("media")) {
               JSONObject firstMediaObj = (JSONObject)entities.getJSONArray("media").get(0);
               tweet.embeddedImageUrl = firstMediaObj.getString("media_url");
            }

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

        if(tweetCount > 0) {
            lastTweetId = tweetList.get(tweetCount -1).getUid();
            long firstTweetId = tweetList.get(0).getUid();
            if(firstTweetId >= lastTweetId) {
                latestTweetId = firstTweetId;
            }
        }

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

    public static long getLatestTweetId() {
        return latestTweetId;
    }

}
