package com.example.mounter.ridesearch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.mounter.MounterBaseActivity;
import com.example.mounter.R;
import com.example.mounter.ridePostingCreator.ChooseRidePosting;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class RideSearchActivity extends MounterBaseActivity {
    private String TAG = "rideSearchActivity";
    private RecyclerView recyclerView;
    private RidePostingRecyclerViewAdapter adapter;
    private RideSearchViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "On create fired");

        // initially theme is set to the splash screen
        // once onCreate is fired <-> activity is loaded
        // change the theme to the App theme once loaded
        // idea taken from https://stackoverflow.com/a/38800389/11957322
        setTheme(R.style.Theme_MounteR);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_search);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), ChooseRidePosting.class));
        });

        viewModel = new ViewModelProvider(this).get(RideSearchViewModel.class);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        adapter = new RidePostingRecyclerViewAdapter(viewModel.getAllCurrentRidePostings());

        recyclerView = findViewById(R.id.ridePosting_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    protected  void onDestroy(){
        Log.d("RideSearchActivity", "onDestroyFired");
        super.onDestroy();
    }
}