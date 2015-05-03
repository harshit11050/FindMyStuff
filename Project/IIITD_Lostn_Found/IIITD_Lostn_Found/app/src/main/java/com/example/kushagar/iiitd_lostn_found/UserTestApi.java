package com.example.kushagar.iiitd_lostn_found;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by Kushagar on 5/1/2015.
 */
public interface UserTestApi {
    @POST("/user/register")
    public void register(@Body User user , Callback<String> cb);
}
