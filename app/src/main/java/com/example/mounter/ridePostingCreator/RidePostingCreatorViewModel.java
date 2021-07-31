package com.example.mounter.ridePostingCreator;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mounter.data.Result;
import com.example.mounter.data.model.RidePostingModel;
import com.example.mounter.ridePostingCreator.model.RidePostingCreatorFormState;

import org.jetbrains.annotations.NotNull;

import io.realm.Realm;
import io.realm.mongodb.User;

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
                bgRealm -> bgRealm.insert(RidePostingModel.createByPassenger(
                        mounter.currentUser(),
                        originAddress,
                        destinationAddress,
                        departureDate + " " +  departureTime,
                        description
                )),
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
                bgRealm -> bgRealm.insert(RidePostingModel.createByDriver(
                        mounter.currentUser(),
                        originAddress,
                        destinationAddress,

                        departureDate + " " +  departureTime,
                        description,
                        estimatedPrice
                        )),
                () -> creationResult.setValue(Result.Success),
                error -> creationResult.setValue(Result.Failure));
    }
}
