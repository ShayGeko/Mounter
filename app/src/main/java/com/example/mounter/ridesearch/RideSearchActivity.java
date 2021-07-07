package com.example.mounter.ridesearch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.mounter.R;
import com.example.mounter.data.model.RidePostingModel;
import com.example.mounter.databinding.ActivityRideSearchBinding;
import com.example.mounter.ui.login.LoginActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

import static com.example.mounter.Mounter.mounter;

public class RideSearchActivity extends AppCompatActivity {
    private User user;
    private Realm mRealm;
    private ActivityRideSearchBinding binding;
    private RecyclerView recyclerView;
    private RidePostingRecyclerViewAdapter adapter;

    protected void onStart(){
        super.onStart();

        try {
            user = mounter.currentUser();
        }
        catch (IllegalStateException e) {

        }
        if (user == null) {
            // if no user is currently logged in, start the login activity so the user can authenticate
            startActivity(new Intent(this, LoginActivity.class));
        }
        else{
            String partitionValue = "1";

            SyncConfiguration config = new SyncConfiguration.Builder(
                    user,
                    partitionValue)
                    .build();

            Realm.setDefaultConfiguration(config);

            // addChangeListenerToRealm(realm);
//
//            FutureTask<String> task = new FutureTask(new BackgroundQuickStart(mounter.currentUser()), "test");
//            ExecutorService executorService = Executors.newFixedThreadPool(2);
//            executorService.execute(task);

            Realm.getInstanceAsync(config, new Realm.Callback() {
               @Override
               public void onSuccess(Realm realm) {
                   Log.i("RideSearchActivity", "ui thread realm instance acquired");
                   mRealm = realm;
                   setUpRecyclerView(realm);
                }
                @Override
                public void onError(@NonNull Throwable exception) {
                    super.onError(exception);
                    exception.printStackTrace();
                }
            });
       }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRideSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());

        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(view -> {
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
                r.insert(ridePosting);
            });
        });
    }
//    private void addChangeListenerToRealm(Realm realm) {
//        // all tasks in the realm
//        RealmResults<RidePostingModel> postings = realm.where(RidePostingModel.class).findAllAsync();
//        postings.addChangeListener((collection, changeSet) -> {
//            // process deletions in reverse order if maintaining parallel data structures so indices don't change as you iterate
//            OrderedCollectionChangeSet.Range[] deletions = changeSet.getDeletionRanges();
//            for (OrderedCollectionChangeSet.Range range : deletions) {
//                Log.v("QUICKSTART", "Deleted range: " + range.startIndex + " to " + (range.startIndex + range.length - 1));
//            }
//            OrderedCollectionChangeSet.Range[] insertions = changeSet.getInsertionRanges();
//            for (OrderedCollectionChangeSet.Range range : insertions) {
//                Log.v("QUICKSTART", "Inserted range: " + range.startIndex + " to " + (range.startIndex + range.length - 1));                            }
//            OrderedCollectionChangeSet.Range[] modifications = changeSet.getChangeRanges();
//            for (OrderedCollectionChangeSet.Range range : modifications) {
//                Log.v("QUICKSTART", "Updated range: " + range.startIndex + " to " + (range.startIndex + range.length - 1));                            }
//        });
//    }

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
        Log.i("RideSearchActivity", "setUpRecyclerView: adapter set up");
        RecyclerView recyclerView = findViewById(R.id.ridePosting_list);
        RidePostingRecyclerViewAdapter adapter = new RidePostingRecyclerViewAdapter(realm.where(RidePostingModel.class).findAll());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }
//    public class BackgroundQuickStart implements Runnable {
//        User user;
//        public BackgroundQuickStart(User user) {
//            this.user = user;
//        }
//        @Override
//        public void run() {
//            String partitionValue = "1";
//            SyncConfiguration config = new SyncConfiguration.Builder(
//                    user,
//                    partitionValue)
//                    .build();
//            Realm backgroundThreadRealm = Realm.getInstance(config);
//            RidePostingModel posting = new RidePostingModel(user);
//            backgroundThreadRealm.executeTransaction (transactionRealm -> {
//                transactionRealm.insert(posting);
//            });
//            // all tasks in the realm
//            RealmResults<RidePostingModel> tasks = backgroundThreadRealm.where(RidePostingModel.class).findAll();
//            // you can also filter a collection
//            RealmResults<RidePostingModel> tasksThatBeginWithN = tasks.where().beginsWith("name", "N").findAll();
//            RealmResults<RidePostingModel> openTasks = tasks.where().equalTo("destinationAddress", "SFU Burnaby Campus").findAll();
//            RidePostingModel otherTask = tasks.get(0);
//            // all modifications to a realm must happen inside of a write block
//            backgroundThreadRealm.executeTransaction( transactionRealm -> {
//                RidePostingModel innerOtherTask = transactionRealm.where(RidePostingModel.class).equalTo("_id", otherTask.get_id()).findFirst();
//            });
//            RidePostingModel yetAnotherTask = tasks.get(0);
//            ObjectId yetAnotherTaskId = yetAnotherTask.get_id();
//            // all modifications to a realm must happen inside of a write block
//            backgroundThreadRealm.executeTransaction( transactionRealm -> {
//                RidePostingModel innerYetAnotherTask = transactionRealm.where(RidePostingModel.class).equalTo("_id", yetAnotherTaskId).findFirst();
//                innerYetAnotherTask.deleteFromRealm();
//            });
//            // because this background thread uses synchronous realm transactions, at this point all
//            // transactions have completed and we can safely close the realm
//            backgroundThreadRealm.close();
//        }
   // }
}