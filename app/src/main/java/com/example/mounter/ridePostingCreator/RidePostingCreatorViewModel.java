package com.example.mounter.ridePostingCreator;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mounter.Mounter;
import com.example.mounter.data.model.Result;
import com.example.mounter.data.realmModels.RidePostingModel;
import com.example.mounter.data.realmModels.UserInfoModel;
import com.example.mounter.data.repositories.GeocodingRepository;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

import static com.example.mounter.Mounter.mounter;

public class RidePostingCreatorViewModel extends ViewModel {
    private static String TAG = "RidePostingCreatorViewModel";
    private Realm mRealm;
    private LiveData<RidePostingModel> mRidePosting;
    private MutableLiveData<Result> creationResult = new MutableLiveData<>();
    private MutableLiveData<Result> getDestinationLatLngResult = new MutableLiveData<>();
    private MutableLiveData<Result> getOriginLatLngResult = new MutableLiveData<>();
    private GeocodingRepository repository;
    private CompositeDisposable disposables = new CompositeDisposable();


    public RidePostingCreatorViewModel(){
        mRealm = Realm.getDefaultInstance();
    }
    public RidePostingCreatorViewModel(Mounter app){
        mRealm = Realm.getDefaultInstance();
        repository = new GeocodingRepository(app);
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
        creationResult.setValue(Result.Pending);
        RidePostingModel ridePosting = RidePostingModel.createByPassenger(
                mounter.currentUser(),
                originAddress,
                destinationAddress,
                departureDate + " " +  departureTime,
                description);
        computeCoordinatesAndAddToRealm(ridePosting, false);
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
        creationResult.setValue(Result.Pending);
        RidePostingModel ridePosting = RidePostingModel.createByDriver(
                mounter.currentUser(),
                originAddress,
                destinationAddress,
                departureDate + " " + departureTime,
                description,
                estimatedPrice);
        computeCoordinatesAndAddToRealm(ridePosting, true);
    }


    private void computeCoordinatesAndAddToRealm(RidePostingModel ridePosting, Boolean shouldCreateAsADriver) {
        disposables.add(
                repository.getLatLngPair(ridePosting.getOriginAddress(), ridePosting.getDestinationAddress())
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                latLngPair -> {
                                    ridePosting.setOriginLatLng(latLngPair.first);
                                    ridePosting.setDestinationLatLng(latLngPair.second);
                                    addToRealm(ridePosting, shouldCreateAsADriver);
                                    },
                                throwable -> {
                                    Log.e(TAG, throwable.getMessage());
                                    creationResult.postValue(Result.Failure);
                                }));
    }

    private void addToRealm(RidePostingModel ridePosting, Boolean createAsADriver) {
        // not in the main thread - need to create a new instance for realm
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(
                    workRealm -> {
                        workRealm.copyToRealm(ridePosting);
                        UserInfoModel user = workRealm.where(UserInfoModel.class)
                                .equalTo("_userId", mounter.currentUser().getId()).findFirst();
                        user.addToMyRidePostings(ridePosting);
                        if(createAsADriver) {
                            user.addRidePosting(ridePosting);
                        }
                        else{
                            ridePosting.addPassenger(user);
                        }
                    });
            creationResult.postValue(Result.Success);
        }
        catch(Exception e){
            creationResult.postValue(Result.Failure);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mRealm.close();
        disposables.clear();
    }
}
