package com.example.sunillakkad.travelmate.utils;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mm98568 on 9/19/16.
 */
public class TheTaxiFareApi {
    private static TheTaxiFareApi mTheTaxiFareApi;
    private Retrofit mRetrofit;
    private RetrofitService mRetrofitService;


    private TheTaxiFareApi() {
        createRetrofit();
    }

    static TheTaxiFareApi getInstance() {
        if (mTheTaxiFareApi == null) {
            mTheTaxiFareApi = new TheTaxiFareApi();
        }

        return mTheTaxiFareApi;
    }

    RetrofitService getRetrofitService() {
        if (mRetrofitService == null) {
            mRetrofitService = mRetrofit.create(RetrofitService.class);
        }
        return mRetrofitService;
    }

    private void createRetrofit() {
        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.create();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.TAXI_FARE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(rxAdapter)
                .build();
    }
}
