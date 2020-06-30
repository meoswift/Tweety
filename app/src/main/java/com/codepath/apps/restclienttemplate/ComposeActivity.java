package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    TwitterClient client;
    EditText composer;
    Button tweetBtn;

    public static final int MAX_TWEET_LENGTH = 140;
    public static final String TAG = "ComposeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApp.getRestClient(this);
        composer = findViewById(R.id.composer);
        tweetBtn = findViewById(R.id.tweetBtn);

    }

    // Set click listener on button
    public void onTweetClicked(View view) {
        // Make API call to publish tweet to timeline
        String tweet = composer.getText().toString();
        validateTweet(tweet);

        client.publishTweet(tweet, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    // parse data from json format to Tweet object
                    Tweet tweet = Tweet.fromJson(json.jsonObject);
                    // start new intent send back a Tweet object to Timeline for display
                    Intent intent = new Intent();
                    // wrap Tweet object in extras
                    intent.putExtra("tweet", Parcels.wrap(tweet));
                    // set result to 20
                    setResult(RESULT_OK, intent);
                    // finish intent and return to Timeline
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response,
                                  Throwable throwable) {
                Log.e(TAG, response);
            }
        });

    }

    private void validateTweet(String tweet) {
        if (tweet.isEmpty()) {
            Toast.makeText(this, "Sorry! Your tweet cannot be empty", Toast.LENGTH_SHORT).show();
            tweetBtn.setClickable(false);
        }
        if (tweet.length() > MAX_TWEET_LENGTH)
            tweetBtn.setClickable(false);
    }

}