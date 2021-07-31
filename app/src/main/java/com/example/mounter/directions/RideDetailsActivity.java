package com.example.mounter.directions;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.User;

import static com.example.mounter.Mounter.mounter;

public class RideDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    public static final String DRIVING = "driving";
    public static final String RIDE_POSTING_ID = "ridePostingId";
    private User user;
    private Realm mRealm;
    private RidePostingModel ridePosting;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityDirectionsBinding binding = ActivityDirectionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // get the rideId from the intent
        // see Intent in RidePostingRecyclerViewAdapter#onBindViewHolder()
        ObjectId rideId = (ObjectId) getIntent().getSerializableExtra(RIDE_POSTING_ID);

        getRealmAndLoadRideData(rideId);

        ObjectId driverId = ridePosting.getDriverId();

        Button requestToJoinRideBtn = findViewById(R.id.requestToJoinRideBtn);

        // if passenger request to join the ride - crete new rideRequest in Realm
        // TODO send push notification to the driver
        requestToJoinRideBtn.setOnClickListener(view ->
                createRideRequest(rideId, driverId)
        );
        getAndDisplayDriverInfo(driverId);
        displayRideData();


        SetUpMap();
    }

    @Override
    public void onDestroy() {
        mRealm.close();
        super.onDestroy();
    }

    /**
     * Gets default instance of {@link Realm}, sets {@link #user} to {@link App#currentUser()} <br/>
     * Sets {@link #ridePosting} to {@link RidePostingModel} with matching rideId
     *
     * @param rideId {@link ObjectId} - id of the ride posting
     */
    private void getRealmAndLoadRideData(ObjectId rideId) {
        mRealm = Realm.getDefaultInstance();
        user = mounter.currentUser();
        // this activity is always launched by intent with "ridePostingId" inside
        // get the ridePostingId and find the ridePosting based on it
        ridePosting = mRealm.where(RidePostingModel.class).equalTo("_id", rideId).findFirst();
    }

    /**
     * creates a {@link RideRequestModel} and stores it into the {@link Realm}
     *
     * @param rideId   {@link ObjectId} - id of the ride posting
     * @param driverId {@link ObjectId} - id of the driver who created the ride posting
     */
    private void createRideRequest(ObjectId rideId, ObjectId driverId) {
        mRealm.executeTransactionAsync(transactionRealm -> {
            RideRequestModel rideRequestModel = new RideRequestModel(
                    driverId,
                    new ObjectId(user.getId()),
                    rideId);

            transactionRealm.insert(rideRequestModel);
        });
    }

    /**
     * Queries {@link Realm} for  {@link UserInfoModel} with User id matching the specified driver id <br/>
     * Displays the user data once loaded
     *
     * @param driverId {@link ObjectId} - id of the driver who created the ridePosting
     */
    private void getAndDisplayDriverInfo(ObjectId driverId) {
        // get the driver user from Realm and display its data
        mRealm.executeTransactionAsync(transactionRealm -> {
            UserInfoModel userInfo = transactionRealm
                    .where(UserInfoModel.class)
                    .equalTo("_userId", driverId.toString())
                    .findFirst();

            assert userInfo != null;
            displayDriverData(userInfo);

            transactionRealm.close();
        });
    }

    /**
     * Displays the rideData from the {@link #ridePosting} on the UI
     */
    private void displayRideData() {
        TextView originAddressTextView = findViewById(R.id.ride_details_origin_address_textView);
        TextView destinationAddressTextView = findViewById(R.id.ride_details_destination_address_textView);
        TextView departureTimeTextView = findViewById(R.id.ride_details_departure_time);

        originAddressTextView.append(ridePosting.getOriginAddress());
        destinationAddressTextView.append(ridePosting.getDestinationAddress());
        departureTimeTextView.append(ridePosting.getDepartureTime());
    }

    /**
     * Loads the {@link GoogleMap} into {@link SupportMapFragment} <br/>
     * Calls {@link #onMapReady(GoogleMap)} once loaded
     */
    private void SetUpMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    /**
     * Displays the driver data on the UI based on the provided {@link UserInfoModel}
     *
     * @param userInfo {@link UserInfoModel} - userModel for the driver
     */
    private void displayDriverData(UserInfoModel userInfo) {
        TextView driverName = findViewById(R.id.ride_details_companion_name);

        String driverFullName = userInfo.getName() + " " + userInfo.getSurname();
        driverName.setText(driverFullName);
    }

    /**
     * Event indicating that the map was loaded <br/>
     * Does not assure that the layout for it was loaded
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMinZoomPreference(10.0f);
        googleMap.setMaxZoomPreference(20.0f);

        loadRide(googleMap);
    }

    /**
     * Displays the ride from {@link #ridePosting} on the {@link GoogleMap}
     *
     * @param googleMap {@link GoogleMap}
     */
    private void loadRide(GoogleMap googleMap) {
        // Add a marker in SFU and move the camera
        LatLng originLatLng = ridePosting.getOriginActualLatLng();
        LatLng destinationLatLng = ridePosting.getDestinationActualLatLng();

        // SFU Burnaby as default
        if (originLatLng == null) originLatLng = new LatLng(49.276765, -122.917957);
        // SFU Surrey as default
        if (destinationLatLng == null) destinationLatLng = new LatLng(49.188680, -122.839940);

        googleMap.addMarker(new MarkerOptions().position(originLatLng).title(String.valueOf(R.string.origin_location)));
        googleMap.addMarker(new MarkerOptions().position(destinationLatLng).title(String.valueOf(R.string.destination_location)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(destinationLatLng));

        GetAndDisplayDirections(googleMap, originLatLng, destinationLatLng);
    }

    /**
     * Gets directions from specified source and destination locations <br/>
     * Displays the polyline
     *  @param googleMap {@link GoogleMap}
     * @param source {@link LatLng}
     * @param destination {@link LatLng}
     */
    private void GetAndDisplayDirections(GoogleMap googleMap, LatLng source, LatLng destination) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = buildDirectionsUrl(destination, source);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    JSONObject data;
                    try {
                        Log.i("rideDetails", "directions request success");
                        Log.d("rideDetails", response);

                        data = new JSONObject(response);
                        List<LatLng> pointsOnRoute = getPointsOnRoute(data);

                        displayRoute(googleMap, pointsOnRoute);
                    } catch (Exception e) {
                        Log.e("rideDetails", e.getMessage());
                        e.printStackTrace();
                    }
                }, error -> showError());

        queue.add(stringRequest);
    }

    /**
     * @param origin {@link LatLng}
     * @param destination {@link LatLng}
     * @return {@link String} directions Url to be asked for the route from origin to destination locations
     */
    String buildDirectionsUrl(LatLng origin, LatLng destination) {
        String originStr = "origin=" + origin.latitude + "," + origin.longitude;
        String destinationStr = "destination=" + destination.latitude + "," + destination.longitude;
        String mode = "mode=" + RideDetailsActivity.DRIVING;
        String parameters = originStr + "&" + destinationStr + "&" + mode;
        String output = "json";

        return "https://maps.googleapis.com/maps/api/directions/" +
                output + "?" +
                parameters + "&key=" +
                getString(R.string.google_maps_key);
    }

    /**
     * Parses the {@link JSONObject} response from GoogleMaps Directions Api and decodes it
     *
     * @param data {@link JSONObject}
     * @return {@link List<LatLng>} representing the points on the optimal route
     * @throws JSONException if parsing fails
     */
    @NotNull
    private List<LatLng> getPointsOnRoute(JSONObject data) throws JSONException {
        String encodedPoints = data.getJSONArray("routes")
                .getJSONObject(0)
                .getJSONObject("overview_polyline")
                .getString("points");

        return PolyUtil.decode(encodedPoints);
    }

    /**
     * Adds a polyline representing the route to the {@link GoogleMap}
     *
     * @param googleMap {@link GoogleMap}
     * @param pointsOnRoute {@link List<LatLng>} list from locations along the route from origin to destination
     */
    private void displayRoute(GoogleMap googleMap, List<LatLng> pointsOnRoute) {
        googleMap.addPolyline(new PolylineOptions()
                .addAll(pointsOnRoute)
                .width(5F)
                .color(Color.RED));
    }

    /**
     * displays a {@link Snackbar} on UI indicating that a general error has occurred
     */
    private void showError() {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar.make(parentLayout, "An error occurred", Snackbar.LENGTH_LONG)
                .show();
    }
}
