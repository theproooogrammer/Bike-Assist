package com.example.navdrawernew;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.navdrawernew.ui.trip.TripDataModel;

import java.util.Date;

public class DataBaseHelper1 extends SQLiteOpenHelper {

    public static String Database_Name = "None";
    public static final String Table_Name = "live";

    public static final String dbflow_meter1 = "flow_meter1";
    public static final String dbflow_meter2 = "flow_meter2";
    public static final String dbodometer = "odometer";
    public static final String dbavailableFuel = "availableFuel";

    public static final String F1HistoryTable = "f1history";
    public static final String f1value = "f1value";
    public static final String fillDate = "fill_date";

    public static final String lowFuelAlertTable = "alert_low_fuel";
    public static final String AlertType = "type";
    public static final String AlertValue = "value";


  /*
    public static final String dbdist_travelled = "dist_travelled";
    public static final String dbfuel_cosumed = "fuel_cosumed";
    public static final String dbavg_trip = "avg_trip";
    public static final String dbtrip_name = "trip_name";
  * */

    public static final String Table_Name2 = "active_trip";
    public static final String dbactive_trip = "active_trip";
    public static final String dbinitial_flowmeter2 = "initial_flowmeter2";
    public static final String dbinitial_odometer = "initial_odometer";

    public static final String Table_Name3 = "trip_history";
    public static final String dbtrip_name = "trip_name";
    public static final String dbtrip_date = "trip_date";
    public static final String dbconsumed_fuel = "consumed_fuel";
    public static final String dbtravelled_distance = "travelled_distance";
    public static final String dbavg_trip = "avg_trip";


    public static final String Table_Name1 = "test";
    public static final String dbavg = "avg";

    Cursor res;
    String result = "";


    public DataBaseHelper1(@Nullable Context context) {
        super(context, Database_Name, null, 1);

        Log.d("DataBaseHelper", " Database_Name "+Database_Name);
        try{
            Log.d("DataBaseHelper1", "INSERTING Values in DB: ");
            boolean result =insertFuelData();

            if (result==true)
                Log.d("DataBaseHelper1", "insertFuelData Inserted ");
            else
                Log.d("DataBaseHelper1", "insertFuelData Not  Inserted");

        } catch (Exception e) {
            Log.d("DataBaseHelper1 Excep", "insertFuelData  : " + e);
        }
        try {
            Log.d("DataBaseHelper1", "INSERTING Values in DB: ");
            boolean result = insertActiveTripData();

            if (result == true)
                Log.d("DataBaseHelper1", "insertActiveTripData Inserted ");
            else
                Log.d("DataBaseHelper1", "insertActiveTripData Not  Inserted");

        } catch (Exception e) {
            Log.d("DataBaseHelper1 Excep", "insertActiveTripData  : " + e);
        }

        try {
            Log.d("DataBaseHelper1", "INSERTING Values in DB: ");
            boolean result = insertAlertData();

            if (result == true)
                Log.d("DataBaseHelper1", "insertActiveTripData Inserted ");
            else
                Log.d("DataBaseHelper1", "insertActiveTripData Not  Inserted");

        }catch (Exception e){
            Log.d("DataBaseHelper1 Excep", "insertActiveTripData  : " + e);
        }

    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+Table_Name+" ( flow_meter1 float , flow_meter2 float , availableFuel float,odometer integer )");
        db.execSQL("create table "+Table_Name1+" (  avg text )  ");
        db.execSQL("create table " + Table_Name2 + " ( active_trip String ,trip_name String,trip_date String, initial_flowmeter2 String , initial_odometer String )");
        db.execSQL("create table " + Table_Name3 + " ( trip_name String ,trip_date String, consumed_fuel float , travelled_distance float, avg_trip float )");
        db.execSQL("Create Table " + lowFuelAlertTable + " ( " + AlertType + " String, " + AlertValue + " String)");
        db.execSQL("Create Table " + F1HistoryTable + " ( " + f1value + " String, " + fillDate + " String)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Table_Name);
        db.execSQL("DROP TABLE IF EXISTS "+Table_Name1);
        db.execSQL("DROP TABLE IF EXISTS " + Table_Name2);
        db.execSQL("DROP TABLE IF EXISTS " + Table_Name3);
        db.execSQL("DROP TABLE IF EXISTS " + F1HistoryTable);
        db.execSQL("DROP TABLE IF EXISTS " + lowFuelAlertTable);
        onCreate(db);
    }

    public Boolean addF1History(String value, String date) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contantValues = new ContentValues();
        contantValues.put(f1value, value);
        contantValues.put(fillDate, date);
        long result = db.insert(F1HistoryTable, null, contantValues);
        return true;
    }


    public Boolean setAlert(String type, String value) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contantValues = new ContentValues();
        contantValues.put(AlertType, type);
        contantValues.put(AlertValue, value);
        long result = db.update(lowFuelAlertTable, contantValues, null, null);
        return true;
    }

    public String[] getAlert() {
        String[] ress = new String[2];
        SQLiteDatabase db = this.getWritableDatabase();
        res = db.rawQuery("select * from " + lowFuelAlertTable, null);
        if (res.moveToFirst()) {
            ress[0] = res.getString(0);
            ress[1] = res.getString(1);
        } else {
            ress[0] = "0";
            ress[1] = "0";
        }
        return ress;

    }

    public boolean addF1history(String value) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contantValues = new ContentValues();
        contantValues.put(f1value, String.valueOf(Float.valueOf(value) / 1000));
        contantValues.put(fillDate, String.valueOf(new Date()));
        long result = db.insert(F1HistoryTable, null, contantValues);
        return result != -1;

    }

    public String[] getF1History() {

        String[] ress = new String[2];
        SQLiteDatabase db = this.getWritableDatabase();


        res = db.rawQuery("select * from " + F1HistoryTable + " order by " + fillDate + " desc limit 1", null);
        //res = db.rawQuery("select * from " + Table_Name, null);

        if (res.moveToFirst()) {
            ress[0] = res.getString(0);
            ress[1] = res.getString(1);
        } else {
            ress[0] = "0";
            ress[1] = "0";
        }
        res.close();
        return ress;
    }

    public static String getDatabase_Name() {
        return Database_Name;
    }

    public static void setDatabase_Name(String db) {
        Database_Name=db;
    }


    public boolean setDbFlowMeter1(float flow_meter1){
        Log.d("SETDBF1 new : ", String.valueOf(flow_meter1));
        addF1history(String.valueOf(flow_meter1));
        Float oldflow_meter1 = Float.valueOf(getDbFlowMeter1());
        Log.d("SETDBF1 old: ", String.valueOf(oldflow_meter1));
        Float allflow_meter1 = oldflow_meter1 + flow_meter1;
        Log.d("SETDBF1: ", String.valueOf(allflow_meter1));

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + Table_Name + " SET flow_meter1 = " + "'" + allflow_meter1 + "' ");
        return true;


    }

    public boolean resetDbFlowMeter1(float flow_meter1) {

        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("UPDATE " + Table_Name + " SET flow_meter1 = " + "'" + flow_meter1 + "' ");
        return true;


    }

    public boolean setDbavailableFuel(float availableFuel){

        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("UPDATE "+Table_Name+" SET availableFuel = "+"'"+availableFuel+"' ");
        return true;

    }

    public boolean setDbOdoMeter(String odometer){

        int oldodometer = Integer.valueOf(getDbOdoMeter());
        int allodometer = oldodometer + Integer.valueOf(odometer);
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("UPDATE " + Table_Name + " SET odometer = " + "'" + allodometer + "' ");
        return true;

    }

    public boolean resetDbOdoMeter(String odometer) {

        int oldodometer = Integer.valueOf(odometer);
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + Table_Name + " SET odometer = " + "'" + oldodometer + "' ");
        return true;

    }

    public boolean setDbFlowMeter2(float flow_meter2){

        Float oldflow_meter2 = Float.valueOf(getDbFlowMeter2());
        Float allflow_meter2 = oldflow_meter2 + Float.valueOf(flow_meter2);
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + Table_Name + " SET flow_meter2 = " + "'" + allflow_meter2 + "' ");
        return true;

    }

    public boolean resetDbFlowMeter2(float flow_meter2) {

        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("UPDATE "+Table_Name+" SET flow_meter2 = "+"'"+flow_meter2+"' ");
        return true;

    }

    public String getDbFlowMeter1() {
        result = "";
        SQLiteDatabase db=this.getWritableDatabase();
        res = db.rawQuery("select flow_meter1 from " + Table_Name, null);
        if (res.moveToFirst()) {
            result = res.getString(0);
        }
        res.close();
        if (result.equalsIgnoreCase("")) {
            result = "0";
        }
        return result;

    }

    public String getDbavailableFuel() {
        result = "";
        SQLiteDatabase db = this.getWritableDatabase();
        res = db.rawQuery("select availableFuel from " + Table_Name, null);
        if (res.moveToFirst()) {
            result = res.getString(0);
        }
        res.close();
        if (result.equalsIgnoreCase("")) {
            result = "0";
        }

        return result;

    }

    public String getDbOdoMeter() {
        result = "";
        SQLiteDatabase db=this.getWritableDatabase();
        res = db.rawQuery("select odometer from " + Table_Name, null);
        if (res.moveToFirst()) {
            result = res.getString(0);
        }
        res.close();
        if (result.equalsIgnoreCase("")) {
            result = "0";
        }
        return result;

    }

    public String getDbFlowMeter2() {
        result = "";
        SQLiteDatabase db=this.getWritableDatabase();
        res = db.rawQuery("select flow_meter2 from " + Table_Name, null);
        if (res.moveToFirst()) {
            result = res.getString(0);
        }
        res.close();
        if (result.equalsIgnoreCase("")) {
            result = "0";
        }
        return result;

    }

    public String getTankCapacity() {
        result = "";
        SQLiteDatabase db = this.getWritableDatabase();
        res = db.rawQuery("select tank_capacity from " + Table_Name, null);
        if (res.moveToFirst()) {
            result = res.getString(0);
        }
        res.close();
        if (result.equalsIgnoreCase("")) {
            result = "0";
        }
        return result;
    }
/*
    public boolean addData(String trip_name,String dist_travelled , String fuel_cosumed , String avg_trip){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contantValues= new ContentValues();
        contantValues.put(dbtrip_name,trip_name);
        contantValues.put(dbdist_travelled,dist_travelled);
        contantValues.put(dbfuel_cosumed,fuel_cosumed);
        contantValues.put(dbavg_trip,avg_trip);
        Log.d("ADebugTag", "Value DB: " + avg_trip);
        long result = db.insert(Table_Name,null,contantValues);

        if (result==-1)
            return false;
        else
            return true;
    }

    */


    public boolean insertFuelData(){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contantValues= new ContentValues();
        contantValues.put(dbflow_meter1,0.0);
        contantValues.put(dbflow_meter2,0.0);
        contantValues.put(dbavailableFuel,0.0);
        contantValues.put(dbodometer,0);
        long result = db.insert(Table_Name,null,contantValues);
        return result != -1;

    }

    public boolean insertAlertData() {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contantValues = new ContentValues();
        contantValues.put(AlertType, "");
        contantValues.put(AlertValue, "");
        long result = db.insert(lowFuelAlertTable, null, contantValues);
        return result != -1;

    }

    public boolean insertActiveTripData() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contantValues = new ContentValues();
        contantValues.put(dbactive_trip, "0");
        contantValues.put(dbtrip_name, "");
        contantValues.put(dbtrip_date, "");
        contantValues.put(dbinitial_flowmeter2, "0");
        contantValues.put(dbinitial_odometer, "0");
        long result = db.insert(Table_Name2, null, contantValues);
        return result != -1;

    }


    public Cursor getAllData(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+Table_Name,null);
        return res;
    }

    public boolean insertTest(String avg){

        Log.d("insertTest", "SETTING Values in DB: "+avg);
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contantValues= new ContentValues();
        contantValues.put(dbavg,avg);
        long result = db.insert(Table_Name1,null,contantValues);
        return result != -1;
    }

    public Cursor getTest(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+Table_Name1,null);
        return res;
    }


    public boolean addActiveTrip(TripDataModel trip) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contantValues = new ContentValues();

        contantValues.put(dbactive_trip, trip.getActive());
        contantValues.put(dbtrip_name, trip.getTripName());
        contantValues.put(dbtrip_date, trip.getSetDate());
        if (trip.getFuel().equalsIgnoreCase("")) {
            contantValues.put(dbinitial_flowmeter2, getDbFlowMeter2());
        } else {
            contantValues.put(dbinitial_flowmeter2, trip.getFuel());
        }

        if (trip.getDistance().equalsIgnoreCase("")) {
            contantValues.put(dbinitial_odometer, getDbOdoMeter());
        } else {
            contantValues.put(dbinitial_odometer, trip.getDistance());
        }

        long result = db.update(Table_Name2, contantValues, null, null);
        return result != -1;
    }

    public boolean addTripHistory(TripDataModel trip) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contantValues = new ContentValues();
        contantValues.put(dbtrip_name, trip.getTripName());
        contantValues.put(dbtrip_date, trip.getSetDate());
        contantValues.put(dbconsumed_fuel, Float.valueOf(trip.getFuel()));
        contantValues.put(dbtravelled_distance, Float.valueOf(trip.getDistance()));
        contantValues.put(dbavg_trip, Float.valueOf(trip.getMileage()));
        long result = db.insert(Table_Name3, null, contantValues);
        return result != -1;
    }

    public String getDbActiveTrip() {

        result = "0";
        SQLiteDatabase db = this.getWritableDatabase();
        res = db.rawQuery("select active_trip from " + Table_Name2, null);

        if (res.moveToFirst()) {
            result = res.getString(0);

        }

        res.close();
        return result;

    }

    public String getDbActiveTripName() {
        result = "0";
        SQLiteDatabase db = this.getWritableDatabase();
        res = db.rawQuery("select trip_name from " + Table_Name2, null);
        if (res.moveToFirst()) {
            result = res.getString(0);
        }
        res.close();
        return result;

    }

    public String getDbActiveTripDate() {
        result = "0";
        SQLiteDatabase db = this.getWritableDatabase();
        res = db.rawQuery("select trip_date from " + Table_Name2, null);
        if (res.moveToFirst()) {
            result = res.getString(0);
        }
        res.close();
        return result;

    }


    public String getInitialFlow_meter2() {
        result = "0";
        SQLiteDatabase db = this.getWritableDatabase();
        res = db.rawQuery("select initial_flowmeter2 from " + Table_Name2, null);
        if (res.moveToFirst()) {
            result = res.getString(0);
        }
        res.close();
        return result;

    }

    public String getInitialOdo_meter2() {
        result = "0";
        SQLiteDatabase db = this.getWritableDatabase();

        res = db.rawQuery("select initial_odometer from " + Table_Name2, null);
        if (res.moveToFirst()) {
            result = res.getString(0);
        }
        res.close();
        return result;

    }

    public boolean deactivateTrip(TripDataModel trip) throws Exception {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("active_trip", "0");
        db.update(Table_Name2, contentValues, null, null);

        boolean val = false;
        if (getDbActiveTrip().equalsIgnoreCase("0")) {
            val = addTripHistory(trip);

        }
        return val;
    }




}
