package com.kiran.mytweetsapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kiran.mytweetsapp.models.Tweet;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

/**
 * Created by kkanchamreddy on 12/11/15.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(0.25f)
                .cornerRadiusDp(10)
                .oval(true)
                .build();

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context,android.R.layout.simple_list_item_1,tweets);
    }

    // View lookup cache
    private static class ViewHolder {
        ImageView profileImage;
        TextView userName;
        TextView tweetBody;
        TextView createdAt;
        ImageView embeddedImage;
    }

    //TODO: ViewHolder pattern

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //1. Get the tweet
        Tweet tweet = getItem(position);

        //2 Find or Inflate the template
        ViewHolder viewHolder; // view lookup cache stored in tag

        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet,parent,false);

            //3.Find the subviews to fill with data in the template
            viewHolder.profileImage = (ImageView)convertView.findViewById(R.id.ivProfileImage);
            viewHolder.userName = (TextView)convertView.findViewById(R.id.tvUserName);
            viewHolder.tweetBody = (TextView)convertView.findViewById(R.id.tvBody);
            viewHolder.createdAt = (TextView)convertView.findViewById(R.id.tvCreatedAt);
            viewHolder.embeddedImage =(ImageView)convertView.findViewById(R.id.ivEmbeddedimage);

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

        Picasso.with(getContext())
                .load(tweet.getUser().getProfileImageUrl())
                .transform(transformation)
                .into(viewHolder.profileImage);

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
