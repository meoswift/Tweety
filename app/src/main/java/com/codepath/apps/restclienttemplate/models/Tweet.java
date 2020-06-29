package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tweet {
    public String text;
    public String createdAt;
    public User user;

    public static Tweet fromJson(JSONObject data) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.text = data.getString("text");
        tweet.createdAt = data.getString("created_at");
        tweet.user = User.fromJson(data.getJSONObject("user"));

        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray data) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            Tweet tweet = fromJson((JSONObject) data.get(i));
            tweets.add(tweet);
        }

        return tweets;
    }
}
