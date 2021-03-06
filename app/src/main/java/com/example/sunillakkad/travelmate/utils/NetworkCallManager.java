package com.example.sunillakkad.travelmate.utils;

import com.example.sunillakkad.travelmate.events.CheckInternetRequest;
import com.example.sunillakkad.travelmate.events.CheckInternetResult;
import com.example.sunillakkad.travelmate.model.TaxiFinderResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import rx.Observable;

/**
 * Created by mm98568 on 9/16/16.
 */
public class NetworkCallManager {
    private final static String TAG = NetworkCallManager.class.getSimpleName();

    public NetworkCallManager() {
    }

    public void registerService() {
        EventBus.getDefault().register(this);
    }

    public void unregisterService() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent(CheckInternetRequest checkInternetRequest) {
        EventBus.getDefault().post(new CheckInternetResult(NetworkConnectionUtils.checkInternet()));
    }

    public static Observable<TaxiFinderResponse> getTaxiFareRate(String api_key, String origin, String destination){
        return TheTaxiFareApi.getInstance().getRetrofitService().getTaxiFareRate(api_key, origin, destination);
    }
}
