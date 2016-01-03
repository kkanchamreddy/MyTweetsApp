package com.kiran.mytweetsapp.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kkanchamreddy on 1/2/16.
 */
public class List {
    private long id;
    private String name;
    private String description;
    private String mode;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getMode() {
        return mode;
    }

    public static ArrayList<List> fromJSONArray(JSONArray response) {
        ArrayList<List> tweetList = new ArrayList<>();
        int listCount = response.length();
        for(int i = 0; i< listCount; i++) {
            JSONObject listJSON = null;
            try {
                listJSON = response.getJSONObject(i);
                List list = List.fromJSON(listJSON);
                if(list!=null) {
                    tweetList.add(list);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tweetList;
    }

    public static List fromJSON(JSONObject jsonObject) {
        List list = new List();
        try {
            list.id = jsonObject.getLong("id");
            list.name = jsonObject.getString("name");
            list.description = jsonObject.getString("description");
            list.mode = jsonObject.getString("mode");
        }catch(JSONException e) {
            e.printStackTrace();
        }

        return list;
    }
}
