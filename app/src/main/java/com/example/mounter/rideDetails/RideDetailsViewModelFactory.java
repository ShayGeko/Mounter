package com.example.mounter.rideDetails;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;

public class RideDetailsViewModelFactory implements ViewModelProvider.Factory {
    private ObjectId rideId;

    public RideDetailsViewModelFactory(ObjectId rideId){
        this.rideId = rideId;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        return (T) new RideDetailsViewModel(rideId);
    }
}
