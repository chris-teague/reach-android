package com.turbolinks.app;

/**
 * Created by christeague on 13/7/17.
 */

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("token")
    public String token;

    @SerializedName("id")
    public String id;

    public User(String id, String token) {
        this.id = id;
        this.token = token;
    }
}
