package com.kiran.mytweetsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kiran.mytweetsapp.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kkanchamreddy on 12/11/15.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {
    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context,android.R.layout.simple_list_item_1,tweets);
    }

    //TODO: ViewHolder pattern
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //1. Get the tweet
        Tweet tweet = getItem(position);

        //2 Find or Inflate the template
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet,parent,false);
        }

        //3. Find the subviews to fill with data in the template
        ImageView ivImageView = (ImageView)convertView.findViewById(R.id.ivProfileImage);
        TextView tvUserName = (TextView)convertView.findViewById(R.id.tvUserName);
        TextView tvTweetBody = (TextView)convertView.findViewById(R.id.tvBody);


        //4.Populate the data into subviews

        tvUserName.setText(tweet.getUser().getName());
        tvTweetBody.setText(tweet.getBody());
        ivImageView.setImageResource(android.R.color.transparent);

        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivImageView);

        //5. Return the view to be inserted into the list
        return convertView;



    }
}
