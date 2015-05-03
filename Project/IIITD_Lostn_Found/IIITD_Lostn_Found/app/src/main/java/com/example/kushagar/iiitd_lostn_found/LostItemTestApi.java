package com.example.kushagar.iiitd_lostn_found;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Kushagar on 5/2/2015.
 */
public interface LostItemTestApi {
    @POST("/reportlost/{userid}")
    public void reportlost(@Path("userid") int userid,@Body LostItem lostItem , Callback<String> cb);
}
