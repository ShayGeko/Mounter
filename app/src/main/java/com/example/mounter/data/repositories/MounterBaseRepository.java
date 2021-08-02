package com.example.mounter.data.repositories;


import android.app.Application;

import com.example.mounter.Mounter;

public class MounterBaseRepository {
    protected Mounter app;
    public MounterBaseRepository(){}
    public MounterBaseRepository(Mounter application){
        app = application;
    }
}
