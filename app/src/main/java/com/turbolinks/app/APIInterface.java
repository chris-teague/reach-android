package com.turbolinks.app;

/**
 * Created by christeague on 13/7/17.
 */

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Path;

interface APIInterface {
    @FormUrlEncoded
    @POST("/users.json")
    Call<User> createUser(@Field("user[client]") String client);

    @FormUrlEncoded
    @POST("/users/{id}.json")
    Call<User> updateUser(@Path("id") String userID, @Field("_method") String method, @Field("user[lat]") String lat, @Field("user[lng]") String lng);
}
