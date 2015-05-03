package com.springapp.mvc;

/**
 * Created by Shayan on 02-05-2015.
 */
public class FoundItemCounter
{
    private static long counter=0;

    public static long getCounter() {
        return counter;
    }

    public static void setCounter(long counter) {
        FoundItemCounter.counter = counter;
    }

    public static void increment()
    {
        FoundItemCounter.counter= FoundItemCounter.getCounter()+1;
    }
}
