package com.springapp.mvc;

import java.util.List;

/**
 * Created by Shayan on 02-05-2015.
 */
public interface FoundItemDAO
{
    void create(FoundItem item, int foundItemID,int userid);
    List<FoundItem> getItemList(String query);
    int getUserID(FoundItem item);
    String getEmailID(int ID);
}
