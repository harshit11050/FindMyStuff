package com.springapp.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * Created by Shayan on 02-05-2015.
 */

@Controller
@RequestMapping("/reportlost")
public class LostItemController
{
    LostItemDaoImpl lostItemDao = new LostItemDaoImpl();

    @RequestMapping(value = "/{userid}",method = {RequestMethod.POST,RequestMethod.HEAD} ,
            consumes = "application/json" , headers = "Accept=application/json, application/xml")
    public @ResponseBody String addLostItem(@PathVariable("userid") int userid,@RequestBody LostItem item)
    {
        System.out.println("in method");
        LostItemCounter.increment();
        lostItemDao.create(item, (int) FoundItemCounter.getCounter(),userid);

        return "success";
    }

    @RequestMapping(value = "/{userid}", method = {RequestMethod.GET,RequestMethod.HEAD} , produces = "application/json")
    public @ResponseBody LostItem register(@PathVariable("userid") int userid) {
        System.out.println("get1");
        LostItemDaoImpl jed = new LostItemDaoImpl();
        List<LostItem> items = jed.getItemList("select * from lost_item");
        for(LostItem item : items){
            System.out.println(item.getItem_name()+","+item.getItem_location());
        }
        return items.get(0);
    }

    /*@RequestMapping(value = "/{userid}",method = {RequestMethod.GET,RequestMethod.HEAD} , produces = "application/json")
    public @ResponseBody
    Collection<LostItem> getEvent(@PathVariable("userid") String userid)
    {
        System.out.println("get");
        LostItemDaoImpl jed = new LostItemDaoImpl();
        return jed.getItemList("select * from lost_item");
    }*/
}
