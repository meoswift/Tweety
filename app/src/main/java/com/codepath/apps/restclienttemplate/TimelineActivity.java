package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.restclienttemplate.adapters.TweetsAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {

    TwitterClient client;
    RecyclerView timelineRv;
    SwipeRefreshLayout swipeContainer;
    TweetsAdapter adapter;
    List<Tweet> tweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        getSupportActionBar().setTitle("Timeline");


        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                displayHomeTimeline();
            }
        });

        // find recycler view in layout
        timelineRv = findViewById(R.id.timeline);
        // initialize tweets so we don't pass null into adapter
        tweets = new ArrayList<>();
        // create a new adapter
        adapter = new TweetsAdapter(tweets, getApplicationContext());
        // set adapter to RV to display items
        timelineRv.setAdapter(adapter);
        // set layout as linear
        timelineRv.setLayoutManager(new LinearLayoutManager(this));

        // create an instance of the current Twitter client
        client = TwitterApp.getRestClient(this);
        displayHomeTimeline();
    }

    private void displayHomeTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONArray data = json.jsonArray;
                try {
                    // clear out old items before fetching new ones on refresh
                    adapter.clear();
                    // add all tweets to adapter to display to view
                    adapter.addAll(Tweet.fromJsonArray(data));
                    // signal refresh has finished
                    swipeContainer.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response,
                                  Throwable throwable) {
                Log.e("TimelineActivity", "Error fetching home timeline!" + response);
            }
        });
    }
}