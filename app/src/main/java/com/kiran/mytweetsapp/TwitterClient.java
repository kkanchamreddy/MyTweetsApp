package com.kiran.mytweetsapp;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "iwQrSym4aGNlAbH3v7o36z1I4";       // Change this
	public static final String REST_CONSUMER_SECRET = "aslGKj6hLDHPRT9pezQY6oX7ShZI1uJAIgRVXalOJE88ZzRp6u"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

    private void getTimeline(String apiUrl, long maxId,long sinceId,AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("count", 25);
        if(maxId > 0) {
            params.put("max_id", maxId);
        }
        if(sinceId > 0) {
            params.put("since_id", sinceId);
        }
        client.get(apiUrl, params, handler);
    }


	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */

	//Home Timeline - Gets the timeline
	//GET statuses/home_timeline.json
	//Params count=25, since_id=1

	public void getHomeTimeline(long maxId,long sinceId,AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
        getTimeline(apiUrl, maxId, sinceId, handler);
	}

	//Compose Tweet
	//POST statuses/update.json
	//Params: status

	public void postTweet(String tweetText,AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", tweetText);

		client.post(apiUrl,params,handler);
	}

	// Mentions Timeline
	//GET /mentions/timeline.json
	//Params: count, since_id
	public void getMentionsTimeline(long maxId, long sinceId, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        getTimeline(apiUrl, maxId, sinceId, handler);
	}

    //Likes Timeline
    public void getLikesTimeline(long maxId, long sinceId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("favorites/list.json");
        getTimeline(apiUrl, maxId, sinceId, handler);
    }

    //Get User Timeline
    public void getUserTimeline(long maxId, long sinceId, String screenName,AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", 25);
        if(maxId > 0) {
            params.put("max_id", maxId);
        }
        if(sinceId > 0) {
            params.put("since_id", sinceId);
        }
        params.put("screen_name", screenName);
        client.get(apiUrl, params, handler);
    }

    //Get User Info
    public void getUserInfo(String screenName, AsyncHttpResponseHandler handler) {
        String apiUrl;
        if(screenName == null) {
            apiUrl = getApiUrl("account/verify_credentials.json");
        } else {
            apiUrl = getApiUrl("users/show.json");
        }
        RequestParams params = new RequestParams();
        params.put("screen_name", screenName);
        client.get(apiUrl, params, handler);
    }

	//Retweet a tweet
	//Params: id (tweet id)

	public void postRetweet(long id, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/retweet/" + String.valueOf(id) + ".json");
		RequestParams params = new RequestParams();
		params.put("id", id);

		client.post(apiUrl,params,handler);
	}

    //Like/Favourite a tweet
    public void postLike(long id,boolean isLiked, AsyncHttpResponseHandler handler) {
        String apiUrl;
        if(isLiked) {
            apiUrl = getApiUrl("favorites/destroy.json");
        } else {
            apiUrl = getApiUrl("favorites/create.json");
        }

        RequestParams params = new RequestParams();
        params.put("id", id);

        client.post(apiUrl,params,handler);

    }
}