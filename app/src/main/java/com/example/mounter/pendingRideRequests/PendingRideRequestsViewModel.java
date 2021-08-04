package com.example.mounter.pendingRideRequests;

import androidx.lifecycle.ViewModel;

import com.example.mounter.data.model.RealmLiveData;
import com.example.mounter.data.model.RealmLiveObject;
import com.example.mounter.data.realmModels.RidePostingModel;
import com.example.mounter.data.realmModels.RideRequestModel;
import com.example.mounter.data.realmModels.UserInfoModel;

import org.bson.types.ObjectId;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.mounter.Mounter.mounter;

public class PendingRideRequestsViewModel extends ViewModel {
    private Realm mRealm;
    private RealmLiveData<RideRequestModel> rideRequests;

    public PendingRideRequestsViewModel(){
        mRealm = Realm.getDefaultInstance();
        rideRequests = loadRideRequests();
    }

    public RealmLiveData<RideRequestModel> getRidePostingsAsLiveData() {
        return rideRequests;
    }
    public RealmResults<RideRequestModel> getRidePostings(){
        return mRealm.where(RideRequestModel.class)
                        .equalTo("driver._userId", mounter.currentUser().getId()).findAllAsync();
    }
    public RealmLiveObject<UserInfoModel> getUserById(String id){
        return new RealmLiveObject<>(
                mRealm.where(UserInfoModel.class).equalTo("_userId", id).findFirstAsync());
    }
    private RealmLiveData<RideRequestModel> loadRideRequests() {
        return new RealmLiveData<>(
                mRealm.where(RideRequestModel.class)
                        .equalTo("driver._userId", mounter.currentUser().getId()).findAllAsync());
    }

    public void rideRequestDeclined(ObjectId rideRequestId) {
        mRealm.executeTransactionAsync(bgRealm->{
            RideRequestModel rideRequestModel = bgRealm
                    .where(RideRequestModel.class)
                    .equalTo("_id", rideRequestId).findFirst();
            rideRequestModel.deleteFromRealm();
        });
    }
    public void rideRequestAccepted(ObjectId rideRequestId) {
        mRealm.executeTransactionAsync(bgRealm->{
            RideRequestModel rideRequestModel = bgRealm
                    .where(RideRequestModel.class)
                    .equalTo("_id", rideRequestId).findFirst();

            RidePostingModel ridePosting = rideRequestModel.getRidePosting().first();

            if(rideRequestModel.isADriverRequest()){
                UserInfoModel driver = bgRealm
                        .where(UserInfoModel.class)
                        .equalTo("_userId", rideRequestModel.getDriverId().toString()).findFirst();

                driver.addRidePosting(ridePosting);
                ridePosting.setDriverId(new ObjectId(driver.getUserId()));
            }
            else {
                UserInfoModel passenger = rideRequestModel.getPassenger().first();
                ridePosting.addPassenger(passenger);
            }

            rideRequestModel.deleteFromRealm();
        });
    }
}
