package com.springapp.mvc;

/**
 * Created by Shayan on 02-05-2015.
 */
public interface UserDAO {

    void addUser(User user, int userid);
    void getCount(User user);
    int checkUser(User user);
}
