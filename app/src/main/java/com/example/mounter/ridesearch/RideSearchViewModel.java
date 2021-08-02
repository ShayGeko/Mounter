package com.example.mounter.ridesearch;

import androidx.lifecycle.ViewModel;

import com.example.mounter.data.RealmLiveData;
import com.example.mounter.data.realmModels.RidePostingModel;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.mongodb.sync.SyncConfiguration;

import static com.example.mounter.Mounter.mounter;
import static com.example.mounter.Mounter.realmPartition;
import static com.example.mounter.common.MounterDateUtil.getCurrentDateInDays;

public class RideSearchViewModel extends ViewModel {
    private static final String TAG = "RideSearchViewModel";
    public static final String DEPARTURE_TIME_IN_DAYS = "departureTimeInDays";

    private Realm mRealm;
    private RealmLiveData<RidePostingModel> mRidePostingsResult;

    public RideSearchViewModel(){
        configureRealm();
        mRidePostingsResult = getAllCurrentRidePostingsAsLiveData();
    }

    /**
     *
     * @return {@link RealmLiveData<RidePostingModel>} of all ride postings currently in the viewModel
     */
    public RealmLiveData<RidePostingModel> getRidePostings(){
        return mRidePostingsResult;
    }

    /**
     *
     * @return {@link RealmLiveData<RidePostingModel>} of all ride postings with
     * creation date grater or equal to today's date
     */
    public RealmLiveData<RidePostingModel> getAllCurrentRidePostingsAsLiveData(){
        return new RealmLiveData<>(getAllCurrentRidePostings());

    }

    /**
     *
     * @return {@link RealmResults<RidePostingModel>} of all ride postings with
     * creation date grater or equal to today's date
     */
    public RealmResults<RidePostingModel> getAllCurrentRidePostings(){
        return mRealm.where(RidePostingModel.class)
                .greaterThanOrEqualTo(DEPARTURE_TIME_IN_DAYS, getCurrentDateInDays())
                .findAll();
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
