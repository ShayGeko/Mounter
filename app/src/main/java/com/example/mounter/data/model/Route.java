package com.example.mounter.data.model;


import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONObject;

public class Route {
    public String summary;
    public JSONArray legs;
    public String copyrights;
    public OverviewPolyline overviewPolyline;
    public JSONArray warnings;
    public JSONArray waypointOrder;
    public LatLngBounds bounds;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public JSONArray getLegs() {
        return legs;
    }

    public void setLegs(JSONArray legs) {
        this.legs = legs;
    }

    public String getCopyrights() {
        return copyrights;
    }

    public void setCopyrights(String copyrights) {
        this.copyrights = copyrights;
    }

    public OverviewPolyline getOverviewPolyline() {
        return overviewPolyline;
    }

    public void setOverviewPolyline(OverviewPolyline overviewPolyline) {
        this.overviewPolyline = overviewPolyline;
    }

    public JSONArray getWarnings() {
        return warnings;
    }

    public void setWarnings(JSONArray warnings) {
        this.warnings = warnings;
    }

    public JSONArray getWaypointOrder() {
        return waypointOrder;
    }

    public void setWaypointOrder(JSONArray waypointOrder) {
        this.waypointOrder = waypointOrder;
    }

    public LatLngBounds getBounds() {
        return bounds;
    }

    public void setBounds(LatLngBounds bounds) {
        this.bounds = bounds;
    }
}
