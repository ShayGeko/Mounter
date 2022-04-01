package com.example.mounter.ui.ridePostingCreator;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mounter.Mounter;

import org.jetbrains.annotations.NotNull;

public class RidePostingCreatorViewModelFactory implements ViewModelProvider.Factory {
    private Mounter app;

    public RidePostingCreatorViewModelFactory(Mounter app){
        this.app = app;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        return (T) new RidePostingCreatorViewModel(app);
    }
}
