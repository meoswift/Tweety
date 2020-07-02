package com.codepath.apps.restclienttemplate.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class User {
    public long id;
    public String name;
    public String username;
    public String profileImageUrl;

    public User() {};

    public static User fromJson(JSONObject data) throws JSONException {
        User user = new User();
        user.id = data.getLong("id");
        user.name = data.getString("name");
        user.username = "@" + data.getString("screen_name");
        user.profileImageUrl = data.getString("profile_image_url_https");

        return user;
    }
}
