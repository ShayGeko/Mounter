package com.example.mounter.directions;

import android.app.Application;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mounter.R;
import com.example.mounter.databinding.ActivityDirectionsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.maps.android.PolyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MyRideActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.mounter.databinding.ActivityDirectionsBinding binding = ActivityDirectionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this::onMapReady);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    public void onMapReady(GoogleMap googleMap){
        googleMap.setMinZoomPreference(10.0f);
        googleMap.setMaxZoomPreference(20.0f);
        // Add a marker in SFU and move the camera
        LatLng sfu_burnaby = new LatLng(49.276765, -122.917957);
        LatLng sfu_surrey =  new LatLng(49.188680, -122.839940);


        googleMap.addMarker(new MarkerOptions().position(sfu_burnaby).title("Marker on Burnaby Campus"));
        googleMap.addMarker(new MarkerOptions().position(sfu_surrey).title("Marker on Surrey Campus"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sfu_surrey));

        String directionMode = "driving";


        GetAndDisplayDirections(googleMap, sfu_burnaby, sfu_surrey, directionMode);
    }

    private void GetAndDisplayDirections(GoogleMap googleMap, LatLng source, LatLng destination, String directionMode) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getDirectionsUrl(destination, source, directionMode);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    JSONObject data;
                    try {
                        data = new JSONObject(response);
                        String encodedPoints = data.getJSONArray("routes")
                            .getJSONObject(0)
                            .getJSONObject("overview_polyline")
                            .getString("points");
                        List<LatLng> latlngs = PolyUtil.decode(encodedPoints);
                        googleMap.addPolyline(new PolylineOptions()
                            .addAll(latlngs)
                            .width(5F)
                            .color(Color.RED));
                    }
                     catch (Exception e) {
                        e.printStackTrace();
                    }
            }, error -> {

                View parentLayout = findViewById(android.R.id.content);
                Snackbar.make(parentLayout, "An error occurred", Snackbar.LENGTH_LONG)
                        .show();
            });

        queue.add(stringRequest);
    }

    String getDirectionsUrl(LatLng origin, LatLng destination, String directionMode){
        String originStr = "origin=" + origin.latitude + "," + origin.longitude;
        String destinationStr = "destination=" + destination.latitude + "," + destination.longitude;
        String mode = "mode=" + directionMode;
        String parameters = originStr + "&" + destinationStr + "&" + mode;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"+
                output+ "?" +
                parameters+"&key="+
                getString(R.string.google_maps_key);

        return url;
    }
    private String downloadUrl(LatLng origin, LatLng destination, String directionMode)  throws IOException {
        String strUrl = getDirectionsUrl(origin, destination, directionMode);
        return downloadUrl(strUrl);
    }
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            System.out.println(url);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            System.out.println("Downloaded URL: " + data);
            br.close();
        } catch (Exception e) {
            Log.d("mylog", "Exception downloading URL: " + e.toString());
        } finally {
            assert iStream != null;
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


}
