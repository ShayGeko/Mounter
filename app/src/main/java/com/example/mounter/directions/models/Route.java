package com.example.mounter.directions.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Route {
    @JsonProperty("overview_polyline")
    public OverviewPolyLine overviewPolyLine;
}
