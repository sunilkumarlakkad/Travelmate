package com.example.sunillakkad.travelmate.application;

import android.app.Application;
import android.util.Log;

import com.example.sunillakkad.travelmate.utils.NetworkCallManager;

/**
 * Created by mm98568 on 9/15/16.
 */
public class TravelmateApp extends Application {
    private final String TAG = TravelmateApp.class.getSimpleName();

    private static TravelmateApp mInstance;
    private NetworkCallManager mNetworkCallManager;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        mNetworkCallManager = new NetworkCallManager();
        mNetworkCallManager.registerService();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d(TAG, "terminating app");

        if (mNetworkCallManager != null)
            mNetworkCallManager.unregisterService();
    }

    public static synchronized TravelmateApp getInstance() {
        return mInstance;
    }

}
