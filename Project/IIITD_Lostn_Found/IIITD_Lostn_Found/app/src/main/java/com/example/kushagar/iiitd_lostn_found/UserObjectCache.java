package com.example.kushagar.iiitd_lostn_found;

import org.json.JSONObject;

/**
 * Created by Kushagar on 5/1/2015.
 */
//singleton class to cache the response by google oAuth token
public class UserObjectCache {
    private static JSONObject user_cache;
    private static UserObjectCache ins = new UserObjectCache();

    private UserObjectCache(){

    }

    public static UserObjectCache getInstance(){
        return ins;
    }

    public JSONObject getUser_cache() {
        return user_cache;
    }

    public void setUser_cache(JSONObject user_cache) {
        this.user_cache = user_cache;
    }
}
