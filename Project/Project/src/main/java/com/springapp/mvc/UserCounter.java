package com.springapp.mvc;

/**
 * Created by Shayan on 01-05-2015.
 */
public class UserCounter {
    private static long counter=0;

    public static long getCounter() {
        return counter;
    }

    public static void setCounter(long counter) {
        UserCounter.counter = counter;
    }

    public static void increment()
    {
        UserCounter.counter= UserCounter.getCounter()+1;
    }
}
