package com.example.mounter.rideDetails;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.example.mounter.MounterBaseActivity;
import com.example.mounter.R;
import com.example.mounter.data.realmModels.RidePostingModel;
import com.example.mounter.data.realmModels.UserInfoModel;
import com.example.mounter.databinding.ActivityDirectionsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.snackbar.Snackbar;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class RideDetailsActivity extends MounterBaseActivity implements OnMapReadyCallback {
    private RideDetailsViewModel viewModel;

    public static final String RIDE_POSTING_ID = "ridePostingId";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityDirectionsBinding binding = ActivityDirectionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // get the rideId from the intent
        // see Intent in RidePostingRecyclerViewAdapter#onBindViewHolder()
        ObjectId rideId = (ObjectId) getIntent().getSerializableExtra(RIDE_POSTING_ID);


        viewModel = new ViewModelProvider(this, new RideDetailsViewModelFactory(rideId))
                .get(RideDetailsViewModel.class);


        Button requestToDriveBtn = findViewById(R.id.requestToDriveRideBtn);

        // requestToDriveBtn.setEnabled(false);
        viewModel.getRidePosting().observe(this, ridePosting -> {
            if(ridePosting.isLoaded()){
                if(ridePosting.needsAdriver()){
                    addRequestToDriveBtn();
                }
                displayRideData(ridePosting);
            }
        });
        viewModel.getDriverInfo().observe(this, driverInfo ->{
            if(driverInfo.isLoaded() && driverInfo.isValid()){
                displayDriverData(driverInfo);
            }
        });
        Button requestToJoinRideBtn = findViewById(R.id.requestToJoinRideBtn);

        // if passenger request to join the ride - crete new rideRequest in Realm
        // TODO send push notification to the driver
        requestToJoinRideBtn.setOnClickListener(view ->
                viewModel.createPassengerRideRequest()
        );

        SetUpMap();
    }

    private void addRequestToDriveBtn() {
        Button requestToDriveBtn = findViewById(R.id.requestToDriveRideBtn);
        requestToDriveBtn.setVisibility(View.VISIBLE);
        requestToDriveBtn.setOnClickListener(view -> {
            viewModel.createDriverRideRequest();
        });
    }

    /**
     * Displays the rideData from the ridePosting on the UI
     * @param ridePosting {@link RidePostingModel}
     */
    private void displayRideData(RidePostingModel ridePosting) {
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

        TextView rating = findViewById(R.id.ride_details_companion_rating);

        rating.setText(getString(R.string.rating) + ": " + userInfo.getRating());
    }

    /**
     * Event indicating that the map was loaded <br/>
     * Does not assure that the layout for it was loaded
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMinZoomPreference(10.0f);
        googleMap.setMaxZoomPreference(20.0f);

        viewModel.getDirections().observe(this, directions -> {
            if(directions.isLoaded()){
                displayRoute(googleMap, viewModel.getRoute(directions));
            }
        });
    }
    /**
     * Adds a polyline representing the route to the {@link GoogleMap}
     *
     * @param googleMap {@link GoogleMap}
     * @param pointsOnRoute {@link List<LatLng>} list from locations along the route from origin to destination
     */
    private void displayRoute(GoogleMap googleMap, ArrayList<LatLng> pointsOnRoute) {
        googleMap.addPolyline(new PolylineOptions()
                .addAll(pointsOnRoute)
                .width(5F)
                .color(Color.RED));

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(pointsOnRoute.get(0)));
        googleMap.addMarker(new MarkerOptions().position(pointsOnRoute.get(0)));
        googleMap.addMarker((new MarkerOptions().position(pointsOnRoute.get(pointsOnRoute.size() - 1))));
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
