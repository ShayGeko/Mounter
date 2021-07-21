package com.example.mounter.data.model;

import com.example.mounter.data.RealmConverter;
import com.google.android.gms.maps.model.LatLng;

import org.bson.types.ObjectId;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import io.realm.mongodb.User;

import static com.example.mounter.Mounter.mounter;

public class RideRequestModel extends RealmObject {
    @PrimaryKey ObjectId _id = new ObjectId();
    @Required
    private ObjectId _driverId;
    @Required
    private ObjectId _passengerId;
    @Required
    private ObjectId _ridePostingId;
    @Required
    private String _partition = "1";

    public RideRequestModel(){
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
