package com.example.mounter.main;

import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.mounter.MounterBaseActivity;
import com.example.mounter.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class MainActivity extends MounterBaseActivity {

    private ImageView upArrow;
    private FragmentContainerView mapLayout;
    private LinearLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.Theme_Design_NoActionBar);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        upArrow = findViewById(R.id.upArrow);
        mapLayout = findViewById(R.id.map);
        fragmentContainer = findViewById(R.id.fragmentContainer);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this::onMapReady);

        upArrow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                slideFragment();
            }
        });
    }

    private void onMapReady(GoogleMap googleMap) {
        googleMap.setMaxZoomPreference(200.0f);
        googleMap.setMinZoomPreference(0.0f);
        LatLng destinationLatLng = new LatLng(49.24892129204957, -122.98082162459477);
        String directionMode = "driving";
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destinationLatLng, 12.0f));
    }

    private void slideFragment(){
        SlideMainFragment fragment = new SlideMainFragment();
        FragmentManager fManager = getSupportFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();
        fTransaction.setCustomAnimations(R.anim.enter_bottom, R.anim.exit_bottom, R.anim.enter_bottom, R.anim.exit_bottom);
        fTransaction.addToBackStack("SlideMainFragment");
        fTransaction.add(R.id.fragmentContainer, fragment, "SlideMainFragment").commit();
    }


}