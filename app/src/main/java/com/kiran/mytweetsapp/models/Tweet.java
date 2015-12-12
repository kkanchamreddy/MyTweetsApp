package com.kiran.mytweetsapp.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kkanchamreddy on 12/11/15.
 */

//Parse the JSON & Store the data
public class Tweet {
    private String body;
    private long uid; //unique tweet id
    private String createdAt;
    private User user;


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

        for(int i = 0; i< response.length(); i++) {
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

        return tweetList;
    }
}
