package com.example.mounter.data.realmModels;

import com.example.mounter.Mounter;
import com.example.mounter.data.RealmConverter;
import com.google.android.gms.maps.model.LatLng;

import org.bson.types.ObjectId;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import io.realm.mongodb.User;

import static com.example.mounter.Mounter.mounter;
import static com.example.mounter.common.MounterDateUtil.getNumberOfDaysSinceEpoch;

public class RidePostingModel extends RealmObject {
    @PrimaryKey
    @Required
    ObjectId _id = new ObjectId();
    private ObjectId _driverId;
    private ObjectId _creatorId;
    @Required
    private String _partition = Mounter.realmPartition;
    @Required
    private RealmList<ObjectId> passengerIds = new RealmList<>();
    private String originAddress;
    private String destinationAddress;
    private String departureTime;
    private int departureTimeInDays;
    private String estimatedPrice;
    @Required
    private RealmList<Double> destinationLatLng = new RealmList<>();
    @Required
    private RealmList<Double> originLatLng = new RealmList<>();
    private String description;

    private RealmList<UserInfoModel> passengers = new RealmList<>();

    private RealmList<RideRequestModel> rideRequests  = new RealmList<>();

    @LinkingObjects("ridePostings")
    private final RealmResults<UserInfoModel> driver = null;

    @LinkingObjects("myRidePostings")
    private final RealmResults<UserInfoModel> creator = null;

    public RidePostingModel(){
        _driverId = new ObjectId(mounter.currentUser().getId());
    }
    public RidePostingModel(User user){
        _driverId = new ObjectId(user.getId());
    }

    private RidePostingModel(String originAddress, String destinationAddress, String departureTime, String description){
        this.originAddress = originAddress;
        this.destinationAddress = destinationAddress;
        setDepartureTime(departureTime);
        this.description = description;
        // TODO departureDate
    }
    public static RidePostingModel createByPassenger(User user,
                                                     String originAddress,
                                                     String destinationAddress,
                                                     String departureTime,
                                                     String description) {

        RidePostingModel ridePosting = new RidePostingModel(
                originAddress,
                destinationAddress,
                departureTime,
                description);

        ridePosting.passengerIds.add(new ObjectId(user.getId()));
        ridePosting._creatorId = new ObjectId(user.getId());
        return ridePosting;
    }
    public static RidePostingModel createByDriver(User user,
                                                  String originAddress,
                                                  String destinationAddress,
                                                  String departureTime,
                                                  String description,
                                                  String estimatedPrice){
        RidePostingModel ridePosting = new RidePostingModel(
                originAddress,
                destinationAddress,
                departureTime,
                description);
        ridePosting.estimatedPrice = estimatedPrice;
        ridePosting._driverId = new ObjectId(user.getId());
        ridePosting._creatorId = new ObjectId(user.getId());

        return ridePosting;
    }

    public ObjectId getId(){
        return _id;
    }
    public ObjectId getDriverId(){
        return  _driverId;
    }
    public void setDriverId(ObjectId id){
        _driverId = id;
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
        this.departureTimeInDays = getNumberOfDaysSinceEpoch(departureTime); //Sets departureTimeInDays
    }

    /**
     *
     * @return a string representing price estimation for this {@link RidePostingModel}
     */
    public String getEstimatedPrice() {
        return estimatedPrice;
    }

    /**
     * sets the estimated price for this {@link RidePostingModel}
     * @param estimatedPrice
     */
    public void setEstimatedPrice(String estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
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

    public boolean hasDestinationLatLng(){
        return this.destinationLatLng.size() == 2;
    }
    public boolean hasOriginLatLng(){
        return this.originLatLng.size() == 2;
    }

    public void addRideRequest(RideRequestModel rideRequest){
        rideRequests.add(rideRequest);
    }
    public void addPassenger(UserInfoModel passenger){
        passengers.add(passenger);
    }


    public UserInfoModel getCreator(){
        if(creator.isEmpty()) return null;

        return creator.first();
    }

    public boolean needsAdriver() {
        return this._driverId == null;
    }

    public ObjectId getCreatorId() {
        return _creatorId;
    }
}
