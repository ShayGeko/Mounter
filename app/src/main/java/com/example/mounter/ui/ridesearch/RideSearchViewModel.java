package com.example.mounter.ui.ridesearch;

import androidx.lifecycle.ViewModel;

import com.example.mounter.data.model.RealmLiveData;
import com.example.mounter.data.realmModels.RidePostingModel;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.mongodb.sync.SyncConfiguration;

import static com.example.mounter.Mounter.mounter;
import static com.example.mounter.Mounter.realmPartition;
import static com.example.mounter.common.MounterDateUtil.getCurrentDateInDays;
import static com.example.mounter.common.MounterStringUtils.containsData;

public class RideSearchViewModel extends ViewModel {
    private static final String TAG = "RideSearchViewModel";
    public static final String DEPARTURE_TIME_IN_DAYS = "departureTimeInDays";

    private Realm mRealm;
    private RealmLiveData<RidePostingModel> mRidePostingsResult;
    private RealmQuery<RidePostingModel> currentRidePostings;
    private String filter;

    public RideSearchViewModel(){
        configureRealm();

        currentRidePostings = mRealm.where(RidePostingModel.class)
                .greaterThanOrEqualTo(DEPARTURE_TIME_IN_DAYS, getCurrentDateInDays());


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
        return currentRidePostings.findAll();
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

    public void updateDestinationAddressFilter(String destinationAddress) {
        if(!containsData(destinationAddress)){
            mRidePostingsResult.postValue(currentRidePostings.findAllAsync());
        }
        mRidePostingsResult.postValue(
                mRealm.where(RidePostingModel.class)
                        .greaterThanOrEqualTo(DEPARTURE_TIME_IN_DAYS, getCurrentDateInDays())
                        .and()
                        .contains("destinationAddress", destinationAddress, Case.INSENSITIVE)
                        .sort("departureTime")
                        .findAllAsync()
        );
    }
}
