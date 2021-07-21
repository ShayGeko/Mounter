package com.example.mounter.directions;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mounter.R;
import com.example.mounter.data.model.RidePostingModel;
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

import org.bson.types.ObjectId;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import io.realm.Realm;
import io.realm.mongodb.User;

import static com.example.mounter.Mounter.mounter;

public class MyRideActivity extends AppCompatActivity implements OnMapReadyCallback {
    private User user;
    private Realm mRealm;
    private RidePostingModel ridePosting;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.mounter.databinding.ActivityDirectionsBinding binding = ActivityDirectionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mRealm = Realm.getDefaultInstance();
        user = mounter.currentUser();

        ObjectId rideId = (ObjectId) getIntent().getSerializableExtra("ridePostingId");
        ridePosting = mRealm.where(RidePostingModel.class).equalTo("_id", rideId).findFirst();

        TextView originAddressTextView = findViewById(R.id.ride_details_origin_address_textView);
        TextView destinationAddressTextView = findViewById(R.id.ride_details_destination_address_textView);
        TextView departureTimeTextView = findViewById(R.id.ride_details_departure_time);

        originAddressTextView.append(ridePosting.getOriginAddress());
        destinationAddressTextView.append(ridePosting.getDestinationAddress());
        departureTimeTextView.append(ridePosting.getDepartureTime());


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this::onMapReady);
    }

    @Override
    public void onDestroy(){
        mRealm.close();
        super.onDestroy();
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
        LatLng originLatLng = ridePosting.getOriginActualLatLng();
        LatLng destinationLatLng = ridePosting.getDestinationActualLatLng();

        // SFU Burnaby as default
        if(originLatLng == null) originLatLng = new LatLng(49.276765, -122.917957);
        // SFU Surrey as default
        if(destinationLatLng == null) destinationLatLng = new LatLng(49.188680, -122.839940);

        googleMap.addMarker(new MarkerOptions().position(originLatLng).title("Marker on Burnaby Campus"));
        googleMap.addMarker(new MarkerOptions().position(destinationLatLng).title("Marker on Surrey Campus"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(destinationLatLng));

        String directionMode = "driving";


        GetAndDisplayDirections(googleMap, originLatLng, destinationLatLng, directionMode);
    }

    private void GetAndDisplayDirections(GoogleMap googleMap, LatLng source, LatLng destination, String directionMode) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getDirectionsUrl(destination, source, directionMode);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    JSONObject data;
                    try {
                        Log.i("rideDetails", "directions request success");
                        Log.d("rideDetails", response);
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
                        Log.e("rideDetails", e.getMessage());
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
