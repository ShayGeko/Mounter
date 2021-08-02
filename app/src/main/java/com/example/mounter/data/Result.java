package com.example.mounter.data;


public enum Result {
    Success,
    Failure;

    private static String ERROR_MESSAGE = "login failed";

    public boolean isSuccess(){
        return this == Success;
    }
    public boolean isFailure(){
        return this == Failure;
    }
    public String getError(){
        if(isSuccess()) return null;

        return ERROR_MESSAGE;
    }
}