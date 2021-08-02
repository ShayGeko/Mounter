package com.example.mounter.data.realmModels;


import com.example.mounter.Mounter;

import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class RideRequestModel extends RealmObject {
    @PrimaryKey
    @Required
    ObjectId _id = new ObjectId();
    @Required
    private ObjectId _driverId;
    @Required
    private ObjectId _passengerId;
    @Required
    private ObjectId _ridePostingId;
    @Required
    private String _partition = Mounter.realmPartition;

    public RideRequestModel(){
    }
    public RideRequestModel(ObjectId driverId, ObjectId passengerId, ObjectId ridePostingId){
        this._driverId = driverId;
        this._passengerId = passengerId;
        this._ridePostingId = ridePostingId;
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
}