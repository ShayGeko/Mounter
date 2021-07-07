package com.example.mounter.data.model;

import com.example.mounter.data.RealmConverter;
import com.google.android.gms.maps.model.LatLng;

import org.bson.types.ObjectId;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import io.realm.mongodb.User;

import static com.example.mounter.Mounter.mounter;

public class RidePostingModel extends RealmObject {
    @PrimaryKey ObjectId _id = new ObjectId();
    @Required
    private ObjectId _driverId;
    @Required
    private String _partition = "1";
    private String originAddress;
    private String destinationAddress;
    private Date departureTime;
    @Required
    private RealmList<Double> destinationLatLng = new RealmList<>();
    @Required
    private RealmList<Double> originLatLng = new RealmList<>();
    private String description;

    public RidePostingModel(){
        _driverId = new ObjectId(mounter.currentUser().getId());
    }
    public RidePostingModel(User user){
        _driverId = new ObjectId(user.getId());
    }
    public ObjectId get_id(){
        return _id;
    }
    public ObjectId get_driverId(){
        return  _driverId;
    }
    public String get_partition() {
        return _partition;
    }

    public String getOriginAddress() {
        return originAddress;
    }

    public void setOriginAddress(String originAddress) {
        this.originAddress = originAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public RealmList<Double> getDestinationLatLng() {
        return destinationLatLng;
    }

    public LatLng getDestinationActualLatLng(){
        return RealmConverter.toLatLng(destinationLatLng);
    }


    public void setDestinationLatLng(LatLng destinationLatLng) {
        this.destinationLatLng = RealmConverter.toRealmList(destinationLatLng);
    }

    public RealmList<Double> getOriginLatLng() {
        return originLatLng;
    }

    public LatLng getOriginActualLatLng() {
        return RealmConverter.toLatLng(originLatLng);
    }

    public void setOriginLatLng(RealmList<Double> originLatLng) {
        this.originLatLng = originLatLng;
    }
    public void setOriginLatLng(LatLng originLatLng) {
        this.originLatLng = RealmConverter.toRealmList(originLatLng);
    }

    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }
}
