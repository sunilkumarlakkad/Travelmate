package com.example.sunillakkad.travelmate.utils;

import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ct37238 on 7/18/16.
 */
public class NetworkConnectionUtils {
    private static final String TAG = NetworkConnectionUtils.class.getSimpleName();
    private static final String MOBILE_CONNECTION_TEST_URL = "https://www.google.com/";

    public static boolean checkInternet() {

        boolean success = false;
        try {
            URL url = new URL(MOBILE_CONNECTION_TEST_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            URL connectedUrl = connection.getURL();
            String connectedPath = connectedUrl.getProtocol() + "://" + connectedUrl.getHost() + connectedUrl.getPath();
            success = MOBILE_CONNECTION_TEST_URL.equals(connectedPath);
            connection.disconnect();
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }

        if (!success) {
            Log.d(TAG, ">>>>>>>>>>>>>>>>          no internet connection                   <<<<<<<<<<<<<<<<<");
            Log.d(TAG, ">>>>>>>>>>>>>>>>          no internet connection                   <<<<<<<<<<<<<<<<<");
            Log.d(TAG, ">>>>>>>>>>>>>>>>          no internet connection                   <<<<<<<<<<<<<<<<<");
            Log.d(TAG, ">>>>>>>>>>>>>>>>          no internet connection                   <<<<<<<<<<<<<<<<<");
            Log.d(TAG, ">>>>>>>>>>>>>>>>          no internet connection                   <<<<<<<<<<<<<<<<<");
        }
        return success;
    }
}
