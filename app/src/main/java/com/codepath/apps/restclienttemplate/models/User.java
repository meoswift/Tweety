package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    public String name;
    public String username;
    public String profileImage;

    public static User fromJson(JSONObject data) throws JSONException {
        User user = new User();
        user.name = data.getString("name");
        user.username = data.getString("screen_name");
        user.profileImage = data.getString("profile_image_url_https");

        return user;
    }
}
