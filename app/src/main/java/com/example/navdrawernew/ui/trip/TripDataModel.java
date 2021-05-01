package com.example.navdrawernew.ui.trip;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TripDataModel {
    private String tripName;
    private String setDate;
    private String fuel;
    private String distance;
    private String mileage;
    private String active;

    public TripDataModel(String active, String tripName, String setDate, String fuel, String distance, String mileage) {
        this.setActive(active);
        this.setTripName(tripName);
        this.setSetDate(setDate);
        this.setFuel(fuel);
        this.setDistance(distance);
        this.setMileage(mileage);
    }

    public TripDataModel() {
        this.setActive("1");
        this.setTripName("");
        SimpleDateFormat sf = new SimpleDateFormat("EEEE, MMMM dd, hh:mm a");
        Date d = new Date();
        this.setSetDate(sf.format(d));
        this.setFuel("");
        this.setDistance("");
        this.setMileage("");
    }

    public String getTripName() {
        return tripName;
    }

    public String getSetDate() {
        return setDate;
    }

    public String getFuel() {
        return fuel;
    }

    public String getDistance() {
        return distance;
    }

    public String getMileage() {
        return mileage;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public void setSetDate(String setDate) {
        this.setDate = setDate;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public void setMileage() {
        this.mileage = String.valueOf(Float.valueOf(this.distance) / Float.valueOf(this.fuel));
    }
}
