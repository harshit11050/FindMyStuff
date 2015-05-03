package com.example.kushagar.iiitd_lostn_found;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.liveo.interfaces.NavigationLiveoListener;
import br.liveo.navigationliveo.NavigationLiveo;


public class ApplicationDashBoard extends NavigationLiveo implements NavigationLiveoListener {

    UserIdentification userIdentification = UserIdentification.getInstance();
    public List<String> mListNameItem;
    String url ="http://192.168.48.144:8080/reportlost/"+userIdentification.getUserid();
    int id;
    FragmentManager mFragmentManager = getSupportFragmentManager();
    Fragment mFragment = null;
    ArrayList<LostItem> e;

    private class ProgressTask extends AsyncTask<String , Void , Boolean> {
        private ProgressDialog dialog;
        private Context context;
        private Activity activity ;

        public ProgressTask(Activity a){
            this.activity =a;
            context = a;
            dialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Started>.......");
            this.dialog.show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(dialog.isShowing()){
                this.dialog.dismiss();
            }
            if (mFragment != null){
                mFragmentManager.beginTransaction().replace(id, mFragment).commit();
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            MyJsonParser jsonParser = new MyJsonParser();
            JSONArray jsonArray = jsonParser.getJSONFromUrl(url);
            Log.e("events_net",jsonArray.toString());

            e = new ArrayList<LostItem>();

            //Log.e("Json_array_response",jsonArray.toString());
            for(int i=0;i<jsonArray.length();i++){
                try{
                    LostItem sevent = new LostItem();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    sevent.setItem_name(jsonObject.getString("item_name"));
                    sevent.setItem_location(jsonObject.getString("item_location"));
                    sevent.setItem_details(jsonObject.getString("item_details"));
                    sevent.setDate(jsonObject.getString("date"));

                    Log.e("e_desp",sevent.getItem_name());
                    e.add(sevent);

                }catch(Exception e){
                    Log.d("exception", e.toString());
                    e.printStackTrace();
                }
            }

            SendEvent.setArrayList(e);

            //dash board left
            mFragment = new DashboardFragment().newinstance("all_events",e);

            return null;
        }
    }

    @Override
    public void onUserInformation() {
        UserObjectCache objectCache = UserObjectCache.getInstance();
        String username="kushagar";

        try {
            username = objectCache.getUser_cache().getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.mUserName.setText(username);
        this.mUserEmail.setText("kushagar11061@iiitd.ac.in");
        this.mUserPhoto.setImageResource(R.drawable.ic_no_user);
        this.mUserBackground.setImageResource(R.drawable.ic_user_background);
    }

    @Override
    public void onInt(Bundle bundle) {
        this.setNavigationListener(this);

        //First item of the position selected from the list
        this.setDefaultStartPositionNavigation(1);

        // name of the list items
        mListNameItem = new ArrayList<>();
        mListNameItem.add(0, "All Items");
        mListNameItem.add(1, "Lost Items");
        mListNameItem.add(2, "Report Lost Item");
        mListNameItem.add(3, "Report Found Item Item");

        // icons list items
        List<Integer> mListIconItem = new ArrayList<>();
        mListIconItem.add(0, R.drawable.ic_send_black_24dp);
        mListIconItem.add(1, R.drawable.ic_send_black_24dp); //Item no icon set 0
        mListIconItem.add(2, R.drawable.ic_send_black_24dp);
        mListIconItem.add(3, R.drawable.ic_send_black_24dp);

        //{optional} - Among the names there is some subheader, you must indicate it here
        List<Integer> mListHeaderItem = new ArrayList<>();
        mListHeaderItem.add(4);

        //{optional} - Among the names there is any item counter, you must indicate it (position) and the value here
        SparseIntArray mSparseCounterItem = new SparseIntArray(); //indicate all items that have a counter
        mSparseCounterItem.put(0, 7);
        mSparseCounterItem.put(1, 123);
        mSparseCounterItem.put(6, 250);

        //If not please use the FooterDrawer use the setFooterVisible(boolean visible) method with value false
        this.setFooterInformationDrawer("Log-out", R.drawable.ic_delete_black_24dp);

        this.setNavigationAdapter(mListNameItem, mListIconItem, mListHeaderItem, mSparseCounterItem);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onItemClickNavigation(int position, int layoutContainerId) {

        id = layoutContainerId;

        switch (position){
            case 0:
                //all lost-items in here
                url = "http://192.168.48.144:8080/reportlost/"+userIdentification.getUserid();
                new ProgressTask(ApplicationDashBoard.this).execute();
                //mFragment = new DashboardFragment().newinstance("all_evenets",);
                //Log.e("on click", "on 0 clicked");
                break;
            case 1:
                //mFragment = new FragmentMain().newInstance(mListNameItem.get(position));
                //all the current logged in user lost items here
                url = "http://192.168.48.144:8080/reportlost/"+userIdentification.getUserid();
                new ProgressTask(ApplicationDashBoard.this).execute();
                Log.e("on click", "on 1 clicked");
                break;
            case 2:
                //ArrayList<Event>event= new ArrayList<>();
                //mFragment = new FragmentMain().newInstance1(mListNameItem.get(position),event);
                mFragment = new ReportLostItem();
                url = "http://192.168.50.241:8084/fbevent";
               // new ProgressTask(ApplicationDashBoard.this).execute();
                Log.e("on click","on 2 clicked");
                break;
            case 3:
                mFragment = new ReportFoundItem();
                Log.e("on click","on 3 clicked");
                break;
        }

        if (mFragment != null){
            mFragmentManager.beginTransaction().replace(layoutContainerId, mFragment).commit();
        }
    }

    @Override
    public void onPrepareOptionsMenuNavigation(Menu menu, int i, boolean b) {

    }

    @Override
    public void onClickFooterItemNavigation(View view) {

    }

    @Override
    public void onClickUserPhotoNavigation(View view) {

    }
}
