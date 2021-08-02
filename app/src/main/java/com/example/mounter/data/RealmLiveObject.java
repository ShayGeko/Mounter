package com.example.mounter.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class RealmLiveObject<T extends RealmModel> extends MutableLiveData<T> {
    private T object;

    private final RealmChangeListener<T> listener =
            object -> setValue(object);

    public RealmLiveObject(T realmObject) {
        object = realmObject;
    }

    @Override
    protected void onActive() {
        RealmObject.addChangeListener(object, listener);
    }

    @Override
    protected void onInactive() {
        RealmObject.addChangeListener(object, listener);
    }
}
