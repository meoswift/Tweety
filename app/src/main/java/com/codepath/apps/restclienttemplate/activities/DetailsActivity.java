package com.codepath.apps.restclienttemplate.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.databinding.ActivityDetailsBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.appbar.MaterialToolbar;

import org.parceler.Parcels;

import okhttp3.Headers;

public class DetailsActivity extends AppCompatActivity {

    TwitterClient client;
    ActivityDetailsBinding binding;
    Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // create an instance of the current Twitter client
        client = TwitterApp.getRestClient(this);

        // Apply view binding to reduce code boilerplate
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Get Tweet object that was passed from previous activity
        Intent intent = getIntent();
        tweet = Parcels.unwrap(intent.getParcelableExtra("tweet"));

        // Set view properties of a tweet
        binding.name.setText(tweet.user.name);
        binding.username.setText(tweet.user.username);
        binding.text.setText(tweet.text);

        // Display media on tweet if there is any
        if (tweet.mediaUrl != null) {
            Glide.with(this)
                    .load(tweet.mediaUrl)
                    .transform(new RoundedCorners(45))
                    .into(binding.media);
        } else {
            binding.media.setImageResource(0);
        }

        // Display the user profile image
        Glide.with(this)
                .load(tweet.user.profileImageUrl)
                .circleCrop()
                .into(binding.profileImage);
    }

    // When user clicks on favorite icon, fetch the API to favorite that tweet
    public void onFavoriteTweet(View view) {
        client.favoriteTweet(tweet.id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                // Change the color of icon
                binding.favBtn.setColorFilter(getResources().getColor(R.color.favorite));
                // Update tweet's favorited property
                tweet.favorited = true;
            }

            // On failure happens when user try to click on an already favorited tweet.
            // In this case, unfavorite the tweet
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d("DetailsActivity", "onFailure favorite tweet!" + response);
                unfavoriteTweet(tweet.id);
            }
        });
    }

    // When user clicks on the favorite icon twice
    private void unfavoriteTweet(long id) {
        client.unfavoriteTweet(id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                // change the color of view and property of Tweet object
                binding.favBtn.setColorFilter(getResources().getColor(R.color.normal));
                tweet.favorited = false;
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d("DetailsActivity", "onFailure unfavorite tweet!" + response);
            }
        });
    }

    // When user clicks on retweet icon, fetch the API to retweet that tweet
    public void onRetweet(View view) {
        client.retweetTweet(tweet.id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                // Change color of view and set property of Tweet object
                binding.retweetBtn.setColorFilter(getResources().getColor(R.color.retweet));
                tweet.retweeted = true;
            }

            // On failure happens when user try to click on an already retweeted tweet.
            // In this case, unretweet the tweet
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d("DetailsActivity", "onFailure retweet!" + response);
                unretweet(tweet.id);
            }
        });
    }

    // When user clicks on the retweet icon twice
    private void unretweet (long id) {
        client.unretweetTweet(id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                // change the color of view and property of Tweet object
                binding.retweetBtn.setColorFilter(getResources().getColor(R.color.normal));
                tweet.retweeted = false;
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d("DetailsActivity", "onFailure unretweet tweet!" + response);
            }
        });
    }
}