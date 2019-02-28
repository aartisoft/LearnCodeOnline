package com.example.serverfoodapp.Retrofit.RetrofitApi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IGeoCoordinates {

    @GET("maps/api/geocode/json")
        Call<String> getGeoCode(@Query("address") String address, @Query("key") String key);

    @GET("maps/api/directions/json")
        Call<String> getDirections(@Query("origin") String origin, @Query("destination") String dest, @Query("key") String key);

}
