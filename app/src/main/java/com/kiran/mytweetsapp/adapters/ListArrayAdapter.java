package com.kiran.mytweetsapp.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kiran.mytweetsapp.FragmentChangeListener;
import com.kiran.mytweetsapp.R;
import com.kiran.mytweetsapp.fragments.ListsTimelineFragment;
import com.kiran.mytweetsapp.models.List;

/**
 * Created by kkanchamreddy on 1/2/16.
 */
public class ListArrayAdapter extends ArrayAdapter<List> {

    private Context activityContext;
    private final int REQUEST_CODE = 1;

    public ListArrayAdapter(Context context, java.util.List<List> tweetLists) {
        super(context,android.R.layout.simple_list_item_1,tweetLists);
        this.activityContext = context;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView name;
        TextView description;
        RelativeLayout container;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        //1. Get the list
        final List tweetList = getItem(position);

        //2 Find or Inflate the template
        final ViewHolder viewHolder; // view lookup cache stored in tag

        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet_list,parent,false);

            //3.Find the subviews to fill with data in the template
            viewHolder.name = (TextView)convertView.findViewById(R.id.tvListName);
            viewHolder.description = (TextView)convertView.findViewById(R.id.tvListDescription);
            viewHolder.container = (RelativeLayout)convertView.findViewById(R.id.vTweetListContainer);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        //4.Populate the data into subviews
        viewHolder.name.setText(tweetList.getName());
        viewHolder.description.setText(tweetList.getDescription());
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fr= ListsTimelineFragment.newInstance(tweetList.getId());
                FragmentChangeListener fc=(FragmentChangeListener)getContext();
                fc.replaceFragment(fr);
            }
        });

        //5. Return the view to be inserted into the list
        return convertView;
    }
}
