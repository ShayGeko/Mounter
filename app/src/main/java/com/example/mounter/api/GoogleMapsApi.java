package com.example.mounter.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleMapsApi {

    @GET("/maps/api/directions/json")
    Call<ResponseBody> getDirections(
            @Query("origin") String originLatLng,
            @Query("destination") String destinationLatLng,
            @Query("mode") String drivingMode,
            @Query("key") String ApiKey);
}
