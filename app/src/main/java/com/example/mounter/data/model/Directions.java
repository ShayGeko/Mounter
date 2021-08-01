package com.example.mounter.data.model;

import org.json.JSONArray;

public class Directions {
    private JSONArray geocodedWaypoints;
    private Route route;

    private boolean isLoaded;

    public void setGeocodedWaypoints(JSONArray geocodedWaypoints){
        this.geocodedWaypoints = geocodedWaypoints;
    }
    public JSONArray getGeocodedWaypoints(){
        return geocodedWaypoints;
    }
    public void setRoute(Route route){
        this.route = route;
    }
    public Route getRoute(){
        return route;
    }

    public boolean isLoaded(){
        return this.isLoaded;
    }
    public void setLoaded(){
        isLoaded = true;
    }
}
