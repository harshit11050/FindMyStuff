package com.springapp.mvc;

import java.util.List;

/**
 * Created by Shayan on 27-04-2015.
 */
public interface LostItemDAO
{
    void create(LostItem item, int lostItemID,int userid);
    List<LostItem> getItemList(String query);
    int getUserID(LostItem item);
    String getEmailID(int ID);
}
