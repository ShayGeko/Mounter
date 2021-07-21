package com.example.mounter.ridesearch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.mounter.R;
import com.example.mounter.data.model.RidePostingModel;
import com.example.mounter.ui.createListings.ChooseListing;
import com.example.mounter.ui.createListings.ListingCreator;
import com.example.mounter.ui.login.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.jetbrains.annotations.NotNull;


import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

import static com.example.mounter.Mounter.mounter;

public class RideSearchActivity extends AppCompatActivity {
    private User user;
    private Realm mRealm = null;
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
            if(mRealm == null || mRealm.isClosed()) {
                setUpRealm(user);
            }
        }
    }

    protected void onResume(){
        super.onResume();
        Log.d("RideSearchActivity", "onResume fired");
        if(mRealm != null){
            setUpRecyclerView(mRealm);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("RideSearchActivity", "On create fired");

        // initially theme is set to the splash screen
        // once onCreate is fired <-> activity is loaded
        // change the theme to the App theme once loaded
        // idea taken from https://stackoverflow.com/a/38800389/11957322
        setTheme(R.style.Theme_MounteR);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_search);
        recyclerView = findViewById(R.id.ridePosting_list);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), ChooseListing.class));
        });

    }

    @Override
    protected  void onDestroy(){
        Log.d("RideSearchActivity", "onDestroyFired");
//        recyclerView.setAdapter(null);
        mRealm.close();
        super.onDestroy();
    }
    private void setUpRecyclerView(Realm realm){
        Log.d("RideSearchActivity", "setUpRecyclerView: adapter set up");
        Calendar myCalendar = Calendar.getInstance();
        Date date = new Date();
        recyclerView = findViewById(R.id.ridePosting_list);
        adapter = new RidePostingRecyclerViewAdapter(realm.where(RidePostingModel.class).greaterThanOrEqualTo
                ("departureTimeInDays", getCurrentDateInDays()).findAll());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void setUpRealm(User user){
        Log.d("RideSearchActivity", "settingUpRealm");
        String partitionValue = "1";
        if(mRealm != null && mRealm.isClosed() == false){
            mRealm.close();
        }

        SyncConfiguration config = new SyncConfiguration.Builder(user, partitionValue)
                .waitForInitialRemoteData(5, TimeUnit.SECONDS)
                .compactOnLaunch()
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

    private int getCurrentDateInDays() {
        Calendar myCalendar = Calendar.getInstance();
        int day = myCalendar.get(Calendar.DAY_OF_MONTH);
        int month = myCalendar.get(Calendar.MONTH);
        month += 1;
        int year = myCalendar.get(Calendar.YEAR);

        ListingCreator listingCreator = new ListingCreator();
        RidePostingModel ridePostingModel = new RidePostingModel();
        int daysInMonth = ridePostingModel.getMonthValue(listingCreator.convertMonth(month));   //Converts the month to the number of days in the given month

        return day + daysInMonth + (year * 365);
    }
}