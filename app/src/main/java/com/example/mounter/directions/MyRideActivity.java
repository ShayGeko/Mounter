package com.example.mounter.directions;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mounter.R;
import com.example.mounter.data.model.RidePostingModel;
import com.example.mounter.data.model.RideRequestModel;
import com.example.mounter.data.model.UserInfoModel;
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

        // this activity is always launched by intent with "ridePostingId" inside
        // get the ridePostingId and find the ridePosting based on it
        ObjectId rideId = (ObjectId) getIntent().getSerializableExtra("ridePostingId");
        ridePosting = mRealm.where(RidePostingModel.class).equalTo("_id", rideId).findFirst();

        ObjectId driverId = ridePosting.getDriverId();
        String driverIdStr = driverId.toString();

        Button requestToJoinRideBtn = findViewById(R.id.requestToJoinRideBtn);

        // if passenger request to join the ride - crete new rideRequest in Realm
        // send push notification to the driver
        requestToJoinRideBtn.setOnClickListener(view -> {

            mRealm.executeTransactionAsync(transactionRealm -> {
                RideRequestModel rideRequestModel = new RideRequestModel(
                        driverId,
                        new ObjectId(user.getId()),
                        rideId);

                transactionRealm.insert(rideRequestModel);
            });
        });
        // get the driver user from Realm and display its data
        mRealm.executeTransactionAsync(transactionRealm -> {
            UserInfoModel userInfo = transactionRealm
                    .where(UserInfoModel.class)
                    .equalTo("_userId", driverIdStr)
                    .findFirst();

            displayDriverData(userInfo);

            transactionRealm.close();
        });

        displayRideData();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this::onMapReady);
    }

    private void displayRideData() {
        TextView originAddressTextView = findViewById(R.id.ride_details_origin_address_textView);
        TextView destinationAddressTextView = findViewById(R.id.ride_details_destination_address_textView);
        TextView departureTimeTextView = findViewById(R.id.ride_details_departure_time);

        originAddressTextView.append(ridePosting.getOriginAddress());
        destinationAddressTextView.append(ridePosting.getDestinationAddress());
        departureTimeTextView.append(ridePosting.getDepartureTime());
    }

    private void displayDriverData(UserInfoModel userInfo) {
        TextView driverName = findViewById(R.id.ride_details_companion_name);

        driverName.setText(userInfo.getName() + " " +  userInfo.getSurname());
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
    /* partially autogenerated by AndroidStudio*/
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

}
