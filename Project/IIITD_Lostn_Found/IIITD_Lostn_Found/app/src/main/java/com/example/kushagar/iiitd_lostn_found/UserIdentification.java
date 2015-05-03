package com.example.kushagar.iiitd_lostn_found;

/**
 * Created by Kushagar on 5/2/2015.
 */
public class UserIdentification {
    private static int userid;
    private static UserIdentification ins = new UserIdentification();

    private UserIdentification(){

    }

    public static UserIdentification getInstance(){
        return  ins;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}
