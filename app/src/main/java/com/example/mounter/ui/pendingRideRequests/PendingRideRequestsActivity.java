package com.example.mounter.ui.pendingRideRequests;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mounter.ui.MounterBaseActivity;
import com.example.mounter.R;

public class PendingRideRequestsActivity extends MounterBaseActivity {
    private PendingRideRequestsViewModel viewModel;
    private RecyclerView recyclerView;
    private PendingRideRequestsRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(userNotLoggedIn) return;
        setContentView(R.layout.activity_pending_ride_requests);

        viewModel = new ViewModelProvider(this).get(PendingRideRequestsViewModel.class);

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        adapter = new PendingRideRequestsRecyclerViewAdapter(viewModel.getRidePostings(), viewModel);

        recyclerView = findViewById(R.id.rideRequests_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }
}
