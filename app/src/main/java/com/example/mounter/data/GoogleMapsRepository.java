package com.example.mounter.data;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.mounter.BuildConfig;
import com.example.mounter.api.GoogleMapsApiClient;
import com.example.mounter.data.model.Directions;
import com.example.mounter.data.model.OverviewPolyline;
import com.example.mounter.data.model.Route;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleMapsRepository {
    private static final String TAG = "GoogleMapsRepository";
    private final MutableLiveData<Directions> directions;

    public GoogleMapsRepository(){
        directions = new MutableLiveData<>();
    }

    public MutableLiveData<Directions> getDirections(String mode, String destination, String origin){
        Call<ResponseBody> call = GoogleMapsApiClient.getInstance()
                .getApi()
                .getDirections(origin, destination, mode, BuildConfig.GOOGLE_MAPS_API_KEY);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, response.toString());
                Log.d(TAG, response.message());
                Log.d(TAG, response.body().toString());
                if(response.isSuccessful()){
                    try{
                        directions.setValue(parseDirections(response));
                    }
                    catch(Exception e){
                        Log.e(TAG, "error parsing directions response:");
                        Log.e(TAG, e.getMessage());

                        directions.setValue(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "getDirections response failed");
                Log.e(TAG, t.getMessage());
                directions.postValue(null);
            }
        });

        return directions;
    }

    private Directions parseDirections(Response<ResponseBody> response) throws JSONException, IOException {
        Directions directions = new Directions();
        JSONObject dataObj = new JSONObject(response.body().string());
        Log.d(TAG, dataObj.toString());

        directions.setGeocodedWaypoints(dataObj.getJSONArray("geocoded_waypoints"));

        JSONObject routeObj = dataObj.getJSONArray("routes").getJSONObject(0);

        Route route = new Route();
        route.setSummary(routeObj.getString("summary"));
        route.setLegs(routeObj.getJSONArray("legs"));
        route.setCopyrights(routeObj.getString("copyrights"));

        route.setOverviewPolyline(new OverviewPolyline(
                routeObj.getJSONObject("overview_polyline")
                        .getString("points")));
        route.setWarnings(routeObj.getJSONArray("warnings"));
        route.setWaypointOrder(routeObj.getJSONArray("waypoint_order"));

        JSONObject boundsObj = routeObj.getJSONObject("bounds");
        LatLng southwest = new LatLng(
                boundsObj.getJSONObject("southwest").getDouble("lat"),
                boundsObj.getJSONObject("southwest").getDouble("lng")
        );
        LatLng northeast = new LatLng(
                boundsObj.getJSONObject("northeast").getDouble("lat"),
                boundsObj.getJSONObject("northeast").getDouble("lng")
        );
        route.setBounds(new LatLngBounds(southwest, northeast));

        directions.setRoute(route);
        directions.setLoaded();
        
        return directions;
    }
}
