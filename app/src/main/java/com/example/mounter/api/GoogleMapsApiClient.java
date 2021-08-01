package com.example.mounter.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

// created by following tutorial from
// https://sinhasanket099.medium.com/calling-rest-apis-with-retrofit-and-mvvm-architecture-daf49fa2066a
public class GoogleMapsApiClient {
    private static final String base_url =
            "https://maps.googleapis.com";
    private static GoogleMapsApiClient instance;
    private Retrofit retrofit;

    private GoogleMapsApiClient(){
        retrofit = new Retrofit.Builder().baseUrl(base_url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static synchronized GoogleMapsApiClient getInstance(){
        if(instance == null){
            instance = new GoogleMapsApiClient();
        }

        return instance;
    }
    public GoogleMapsApi getApi(){
        return retrofit.create(GoogleMapsApi.class);
    }
}
