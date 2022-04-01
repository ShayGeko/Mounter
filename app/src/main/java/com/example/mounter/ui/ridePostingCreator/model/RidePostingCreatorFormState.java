package com.example.mounter.ui.ridePostingCreator.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.mounter.BR;

import static com.example.mounter.common.MounterStringUtils.containsData;

public class RidePostingCreatorFormState extends BaseObservable {

    private String destinationAddress;
    private String originAddress;
    private String departureTime;
    private String estimatedPrice;
    private String description;

    @Bindable
    public String getDestinationAddress(){
        return destinationAddress;
    }
    public void setDestinationAddress(String value){
        destinationAddress = value;
        notifyPropertyChanged(BR.valid);
    }

    @Bindable
    public String getOriginAddress(){
        return originAddress;
    }
    public void setOriginAddress(String value) {
        originAddress = value;
        notifyPropertyChanged(BR.valid);
    }

    public String getDepartureTime(){
        return departureTime;
    }
    public void setDepartureTime(String value) {
        departureTime = value;
        notifyPropertyChanged(BR.valid);
    }

    public String getEstimatedPrice(){
        return estimatedPrice;
    }
    public void setEstimatedPrice(String value) {
        estimatedPrice = value;
        notifyPropertyChanged(BR.valid);
    }

    public String getDescription(){
        return description;
    }
    public void setDescription(String value) {
        description = value;
        notifyPropertyChanged(BR.valid);
    }

    @Bindable
    boolean isValid() {
        return containsData(destinationAddress) && containsData(originAddress) && containsData(departureTime);
    }
}
