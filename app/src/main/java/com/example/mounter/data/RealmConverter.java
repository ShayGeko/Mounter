package com.example.mounter.data;

import com.google.android.gms.maps.model.LatLng;

import io.realm.RealmList;

public class RealmConverter {
    public static RealmList<Double> toRealmList(LatLng coords){
        RealmList<Double> realmList = new RealmList<>();
        realmList.add(coords.latitude);
        realmList.add(coords.longitude);

        return realmList;
    }


    public static LatLng toLatLng(RealmList<Double> coords){
        if(coords == null || coords.size() != 2) return null;

        return new LatLng(coords.first(), coords.last());
    }
}
