package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    TwitterClient client;
    EditText composer;
    Button tweetBtn;
    TextInputLayout composerLayout;

    public static final int MAX_TWEET_LENGTH = 140;
    public static final String TAG = "ComposeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApp.getRestClient(this);
        composer = findViewById(R.id.composer);
        tweetBtn = findViewById(R.id.tweetBtn);
        composerLayout = findViewById(R.id.composerLayout);

        validateTweet();
    }

    // Set click listener on button
    public void onTweetClicked(View view) {
        // Make API call to publish tweet to timeline
        String tweet = composer.getText().toString();

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

    private void validateTweet() {
        composerLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence tweet, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence tweet, int i, int i1, int i2) {
                if (tweet.length() == 0)
                    tweetBtn.setEnabled(false);

                if (tweet.length() > MAX_TWEET_LENGTH)
                    tweetBtn.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    public void cancelCompose(View view) {
        // maybe pop up a dialog confirming if user want to cancel composing
        finish();
    }

}