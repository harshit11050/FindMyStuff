package com.example.kushagar.iiitd_lostn_found;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cengalabs.flatui.views.FlatButton;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by Kushagar on 4/16/2015.
 */
public class EventFragment extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_fragment);
        final LostItem e = SendEvent.getMyevent();

        TextView event_view = (TextView)findViewById(R.id.eventtextview);
        String res = "Item Name :"+e.getItem_name() + "\n"+"Deatils:"+"\n" + e.getItem_details()+"\n"+"Date:"+e.getDate() +"  Location:"+e.getItem_location();
        event_view.setText(res);
        FlatButton button = (FlatButton) findViewById(R.id.report_mail);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //http://192.168.50.241:8084/knock/1?regId=&location=IIIT-DELHI
                UserMail ins = new UserMail();
                ins.setItem(e);
                UserIdentification userIdentification = UserIdentification.getInstance();
                ins.setUserid(Integer.toString(userIdentification.getUserid()));
                sendMail(ins);
            }
        });
    }

    public void sendMail(UserMail item){
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Content-Type", "application/json");
                request.addHeader("Accept", "application/json");
            }
        };
        MailTestApi demoService = new RestAdapter.Builder()
                .setEndpoint("http://192.168.48.144:8080")
                .setRequestInterceptor(requestInterceptor).setLogLevel(RestAdapter.LogLevel.FULL).setLog(new RestAdapter.Log() {
                    public void log(String msg) {
                        Log.i("retrofit", msg);
                    }
                })
                .build()
                .create(MailTestApi.class);
        //read-userid here from shared preferences


        UserIdentification userIdentification = UserIdentification.getInstance();

        Log.d("userlostid",userIdentification.getUserid()+"");

        demoService.send(item,new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.e("Success", response.toString());
                Toast.makeText(EventFragment.this,"Item Reported",Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Failure", error.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
