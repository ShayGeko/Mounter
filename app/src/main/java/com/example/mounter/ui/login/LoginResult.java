package com.example.mounter.ui.login;


public enum LoginResult {
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