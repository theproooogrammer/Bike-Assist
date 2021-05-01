package com.example.navdrawernew.ui.fuel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.navdrawernew.DATA;

public class FuelViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    public FuelViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is fuel fragment");

    }

    public LiveData<String> getaAvailableFuel(){

        return DATA.getAvailableFuel();
    }

    public LiveData<String> getF1() {

        return DATA.getFlowMeter1();
    }


    public LiveData<String> getText() {
        return mText;
    }
}