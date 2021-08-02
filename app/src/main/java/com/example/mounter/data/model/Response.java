package com.example.mounter.data.model;

import com.example.mounter.R;

public class Response<T> {
    private enum Status {PENDING, SUCCESS, FAILURE};

    private T data;
    private Status status;
    private String error;

    private Response(){}
    public static <T> Response<T> Pending(){
        Response response = new Response();

        response.status = Status.PENDING;

        return response;
    }
    public static <T> Response<T> Success(T data) {
        Response response = new Response();
        response.data = data;
        response.status = Status.SUCCESS;

        return response;
    }
    public static <T> Response<T> Error(String errorMessage) {
        Response response = new Response();
        response.error = errorMessage;
        response.status = Status.FAILURE;

        return response;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }
    public boolean isFailure(){
        return status == Status.FAILURE;
    }
    public boolean isPending(){
        return status == Status.PENDING;
    }

    public String getError() {
        if(!isFailure()) return null;
        return error;
    }
}