package com.example.sunillakkad.travelmate.utils;

import com.example.sunillakkad.travelmate.model.TaxiFinderResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface RetrofitService {

    @GET("fare?")
    Observable<TaxiFinderResponse> getTaxiFareRate(
            @Query("key") String key,
            @Query("origin") String origin,
            @Query("destination") String destination);



}
