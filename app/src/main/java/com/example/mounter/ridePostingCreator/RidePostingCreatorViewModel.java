package com.example.mounter.ridePostingCreator;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mounter.data.Result;
import com.example.mounter.data.realmModels.RidePostingModel;
import com.example.mounter.data.realmModels.UserInfoModel;

import io.realm.Realm;

import static com.example.mounter.Mounter.mounter;

public class RidePostingCreatorViewModel extends ViewModel {
    private Realm mRealm;
    private LiveData<RidePostingModel> mRidePosting;
    private MutableLiveData<Result> creationResult = new MutableLiveData<>();


    public RidePostingCreatorViewModel(){
        mRealm = Realm.getDefaultInstance();
    }

    public LiveData<Result> getResult(){
        return creationResult;
    }
    /**
     * Inserts passenger ride posting to the database
     */
    public void createPassengerRidePosting(String destinationAddress,
                                           String originAddress,
                                           String departureTime,
                                           String departureDate,
                                           String description) {
        mRealm.executeTransactionAsync(
                bgRealm -> {
                    RidePostingModel ridePosting = RidePostingModel.createByPassenger(
                            mounter.currentUser(),
                            originAddress,
                            destinationAddress,
                            departureDate + " " +  departureTime,
                            description);
                    bgRealm.copyToRealm(ridePosting);
                    UserInfoModel user = bgRealm.where(UserInfoModel.class)
                            .equalTo("_userId", ridePosting.getDriverId().toString()).findFirst();
                    user.addRidePosting(ridePosting);
                    },
                () -> creationResult.setValue(Result.Success),
                error -> creationResult.setValue(Result.Failure));
    }
    /**
     * Inserts driver ride posting in the database
     */
    public void createDriverRidePosting(String destinationAddress,
                                        String originAddress,
                                        String departureTime,
                                        String departureDate,
                                        String description,
                                        String estimatedPrice) {
        mRealm.executeTransactionAsync(
                bgRealm -> {
                    RidePostingModel ridePosting = RidePostingModel.createByDriver(
                            mounter.currentUser(),
                            originAddress,
                            destinationAddress,
                            departureDate + " " + departureTime,
                            description,
                            estimatedPrice);
                    bgRealm.copyToRealm(ridePosting);
                    UserInfoModel user = bgRealm.where(UserInfoModel.class)
                            .equalTo("_userId", ridePosting.getDriverId().toString()).findFirst();
                    user.addRidePosting(ridePosting);
                },
                () -> creationResult.setValue(Result.Success),
                error -> creationResult.setValue(Result.Failure));
    }
}
