package com.example.kushagar.iiitd_lostn_found;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Kushagar on 5/1/2015.
 */
public class MyFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        TextView text = new TextView(this.getActivity());
        text.setText("Section");
        text.setGravity(Gravity.CENTER);
        return text;
    }
}
