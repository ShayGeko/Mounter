package com.example.mounter.directions.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DirectionsResult {
    @JsonProperty
    public List<Route> routes;
}

