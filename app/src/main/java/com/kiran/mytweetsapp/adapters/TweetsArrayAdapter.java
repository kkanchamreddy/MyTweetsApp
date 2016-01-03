package com.kiran.mytweetsapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kiran.mytweetsapp.R;
import com.kiran.mytweetsapp.TwitterApplication;
import com.kiran.mytweetsapp.TwitterClient;
import com.kiran.mytweetsapp.activity.ComposeActivity;
import com.kiran.mytweetsapp.activity.ProfileActivity;
import com.kiran.mytweetsapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by kkanchamreddy on 12/11/15.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    private Context activityContext;
    private final int REQUEST_CODE = 1;
    Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(0.25f)
                .cornerRadiusDp(10)
                .oval(true)
                .build();

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context,android.R.layout.simple_list_item_1,tweets);
        this.activityContext = context;
    }

    // View lookup cache
    private static class ViewHolder {
        ImageView profileImage;
        TextView userName;
        TextView tweetBody;
        TextView createdAt;
        ImageView embeddedImage;
        ImageView replyToIcon;
        ImageView retweetIcon;
        TextView retweetCount;
        ImageView likeIcon;
        TextView likeCount;
    }

    //TODO: ViewHolder pattern

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //1. Get the tweet
        final Tweet tweet = getItem(position);

        //2 Find or Inflate the template
        final ViewHolder viewHolder; // view lookup cache stored in tag

        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet,parent,false);

            //3.Find the subviews to fill with data in the template
            viewHolder.profileImage = (ImageView)convertView.findViewById(R.id.ivProfileImage);
            viewHolder.userName = (TextView)convertView.findViewById(R.id.tvUserName);
            viewHolder.tweetBody = (TextView)convertView.findViewById(R.id.tvBody);
            viewHolder.createdAt = (TextView)convertView.findViewById(R.id.tvCreatedAt);
            viewHolder.embeddedImage =(ImageView)convertView.findViewById(R.id.ivEmbeddedimage);
            viewHolder.replyToIcon = (ImageView)convertView.findViewById(R.id.ivReplyToIcon);
            viewHolder.likeIcon = (ImageView)convertView.findViewById(R.id.ivLikeIcon);
            viewHolder.retweetCount = (TextView)convertView.findViewById(R.id.tvRetweetCount);
            viewHolder.likeCount = (TextView)convertView.findViewById(R.id.tvLikeCount);
            viewHolder.retweetIcon = (ImageView)convertView.findViewById(R.id.ivReTweetIcon);


            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //4.Populate the data into subviews
        viewHolder.userName.setText(tweet.getUser().getName());
        viewHolder.tweetBody.setText(tweet.getBody());
        viewHolder.createdAt.setText(Tweet.getRelativeTimeAgo(tweet.getCreatedAt()));
        viewHolder.profileImage.setImageResource(android.R.color.transparent);
        viewHolder.embeddedImage.setImageResource(android.R.color.transparent);
        viewHolder.retweetCount.setText(String.valueOf(tweet.getRetweetCount()));
        //viewHolder.likeCount.setText(String.valueOf(tweet.getLikeCount()));

        if(tweet.isliked()) {
            viewHolder.likeIcon.setImageResource(R.drawable.ic_liked);
        } else {
            viewHolder.likeIcon.setImageResource(R.drawable.ic_like);
        }

        if(tweet.isRetweeted()) {
            viewHolder.retweetIcon.setImageResource(R.drawable.ic_retweeted);
        } else {
            viewHolder.retweetIcon.setImageResource(R.drawable.ic_retweet);
        }

        //Handle the click event on Reply to Icon
        viewHolder.replyToIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(activityContext, ComposeActivity.class);
                i.putExtra("in_reply_to", "@" + tweet.getUser().getScreenName());
                ((Activity) activityContext).startActivityForResult(i, REQUEST_CODE);
            }
        });

        //Handle the Click event on the ReTweet Icon
        viewHolder.retweetIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwitterClient client = TwitterApplication.getRestClient();
                client.postRetweet(tweet.getUid(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        String toastMsg;
                        Tweet tempTweet = Tweet.fromJSON(response);
                        tweet.setRetweeted(tempTweet.isRetweeted());
                        if(tempTweet.isRetweeted()) {
                            viewHolder.retweetIcon.setImageResource(R.drawable.ic_retweeted);
                            toastMsg = "Successfully Retweeted!!";
                        } else {
                            viewHolder.retweetIcon.setImageResource(R.drawable.ic_retweet);
                            toastMsg = "You undid the retweet !!";
                        }

                        Toast.makeText(getContext(), toastMsg, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Toast.makeText(getContext(), "Retweet Failed,please try again", Toast.LENGTH_LONG).show();
                        Log.d("RETWEET_FAILURE", errorResponse.toString());
                    }
                });

            }
        });

        //Handle the ClickEvent on the Like Icon
        viewHolder.likeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwitterClient client = TwitterApplication.getRestClient();

                client.postLike(tweet.getUid(), tweet.isliked(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        String toastMsg;
                        Tweet tempTweet = Tweet.fromJSON(response);
                        tweet.setLiked(tempTweet.isliked());
                        if(tempTweet.isliked()) {
                            viewHolder.likeIcon.setImageResource(R.drawable.ic_liked);
                            toastMsg = "You liked the tweet!!";
                        } else {
                            viewHolder.likeIcon.setImageResource(R.drawable.ic_like);
                            toastMsg = "You unliked the tweet!!";
                        }
                        Toast.makeText(getContext(),toastMsg , Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Toast.makeText(getContext(), " Action failed,please try again", Toast.LENGTH_LONG).show();
                        Log.d("LIKE_FAILURE", errorResponse.toString());
                    }
                });

            }
        });


        Picasso.with(getContext())
                .load(tweet.getUser().getProfileImageUrl())
                .transform(transformation)
                .into(viewHolder.profileImage);

        //Add Listener to ProfileImage
        viewHolder.profileImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(activityContext, ProfileActivity.class);
                i.putExtra("screen_name", tweet.getUser().getScreenName());
                ((Activity) activityContext).startActivity(i);
            }
        });

        String embeddedImageUrl = tweet.getEmbeddedImageUrl();
        if(embeddedImageUrl != null) {
            Picasso.with(getContext())
                    .load(embeddedImageUrl)
                    .into(viewHolder.embeddedImage);
        }


        //5. Return the view to be inserted into the list
        return convertView;
    }


}
