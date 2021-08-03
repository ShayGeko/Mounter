package com.example.mounter.rideDetails;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mounter.data.repositories.DirectionsRepository;
import com.example.mounter.data.RealmLiveObject;
import com.example.mounter.data.model.Directions;
import com.example.mounter.data.realmModels.RidePostingModel;
import com.example.mounter.data.realmModels.RideRequestModel;
import com.example.mounter.data.realmModels.UserInfoModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import org.bson.types.ObjectId;

import java.util.ArrayList;

import io.realm.Realm;

import static com.example.mounter.Mounter.mounter;

public class RideDetailsViewModel extends ViewModel {
    private Realm mRealm;
    private final DirectionsRepository repository;
    private RealmLiveObject<RidePostingModel> ridePosting;
    private RealmLiveObject<UserInfoModel> driverInfo;


    public RideDetailsViewModel(ObjectId rideId){
        mRealm = Realm.getDefaultInstance();
        repository = new DirectionsRepository();
        ridePosting = loadRidePosting(rideId);
        driverInfo = loadDriverInfo(rideId);

    }
    public ArrayList<LatLng> getRoute(Directions directions){
        String encodedPoints = directions
                .getRoute()
                .getOverviewPolyline()
                .getPoints();

        return new ArrayList<>(PolyUtil.decode(encodedPoints));
    }
    public MutableLiveData<Directions> getDirections(){
        LatLng originLatLng = ridePosting.getValue().getOriginActualLatLng();
        LatLng destinationLatLng = ridePosting.getValue().getDestinationActualLatLng();

        // SFU Burnaby as default
        if (originLatLng == null) originLatLng = new LatLng(49.276765, -122.917957);
        // SFU Surrey as default
        if (destinationLatLng == null) destinationLatLng = new LatLng(49.188680, -122.839940);


        return repository.getDirections(
                "driving",
                destinationLatLng.latitude + "," + destinationLatLng.longitude,
                originLatLng.latitude + "," + originLatLng.longitude);
    }
    public RealmLiveObject<RidePostingModel> getRidePosting(){
        return ridePosting;
    }
    public RealmLiveObject<UserInfoModel> getDriverInfo(){
        return driverInfo;
    }



    /**
     * creates a {@link RideRequestModel} and stores it into the {@link Realm}
     */
    public void createRideRequest() {
        RideRequestModel rideRequest = new RideRequestModel(
                ridePosting.getValue().getDriverId(),
                new ObjectId(mounter.currentUser().getId()),
                ridePosting.getValue().getId());
        mRealm.executeTransactionAsync(transactionRealm -> {
            transactionRealm.copyToRealm(rideRequest);

            UserInfoModel driver = transactionRealm.where(UserInfoModel.class)
                    .equalTo("_userId", rideRequest.getDriverId().toString()).findFirst();
            driver.addPendingRideRequest(rideRequest);

            UserInfoModel passenger = transactionRealm.where(UserInfoModel.class)
                    .equalTo("_userId", rideRequest.getPassengerId().toString()).findFirst();
            passenger.addSentRideRequest(rideRequest);

            RidePostingModel ridePosting = transactionRealm.where(RidePostingModel.class)
                    .equalTo("_id", rideRequest.getRidePostingId()).findFirst();
            ridePosting.addRideRequest(rideRequest);
        });
    }

    private RealmLiveObject<RidePostingModel> loadRidePosting(ObjectId rideId) {
        return new RealmLiveObject<>(
                mRealm.where(RidePostingModel.class).equalTo("_id", rideId)
                        .findFirstAsync());
    }
    /**
     * Queries {@link Realm} for  {@link UserInfoModel} with User id matching the specified driver id <br/>
     * Displays the user data once loaded
     */
    private RealmLiveObject<UserInfoModel> loadDriverInfo(ObjectId rideId) {
        return new RealmLiveObject<>(
                mRealm.where(UserInfoModel.class)
                        .equalTo("ridePostings._id", rideId)
                        .findFirstAsync());
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        mRealm.close();
    }
}
