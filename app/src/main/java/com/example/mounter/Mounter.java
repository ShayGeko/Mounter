package com.example.mounter;

import android.app.Application;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;



public class Mounter extends Application {
    public static App mounter;
    public static String realmPartition;

    ExecutorService executorService;

    @Override
    public void onCreate(){
        super.onCreate();
        Realm.init(this);

        mounter = new App(new AppConfiguration.Builder(getString(R.string.mongodb_realm_app_id))
                .build());
        realmPartition = getString(R.string.realm_partition);
        executorService = Executors.newFixedThreadPool(4);
    }

    public ExecutorService getExecutor() {
        return executorService;
    }
}
