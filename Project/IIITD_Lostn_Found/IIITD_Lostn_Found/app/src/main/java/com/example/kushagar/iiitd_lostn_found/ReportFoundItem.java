package com.example.kushagar.iiitd_lostn_found;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.cengalabs.flatui.views.FlatButton;
import com.cengalabs.flatui.views.FlatEditText;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ReportFoundItem extends Fragment {
    DatePicker picker;
    String date;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View rootView = inflater.inflate(R.layout.fragment_report_lost_item, container, false);
        final FlatEditText item_name = (FlatEditText) rootView.findViewById(R.id.itemname);
        final FlatEditText item_location = (FlatEditText) rootView.findViewById(R.id.itemlocation);
        final FlatEditText item_details = (FlatEditText) rootView.findViewById(R.id.itemdetails);
        FlatButton pickdate = (FlatButton)rootView.findViewById(R.id.button_pickdate);
        FlatButton report = (FlatButton)rootView.findViewById(R.id.ReportLost);
        picker = (DatePicker)rootView.findViewById(R.id.datePicker1);

        item_name.setHint("Object Found");
        item_details.setHint("Describe the item Found Explicitly");
        pickdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date =getCurrentDate();
                Log.d("chosen_date",date);
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = getCurrentDate();
                FoundItem item = new FoundItem();
                item.setDate(date);
                item.setItem_details(item_details.getText().toString());
                item.setItem_location(item_location.getText().toString());
                item.setItem_name(item_name.getText().toString());
                //now send this to the server
                report(item);
            }
        });

        return rootView;
    }


    public void report(FoundItem item){
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Content-Type", "application/json");
                request.addHeader("Accept", "application/json");
            }
        };
        FoundItemTestApi demoService = new RestAdapter.Builder()
                .setEndpoint("http://192.168.48.144:8080")
                .setRequestInterceptor(requestInterceptor).setLogLevel(RestAdapter.LogLevel.FULL).setLog(new RestAdapter.Log() {
                    public void log(String msg) {
                        Log.i("retrofit", msg);
                    }
                })
                .build()
                .create(FoundItemTestApi.class);
        //read-userid here from shared preferences


        UserIdentification userIdentification = UserIdentification.getInstance();

        Log.d("userlostid",userIdentification.getUserid()+"");
        demoService.reportfound(userIdentification.getUserid(), item, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.e("Success", response.toString());
                Toast.makeText(getActivity(), "Successfully Repoted!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Failure", error.toString());
            }

        });
    }

    public String getCurrentDate(){
        StringBuilder builder=new StringBuilder();
        //builder.append("");
        builder.append((picker.getMonth() + 1)+"/");//month is 0 based
        builder.append(picker.getDayOfMonth()+"/");
        builder.append(picker.getYear());
        return builder.toString();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_hello, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub

        switch (item.getItemId()) {

        }
        return true;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
