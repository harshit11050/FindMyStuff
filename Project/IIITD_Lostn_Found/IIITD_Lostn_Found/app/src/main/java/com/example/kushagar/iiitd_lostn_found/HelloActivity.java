package com.example.kushagar.iiitd_lostn_found;

/*import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;*/

/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONException;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HelloActivity extends Activity {
    private static final String TAG = "PlayHelloActivity";
    private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";
    public static final String EXTRA_ACCOUNTNAME = "extra_accountname";


    static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
    static final int REQUEST_CODE_RECOVER_FROM_AUTH_ERROR = 1001;
    static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1002;

    private String mEmail;

    private Type requestType;

    public static String TYPE_KEY = "type_key";
    public static enum Type {FOREGROUND, BACKGROUND, BACKGROUND_WITH_SYNC}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        Bundle extras = getIntent().getExtras();
        requestType = Type.valueOf(extras.getString(TYPE_KEY));
        setTitle(getTitle() + " - " + requestType.name());
        if (extras.containsKey(EXTRA_ACCOUNTNAME)) {
            mEmail = extras.getString(EXTRA_ACCOUNTNAME);
            getTask(HelloActivity.this, mEmail, SCOPE).execute();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
            if (resultCode == RESULT_OK) {
                mEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                Log.d("email-id",mEmail);
                getUsername();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "You must pick an account", Toast.LENGTH_SHORT).show();
            }
        } else if ((requestCode == REQUEST_CODE_RECOVER_FROM_AUTH_ERROR ||
                requestCode == REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR)
                && resultCode == RESULT_OK) {
            handleAuthorizeResult(resultCode, data);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleAuthorizeResult(int resultCode, Intent data) {
        if (data == null) {
            show("Unknown error, click the button again");
            return;
        }
        if (resultCode == RESULT_OK) {
            Log.i(TAG, "Retrying");
            getTask(this, mEmail, SCOPE).execute();
            return;
        }
        if (resultCode == RESULT_CANCELED) {
            show("User rejected authorization.");
            return;
        }
        show("Unknown error, click the button again");
    }

    /** Called by button in the layout */
    public void greetTheUser(View view) {
        getUsername();
    }

    /** Attempt to get the user name. If the email address isn't known yet,
     * then call pickUserAccount() method so the user can pick an account.
     */
    private void getUsername() {
        if (mEmail == null) {
            pickUserAccount();
        } else {
            if (isDeviceOnline()) {
                getTask(HelloActivity.this, mEmail, SCOPE).execute();
            } else {
                Toast.makeText(this, "No network connection available", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /** Starts an activity in Google Play Services so the user can pick an account */
    private void pickUserAccount() {
        String[] accountTypes = new String[]{"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }

    /** Checks whether the device currently has a network connection */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }


    /**
     * This method is a hook for background threads and async tasks that need to update the UI.
     * It does this by launching a runnable under the UI thread.
     */
    public void show(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //run here the next activity
                //do post request to the server here and get the userid of the user
                UserObjectCache objectCache = UserObjectCache.getInstance();
                Log.d("cache",objectCache.getUser_cache().toString());
                //check if user_id is set in shared preferences if yes show the other activity.

                try {
                    getUserid(mEmail,objectCache.getUser_cache().getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
     }

    public void getUserid(String email,String name){
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Content-Type", "application/json");
                request.addHeader("Accept", "application/json");
            }
        };

        UserTestApi demoService = new RestAdapter.Builder()
                .setEndpoint("http://192.168.48.144:8080")
                .setRequestInterceptor(requestInterceptor).setLogLevel(RestAdapter.LogLevel.FULL).setLog(new RestAdapter.Log() {
                    public void log(String msg) {
                        Log.i("retrofit", msg);
                    }
                })
                .build()
                .create(UserTestApi.class);

        User user = new User();
        user.setEmail_id(email);
        user.setUsername(name);

        //Log.e("Putting fb event",event.getEvent_id());
        final String[] userid = new String[1];

        demoService.register(user, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.e("signup_sucess",response.toString());
                Log.e("sign_up",s);
                Toast.makeText(HelloActivity.this,"Successfully Signed Up!!",Toast.LENGTH_SHORT).show();

                userid[0] =s;

                SharedPreferences.Editor editor = getSharedPreferences("mypref",MODE_PRIVATE).edit();
                UserIdentification userIdentification = UserIdentification.getInstance();
                userIdentification.setUserid(Integer.parseInt(userid[0]));

                editor.putString("userid", userid[0]);
                editor.commit();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("signup_failure",error.toString());
            }
        });

        //Log.e("user_id",userid);

        //put this in shared preferences


        Intent intent = new Intent(getApplicationContext(),ApplicationDashBoard.class);
        startActivity(intent);

    }

    /**
     * This method is a hook for background threads and async tasks that need to provide the
     * user a response UI when an exception occurs.
     */
    public void handleException(final Exception e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (e instanceof GooglePlayServicesAvailabilityException) {
                    // The Google Play services APK is old, disabled, or not present.
                    // Show a dialog created by Google Play services that allows
                    // the user to update the APK
                    int statusCode = ((GooglePlayServicesAvailabilityException)e)
                            .getConnectionStatusCode();
                    Dialog dialog = GooglePlayServicesUtil.getErrorDialog(statusCode,
                            HelloActivity.this,
                            REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                    dialog.show();
                } else if (e instanceof UserRecoverableAuthException) {
                    // Unable to authenticate, such as when the user has not yet granted
                    // the app access to the account, but the user can fix this.
                    // Forward the user to an activity in Google Play services.
                    Intent intent = ((UserRecoverableAuthException)e).getIntent();
                    startActivityForResult(intent,
                            REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                }
            }
        });
    }

    /**
     * Note: This approach is for demo purposes only. Clients would normally not get tokens in the
     * background from a Foreground activity.
     */
    private AbstractGetNameTask getTask(
            HelloActivity activity, String email, String scope) {
        switch(requestType) {
            case BACKGROUND:
                return new GetNameInBackground(activity, email, scope);
            default:
                return new GetNameInBackground(activity, email, scope);
        }
    }
}