package com.springapp.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Created by Shayan on 02-05-2015.
 */

@Controller
@RequestMapping("/reportfound")
public class FoundItemController
{
    FoundItemDaoImpl foundItemDao = new FoundItemDaoImpl();

    @RequestMapping(value = "/{userid}",method = {RequestMethod.POST,RequestMethod.HEAD} ,
            consumes = "application/json" , headers = "Accept=application/json, application/xml")
    public @ResponseBody
    String addLostItem(@PathVariable("userid") int userid,@RequestBody FoundItem item)
    {
        System.out.println("in method");
        FoundItemCounter.increment();
        foundItemDao.create(item, (int) FoundItemCounter.getCounter(),userid);

        return "success";
    }

    @RequestMapping(value = "/{userid}",method = {RequestMethod.GET,RequestMethod.HEAD} , produces = "application/json")
    public @ResponseBody
    Collection<FoundItem> getEvent(@PathVariable("userid") String userid)
    {
        FoundItemDaoImpl jed = new FoundItemDaoImpl();
        return jed.getItemList("select * from found_item");
    }

}
