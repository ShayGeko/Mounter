package com.example.mounter.ui.login;


import com.example.mounter.R;

public enum LoginResult {
    Success,
    Failure;


    public boolean isSuccess(){
        return this == Success;
    }
    public boolean isFailure(){
        return this == Failure;
    }
    public String getError(){
        if(isSuccess()) return null;

        return String.valueOf(R.string.login_failed);
    }
}