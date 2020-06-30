package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class ComposeActivity extends AppCompatActivity {

    EditText composer;
    Button tweetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        composer = findViewById(R.id.composer);
        tweetBtn = findViewById(R.id.tweetBtn);

        // Set click listener on button

        // Make API call to publish tweet to timeline
    }


}