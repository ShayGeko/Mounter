package com.example.mounter.main;

import androidx.lifecycle.ViewModel;

import com.example.mounter.data.model.RealmLiveData;
import com.example.mounter.data.realmModels.RidePostingModel;

import org.bson.types.ObjectId;

import io.realm.Realm;
import io.realm.mongodb.sync.SyncConfiguration;

import static com.example.mounter.Mounter.mounter;
import static com.example.mounter.Mounter.realmPartition;
import static com.example.mounter.common.MounterDateUtil.getCurrentDateInDays;

public class MainViewModel extends ViewModel {
    private Realm mRealm;
    private RealmLiveData<RidePostingModel> scheduledRides;

    public MainViewModel(){
        configureRealm();

        scheduledRides = loadGetScheduledRides();
    }

    public RealmLiveData<RidePostingModel> getScheduledRides() {
        return scheduledRides;
    }

    private RealmLiveData<RidePostingModel> loadGetScheduledRides(){
        return new RealmLiveData<>(
                mRealm.where(RidePostingModel.class)
                    .equalTo("_driverId", new ObjectId(mounter.currentUser().getId()))
                    .or()
                    .contains("passengerIds", mounter.currentUser().getId())
                    .findAllAsync());
    }

    private void configureRealm() {
        SyncConfiguration config = new SyncConfiguration.Builder(mounter.currentUser(), realmPartition)
                .compactOnLaunch()
                .build();
        Realm.setDefaultConfiguration(config);
        mRealm = Realm.getInstance(config);
    }
    @Override
    protected void onCleared(){
        mRealm.close();
        super.onCleared();
    }
}
