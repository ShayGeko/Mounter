package com.example.mounter.data.model;


public enum Result {
    Pending,
    Success,
    Failure;

    private static String ERROR_MESSAGE = "login failed";

    public boolean isPending(){
        return this == Pending;
    }
    public boolean isSuccess(){
        return this == Success;
    }
    public boolean isFailure(){
        return this == Failure;
    }
    public String getError(){
        if(!isFailure()) return null;

        return ERROR_MESSAGE;
    }
}