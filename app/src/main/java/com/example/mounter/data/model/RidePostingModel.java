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
    @PrimaryKey
    ObjectId _id = new ObjectId();
    @Required
    private ObjectId _driverId;
    @Required
    private String _partition = "1";

    @Required
    private RealmList<ObjectId> passengerIds;

    private String originAddress;
    private String destinationAddress;
    private String departureTime;
    private int departureTimeInDays;


    public String getEstimatedPrice() {
        return estimatedPrice;
    }

    public void setEstimatedPrice(String estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }

    private String estimatedPrice;
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

    public ObjectId getId(){
        return _id;
    }
    public ObjectId getDriverId(){
        return  _driverId;
    }
    public String getPartition() {
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

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
        convertDateToInt(); //Sets departureTimeInDays
    }

    public RealmList<ObjectId> getPassengerIds(){
        return passengerIds;
    }
    public void enrollUserOnTheRide(String userId){
        passengerIds.add(new ObjectId(userId));
    }
    public void enrollUserOnTheRide(ObjectId userId){
        passengerIds.add(userId);
    }
    public void kickFromTheRide(String userId){
        passengerIds.remove(new ObjectId(userId));
    }
    public void kickFromTheRide(ObjectId userId){
        passengerIds.remove(userId);
    }
    public RealmList<Double> getDestinationLatLng() {
        return destinationLatLng;
    }

    /**
     *
     * @return LatLng object created based on the RealmList stored in the model
     *
=======
    /**
     *
     * @return LatLng object created based on the RealmList stored in the model
>>>>>>> master
     */
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

    private void convertDateToInt(){
        String[] date = getDepartureTime().split(" ");
        String[] dateValues = date[0].split("/");   //[0] = days, [1] = months, [2] = years

        departureTimeInDays = (Integer.parseInt(dateValues[0]) + getMonthValue(dateValues[1]) + (Integer.parseInt(dateValues[2]) * 365));
    }

    /**
     *
     * @param month
     * @return approximate number of days in the specified month
     */
    public int getMonthValue(String month){
        switch(month){
            case "Jan":
                return 31;
            case "Feb":
                return 29 + 31;
            case "Mar":
                return 31;
            case "Apr":
                return 30;
            case "May":
                return 31;
            case "Jun":
                return 30;
            case "Jul":
                return 31;
            case "Aug":
                return 31;
            case "Sep":
                return 30;
            case "Oct":
                return 31;
            case "Nov":
                return 30;
            case "Dec":
                return 31;
        }
        return 31;
    }
}
