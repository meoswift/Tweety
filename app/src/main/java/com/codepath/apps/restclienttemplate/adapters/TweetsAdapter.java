package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.activities.DetailsActivity;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.activities.ReplyActivity;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    List<Tweet> tweetList;
    Context context;

    public TweetsAdapter(List<Tweet> tweetList, Context context) {
        this.tweetList = tweetList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetItem = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder tweetHolder = new ViewHolder(tweetItem);

        return tweetHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tweet tweet = tweetList.get(position);

        // Set all properties of a tweet in timeline
        holder.text.setText(tweet.text);
        holder.name.setText(tweet.user.name);
        holder.username.setText(tweet.user.username);
        holder.timeStamp.setText(tweet.timeStamp);

        // Display media on tweet if there is any
        if (tweet.mediaUrl != null) {
            Glide.with(context)
                    .load(tweet.mediaUrl)
                    .override(880,440)
                    .transform(new CenterCrop(),new RoundedCorners(25))
                    .into(holder.media);
        } else {
            holder.media.setImageResource(0);
        }

        // Set media for user profile image
        Glide.with(context)
                .load(tweet.user.profileImageUrl)
                .circleCrop()
                .into(holder.profileImage);

        // Change the color of view based on favorited or not
        if (tweet.favorited)
            holder.favBtn.setColorFilter(context.getResources().getColor(R.color.favorite));

        // Change the color of view based on retweeted or not
        if (tweet.retweeted)
            holder.rtwBtn.setColorFilter(context.getResources().getColor(R.color.retweet));
    }

    @Override
    public int getItemCount() {
        return tweetList.size();
    }

    // Provide a direct reference to each of the views within a data item (i.e: a tweet)
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView profileImage;
        public TextView name;
        public TextView username;
        public TextView text;
        public TextView timeStamp;
        public ImageView media;
        public ImageView favBtn;
        public ImageView rtwBtn;
        public ImageView replyBtn;

        // this represents one line of tweet
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            // views that will be rendered in a row
            profileImage = itemView.findViewById(R.id.profile_image);
            name = itemView.findViewById(R.id.name);
            username = itemView.findViewById(R.id.username);
            text = itemView.findViewById(R.id.text);
            timeStamp = itemView.findViewById(R.id.timeStamp);
            media = itemView.findViewById(R.id.media);
            favBtn = itemView.findViewById(R.id.favBtn);
            rtwBtn = itemView.findViewById(R.id.retweetBtn);
            replyBtn = itemView.findViewById(R.id.replyBtn);

            itemView.setOnClickListener(this);

            replyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Tweet tweet = tweetList.get(position);
                    Intent intent = new Intent(context, ReplyActivity.class);
                    intent.putExtra("tweet", Parcels.wrap(tweet));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

        }

        @Override
        public void onClick(View v) {
            // get position of item that was clicked
            int position = getAdapterPosition();

            // make sure position is valid
            if (position != RecyclerView.NO_POSITION) {
                // get object in tweets list that will be pass into new activity
                Tweet tweet = tweetList.get(position);
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("tweet", Parcels.wrap(tweet));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }

    }

    public void clear() {
        tweetList.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweetList.addAll(list);
        notifyDataSetChanged();
    }
}
