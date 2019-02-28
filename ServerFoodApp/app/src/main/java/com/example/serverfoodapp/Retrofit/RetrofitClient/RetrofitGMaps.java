package com.example.serverfoodapp.Retrofit.RetrofitClient;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitGMaps {
    private static Retrofit retrofit = null;

    public static Retrofit getRetrofit(String baseUrl){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                       .baseUrl(baseUrl)
                       .addConverterFactory(ScalarsConverterFactory.create())
                       .build();
        }
        return retrofit;
    }
}
