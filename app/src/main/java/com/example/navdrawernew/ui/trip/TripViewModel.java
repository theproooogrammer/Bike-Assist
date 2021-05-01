package com.example.navdrawernew.ui.trip;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.navdrawernew.DATA;

public class TripViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TripViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is tools fragment");
    }

    public LiveData<String> getaAvailableFuel() {

        return DATA.getAvailableFuel();
    }

    public LiveData<String> getOdometer() {
        return DATA.getOdometer();
    }

    public LiveData<String> getFlowMeter2() {
        return DATA.getFlowMeter2();
    }

    public LiveData<String> getText() {
        return mText;
    }
}