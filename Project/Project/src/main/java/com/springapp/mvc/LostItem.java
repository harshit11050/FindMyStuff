package com.springapp.mvc;

import java.sql.Date;
/**
 * Created by Shayan on 26-04-2015.
 */

public class LostItem {
    private String item_name;
    private String item_location;
    private String item_details;
    private String date;


    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_location() {
        return item_location;
    }

    public void setItem_location(String item_location) {
        this.item_location = item_location;
    }

    public String getItem_details() {
        return item_details;
    }

    public void setItem_details(String item_details) {
        this.item_details = item_details;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}