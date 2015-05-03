package com.springapp.mvc;

/**
 * Created by Shayan on 01-05-2015.
 */
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/user/register")
public class UserController
{
    int res;
    @RequestMapping(method = {RequestMethod.POST,RequestMethod.HEAD},consumes = "application/json" , headers = "Accept=application/json, application/xml")

    public @ResponseBody String register(@RequestBody User user)
    {
        System.out.println("in user_reg.");
        UserDAOImpl userDAO = new UserDAOImpl();
        res = userDAO.checkUser(user);
        //System.out.println("Executed");
        if(res!=0)
        {
            return res +"";
        }
        else {
            System.out.println("Executed not yet");
            userDAO.getCount(user);
            //userDAO.checkUser(user);
            UserCounter.increment();
            userDAO.addUser(user, (int) UserCounter.getCounter());

            System.out.println(UserCounter.getCounter());

            return UserCounter.getCounter() + "";
        }
    }

    @RequestMapping(method = {RequestMethod.GET,RequestMethod.HEAD} ,produces = "application/json")
    public @ResponseBody Collection<LostItem> register() {
        System.out.println("get1");
        LostItemDaoImpl jed = new LostItemDaoImpl();
        List<LostItem> items = jed.getItemList("select * from lost_item");
        for(LostItem item : items){
            System.out.println(item.getItem_name()+","+item.getItem_location());
        }
        return items;
    }

}
