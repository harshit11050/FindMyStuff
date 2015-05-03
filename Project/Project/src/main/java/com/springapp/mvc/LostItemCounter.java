package com.springapp.mvc;

/**
 * Created by Shayan on 02-05-2015.
 */
public class LostItemCounter
{
    private static long counter=0;

    public static long getCounter() {
        return counter;
    }

    public static void setCounter(long counter) {
        LostItemCounter.counter = counter;
    }

    public static void increment()
    {
        LostItemCounter.counter= LostItemCounter.getCounter()+1;
    }
}
