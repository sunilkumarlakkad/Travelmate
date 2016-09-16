package com.example.sunillakkad.travelmate.events;

/**
 * Created by ct37238 on 7/18/16.
 */
public class CheckInternetResult {
    boolean hasInternet;

    public boolean hasInternet() {
        return hasInternet;
    }
    public boolean noInternet() {
        return !hasInternet;
    }

    public CheckInternetResult(boolean hasInternet) {

        this.hasInternet = hasInternet;
    }
}
