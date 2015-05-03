package com.example.kushagar.iiitd_lostn_found;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by Kushagar on 5/3/2015.
 */
public interface MailTestApi {
    @POST("/sendmail/lost")
    public void send(@Body UserMail user , Callback<String> cb);
}
