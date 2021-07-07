package com.example.mounter.ridesearch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.mounter.R;
import com.example.mounter.data.model.RidePostingModel;
import com.example.mounter.databinding.ActivityRideSearchBinding;
import com.example.mounter.ui.login.LoginActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.jetbrains.annotations.NotNull;

import java.util.Date;

import io.realm.Realm;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

import static com.example.mounter.Mounter.mounter;

public class RideSearchActivity extends AppCompatActivity {
    private User user;
    private Realm mRealm = null;
    private boolean realmSetUpPrompted = false;
    private ActivityRideSearchBinding binding;
    private RecyclerView recyclerView;
    private RidePostingRecyclerViewAdapter adapter;

    protected void onStart(){
        Log.d("RideSearchActivity", "onStart Fired");
        super.onStart();

        try {
            user = mounter.currentUser();
        }
        catch (IllegalStateException e) {

        }
        if (user == null) {
            Log.d("RideSearchActivity", "User not authorized, prompting login");
            // if no user is currently logged in, start the login activity so the user can authenticate
            startActivity(new Intent(this, LoginActivity.class));
        }
        else{
            setUpRealm(user);
       }
    }

    protected void onResume(){
        Log.d("RideSearchActivity", "onResumeFired");
        super.onResume();
//        try {
//            user = mounter.currentUser();
//        }
//        catch (IllegalStateException e) {
//
//        }
//        if(user != null && !realmSetUpPrompted){
//            setUpRealm(user);
//        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("RideSearchActivity", "On create fired");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_search);

        mRealm = Realm.getDefaultInstance();
        recyclerView = findViewById(R.id.ridePosting_list);

//        Toolbar toolbar = binding.toolbar;
//        setSupportActionBar(toolbar);
//        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
//        toolBarLayout.setTitle(getTitle());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Log.d("RideSearchActivity", "Creating a ride");
            RidePostingModel ridePosting = new RidePostingModel(user);
            ridePosting.setDestinationAddress("SFU Burnaby campus");
            ridePosting.setOriginAddress("SFU Surrey campus");
            ridePosting.setDepartureTime(new Date(System.currentTimeMillis()));
            // SFU Burnaby coordinates
            ridePosting.setDestinationLatLng(new LatLng(49.276765, -122.917957));
            // SFU Surrey coordinates
            ridePosting.setOriginLatLng(new LatLng(49.188680, -122.839940));
            ridePosting.setDescription("test");
            mRealm.executeTransactionAsync(r -> {
                r.delete(RidePostingModel.class);
            });
        });
    }

    @Override
    protected  void onDestroy(){
        super.onDestroy();
        Log.d("RideSearchActivity", "onDestroyFired");
        recyclerView.setAdapter(null);
        mRealm.close();
        mounter.currentUser().logOutAsync(result -> {
            if (result.isSuccess()) {
            } else {
            }
        });
    }
    private void setUpRecyclerView(Realm realm){
        Log.d("RideSearchActivity", "setUpRecyclerView: adapter set up");
        recyclerView = findViewById(R.id.ridePosting_list);
        adapter = new RidePostingRecyclerViewAdapter(realm.where(RidePostingModel.class).findAll());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void setUpRealm(User user){
        Log.d("RideSearchActivity", "settingUpRealm");
        realmSetUpPrompted = true;
        String partitionValue = "1";

        SyncConfiguration config = new SyncConfiguration.Builder(user, partitionValue)
                .waitForInitialRemoteData()
                .build();

        Realm.setDefaultConfiguration(config);

        Realm.getInstanceAsync(config, new Realm.Callback() {
            @Override
            public void onSuccess(@NotNull Realm realm) {
                Log.d("RideSearchActivity", "ui thread realm instance acquired");
                mRealm = realm;
                setUpRecyclerView(realm);


            }
            @Override
            public void onError(@NonNull Throwable exception) {
                super.onError(exception);
                Log.e("RideSearchActivity", "failed to get the ui thread realm instance");
                exception.printStackTrace();
            }
        });


    }
}