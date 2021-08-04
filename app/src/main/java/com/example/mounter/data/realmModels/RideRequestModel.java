package com.example.mounter.data.realmModels;


import com.example.mounter.Mounter;

import org.bson.types.ObjectId;

import java.util.SortedSet;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class RideRequestModel extends RealmObject {
    @PrimaryKey
    @Required
    ObjectId _id = new ObjectId();
    private ObjectId _postingCreatorId;
    private ObjectId _driverId;
    private ObjectId _passengerId;
    @Required
    private ObjectId _ridePostingId;
    @Required
    private String _partition = Mounter.realmPartition;

    private Boolean isDriverRequest = false;

    @LinkingObjects("pendingRideRequests")
    private final RealmResults<UserInfoModel> driver = null;

    @LinkingObjects("sentRideRequests")
    private final RealmResults<UserInfoModel> passenger = null;

    @LinkingObjects("rideRequests")
    private final RealmResults<RidePostingModel> ridePosting = null;

    public RideRequestModel(){
    }
    public RideRequestModel(ObjectId driverId, ObjectId passengerId, ObjectId ridePostingId){
        this._driverId = driverId;
        this._passengerId = passengerId;
        this._ridePostingId = ridePostingId;
    }
    public static RideRequestModel newPassengerRequest(ObjectId postingCreatorId, ObjectId passengerId, ObjectId ridePostingId){
        RideRequestModel request = new RideRequestModel();
        request._postingCreatorId = postingCreatorId;
        request._passengerId = passengerId;
        request._ridePostingId = ridePostingId;

        return request;
    }
    public static RideRequestModel newDriverRequest(ObjectId postingCreatorId, ObjectId _driverId, ObjectId ridePostingId){
        RideRequestModel request = new RideRequestModel();
        request._postingCreatorId = postingCreatorId;
        request._driverId = _driverId;
        request._ridePostingId = ridePostingId;
        request.isDriverRequest = true;

        return request;
    }
    public ObjectId getId(){
        return _id;
    }
    public ObjectId getDriverId(){
        return _driverId;
    }
    public void setDriverId(ObjectId driverId){
        this._driverId = driverId;
    }
    public ObjectId getPassengerId(){
        return _passengerId;
    }
    public void setPassengerId(ObjectId passengerId){
        this._passengerId = passengerId;
    }
    public ObjectId getRidePostingId(){
        return _ridePostingId;
    }
    public void setRidePostingId(ObjectId ridePostingId){
        this._ridePostingId = ridePostingId;
    }

    public RealmResults<UserInfoModel> getPassenger(){
        return passenger;
    }
    public RealmResults<UserInfoModel> getDriver() {
        return driver;
    }
    public RealmResults<RidePostingModel> getRidePosting(){
        return ridePosting;
    }

    public ObjectId getCreatorId() {
        return _postingCreatorId;
    }

    public boolean isADriverRequest(){
        return isDriverRequest;
    }


}
