package com.example.navdrawernew;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SessionManager {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public boolean addItem(Context activity, VehicleDataModel vehicle) {
        String VItem;
        VItem = vehicle.getBluetoothAddress()
                + "," + vehicle.getVehicleModel()
                + "," + vehicle.getVehicleNumber()
                + "," + vehicle.getvehicleTank()
                + "," + vehicle.getDataBaseName();

        String vehicleList = getStringFromPreferences(activity,null,"vehicle");

        if(vehicleList!=null ){
            //MAKING IT NULL BEFORE REPLACING THE NEW VALUE
            vehicleListZero(activity ,null);
            vehicleList = VItem;

        }
        else{ vehicleList = VItem; }

        // Save in Shared Preferences
        return putStringInPreferences(activity,vehicleList,"vehicle");
    }

    private static boolean vehicleListZero(Context activity,String VItem){
        Log.d("MainActivity", "vehicleList NUll");
        return putStringInPreferences(activity,VItem,"vehicle");
    }

    private static String getStringFromPreferences(Context activity,String defaultValue,String key){
        SharedPreferences sharedPreferences = activity.getSharedPreferences(key,Activity.MODE_PRIVATE);
        String temp = sharedPreferences.getString(key, defaultValue);
        return temp;
    }

    private static boolean putStringInPreferences(Context activity,String nick,String key){
        SharedPreferences sharedPreferences = activity.getSharedPreferences(key,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, nick);
        editor.commit();
        return true;
    }


    public static String[] getFavoriteList(Context activity){
        String vehicleList = getStringFromPreferences(activity,null,"vehicle");
        return convertStringToArray(vehicleList);
    }
    private static String[] convertStringToArray(String str){
        String[] arr = str.split(",");
        return arr;
    }


    public SessionManager(Context context){
        sharedPreferences=context.getSharedPreferences("DataBaseKey",0);
        editor=sharedPreferences.edit();
        editor.apply();
    }


    public void setDatabase(String DataBase){
        editor.putString("DataBase",DataBase);

        editor.commit();
    }

    public String getDatabase(){
        Log.d("SessionManager", "SharedPreferences : " + sharedPreferences.getString("DataBase","Vehicle Name"));
        return sharedPreferences.getString("DataBase" ,null);
    }

}
