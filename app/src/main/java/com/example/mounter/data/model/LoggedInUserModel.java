package com.example.mounter.data.model;

import org.bson.types.ObjectId;

import io.realm.annotations.PrimaryKey;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUserModel {

    @PrimaryKey
    private ObjectId userId;
    private String displayName;

    public LoggedInUserModel(ObjectId userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }
}