package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.databinding.ActivityDetailsBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ActivityDetailsBinding binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        Tweet tweet = Parcels.unwrap(intent.getParcelableExtra("tweet"));

        binding.name.setText(tweet.user.name);
        binding.username.setText(tweet.user.username);
        binding.text.setText(tweet.text);

        // display media on tweet if there is any
        if (tweet.mediaUrl != null) {
            Glide.with(this)
                    .load(tweet.mediaUrl)
                    .transform(new RoundedCorners(45))
                    .into(binding.media);
        } else {
            binding.media.setImageResource(0);
        }

        Glide.with(this)
                .load(tweet.user.profileImageUrl)
                .circleCrop()
                .into(binding.profileImage);
    }
}