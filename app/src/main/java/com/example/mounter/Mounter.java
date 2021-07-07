package com.example.mounter;

import android.app.Application;


import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;



public class Mounter extends Application {
    public static App mounter;
    @Override
    public void onCreate(){
        super.onCreate();
        Realm.init(this);

        mounter = new App(new AppConfiguration.Builder(getString(R.string.mongodb_realm_app_id))
                .build());
    }
}
