package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.w3c.dom.Text;

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

        holder.text.setText(tweet.text);
        holder.name.setText(tweet.user.name);
        holder.username.setText(tweet.user.username);
        holder.timeStamp.setText(tweet.timeStamp);


        if (tweet.mediaUrl != null) {
            Log.d("TweetsAdapter", tweet.mediaUrl);

            Glide.with(context)
                    .load(tweet.mediaUrl)
                    .
                    .into(holder.media);
        } else {
            holder.media.setImageResource(0);
        }

        Glide.with(context)
                .load(tweet.user.profileImageUrl)
                .circleCrop()
                .into(holder.profileImage);
    }

    @Override
    public int getItemCount() {
        return tweetList.size();
    }

    // Provide a direct reference to each of the views within a data item (i.e: a tweet)
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView profileImage;
        public TextView name;
        public TextView username;
        public TextView text;
        public TextView timeStamp;
        public ImageView media;

        // this represents one line of tweet
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // views that will be rendered in a row
            profileImage = itemView.findViewById(R.id.profile_image);
            name = itemView.findViewById(R.id.name);
            username = itemView.findViewById(R.id.username);
            text = itemView.findViewById(R.id.text);
            timeStamp = itemView.findViewById(R.id.timeStamp);
            media = itemView.findViewById(R.id.media);
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
