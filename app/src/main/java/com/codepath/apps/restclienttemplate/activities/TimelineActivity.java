package com.codepath.apps.restclienttemplate.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.codepath.apps.restclienttemplate.EndlessRecyclerViewScrollListener;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.adapters.TweetsAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {

    TwitterClient client;
    RecyclerView timelineRv;
    SwipeRefreshLayout swipeContainer;
    TweetsAdapter adapter;
    Toolbar toolbar;
    List<Tweet> tweets;
    ProgressBar miActionProgressItem;
    EndlessRecyclerViewScrollListener scrollListener;

    public static final int PUBLISH_TWEET_REQ = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // create an instance of the current Twitter client
        client = TwitterApp.getRestClient(this);

        // Add functionality when user click on an item on toolbar (e.g: compose)
        toolbar = findViewById(R.id.timelineBar);
        toolbarOptionsSelected();

        // Look up the progress bar in view
        miActionProgressItem = findViewById(R.id.indeterminateBar);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // when user pull to refresh, update the timeline
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
        // set divide lines to recycler view
        createViewDivider();
        // create a new adapter
        adapter = new TweetsAdapter(tweets, getApplicationContext());
        // set adapter to RV to display items
        timelineRv.setAdapter(adapter);
        // initialize a new linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // set layout for recycler view
        timelineRv.setLayoutManager(layoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi();
            }
        };

        // Adds the scroll listener to recycler view
        timelineRv.addOnScrollListener(scrollListener);

        displayHomeTimeline();
    }

    // Display the home timeline when user load up the app
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
                    // signal loading has finished
                    miActionProgressItem.setVisibility(View.INVISIBLE);
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

    // When user click an option in the toolbar, execute the correct function based on id
    private void toolbarOptionsSelected() {
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.compose) {
                    startComposeActivity();
                }
                return true;
            }
        });
    }

    // goes to compose activity when user clicks compose icon
    private void startComposeActivity() {
        Intent intent = new Intent(this, ComposeActivity.class);
        // assign a request code so we get the correct data back
        startActivityForResult(intent, PUBLISH_TWEET_REQ);
    }

    // get back the data from the compose activity to manually populate recyclerview
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // retrieve the data the match our request code when we start activity
        if (requestCode == PUBLISH_TWEET_REQ && resultCode == RESULT_OK) {
            assert data != null;
            // unwrap the tweet object that was returned
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));
            // add this tweet at the top of our list
            tweets.add(0, tweet);
            // notify adapter to update recyclerview
            adapter.notifyDataSetChanged();
            // scroll to the top automatically
            timelineRv.smoothScrollToPosition(0);
        }
    }

    // create a divider for each item in the recyclerview
    private void createViewDivider() {
        // create a divider decoration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        // set drawable border as the divider
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.border));
        // add dividers to timeline recycler view
        timelineRv.addItemDecoration(dividerItemDecoration);
    }

    // Load data from the next page of tweets for endless scrolling
    public void loadNextDataFromApi() {
        // Send an API request to retrieve appropriate paginated data
        client.getNextPageOfTweets(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                // Deserialize and construct new model objects from the API response
                JSONArray data = json.jsonArray;
                try {
                    // Append the new data objects to the existing set of items inside the array of items
                    adapter.addAll(Tweet.fromJsonArray(data));
                    // Notify the adapter of the new items made with `notifyItemRangeInserted()
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e("TimelineActivity", "onFailure fetching next page!");
            }
            // id of the last tweet in the list will be max id
            // we will load all the tweets whose id <= max id (so, older tweets)
        }, tweets.get(tweets.size() - 1).id);
    }
}