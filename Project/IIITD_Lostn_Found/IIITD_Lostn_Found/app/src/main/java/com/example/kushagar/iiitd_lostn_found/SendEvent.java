package com.example.kushagar.iiitd_lostn_found;

import java.util.ArrayList;

/**
 * Created by Kushagar on 5/3/2015.
 */
public class SendEvent {
    private static ArrayList<LostItem>arrayList;
    private static LostItem myevent;
    private static int layout_id;

    public static ArrayList<LostItem> getArrayList() {
        return arrayList;
    }

    public static void setArrayList(ArrayList<LostItem> arrayList) {
        SendEvent.arrayList = arrayList;
    }

    public static LostItem getMyevent() {
        return myevent;
    }

    public static void setMyevent(LostItem myevent) {
        SendEvent.myevent = myevent;
    }

    public static int getLayout_id() {
        return layout_id;
    }

    public static void setLayout_id(int layout_id) {
        SendEvent.layout_id = layout_id;
    }
}
