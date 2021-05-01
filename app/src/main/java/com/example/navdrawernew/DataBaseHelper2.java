package com.example.navdrawernew;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DataBaseHelper2 extends SQLiteOpenHelper {

    public static final String Database_Name = "user.db";

    public static final String Table_Name="user";
    public static final String dbBT_no = "BT_no";
    public static final String dbvehicle = "vehicle";
    public static final String dbvehicle_no = "vehicle_no";
    public static final String dbtank = "tank";
    public static final String dbdatabasename = "databasename";


    public DataBaseHelper2(@Nullable Context context) {
        super(context, Database_Name, null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    //HERE BT_no IS PRIMARY KEY ST IS AFTER TESTING
        db.execSQL("create table "+Table_Name+" ( BT_no text , vehicle text , vehicle_no text , tank text, databasename text )");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }

    /*

    public String getDbvehicle(String temp){

        String tempV=null;
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+Table_Name+" where databasename = ' "+temp+" ' ",null);
        //Cursor res=db.rawQuery("select * from "+Table_Name,null);


       // if (res.getCount()==0) return "No Vehicle Added";

        if (res.moveToNext()){

            res.getString(1);
            Log.d("MainActivity", "MainActivity "+res.getString(1));
            Log.d("MainActivity", "MainActivity "+res.getString(2));
            Log.d("MainActivity", "MainActivity "+res.getString(3));
            Log.d("MainActivity", "MainActivity "+res.getString(4));
            res.getString(1);


            Log.d("MainActivity", "MainActivity "+res.getString(1));
            tempV=res.getString(1);

        }

        return tempV;
    }

    public String getDbvehicle_no(String dbvehicle){

        String tempV=null;
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select vehicle from "+Table_Name+"where vehicle="+dbvehicle,null);

        if (res.getCount()==0) return "No Vehicle Added";

        while (res.moveToNext()){ tempV=res.getString(0); }

        return tempV;
    }

    public String getDbtank(String database_Name){

        String tempV=null;
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select tank from "+Table_Name +" where databasename = "+database_Name ,null);

        if (res.getCount()==0) return "No Vehicle Added";

        while (res.moveToNext()){ tempV=res.getString(0); }

        return tempV;
    }

     */

    public Boolean addVehicle(VehicleDataModel vehicle) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contantValues = new ContentValues();

        contantValues.put(dbBT_no, vehicle.getBluetoothAddress());
        contantValues.put(dbvehicle, vehicle.getVehicleModel());
        contantValues.put(dbvehicle_no, vehicle.getVehicleNumber());
        contantValues.put(dbtank, vehicle.getvehicleTank());
        contantValues.put(dbdatabasename, vehicle.getDataBaseName());

        long result = db.insert(Table_Name, null, contantValues);

        return result != -1;
    }

    public Boolean addVehicle(String BT_no,String vehicle,String vehicle_no,String tank,String databasename){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contantValues= new ContentValues();

        contantValues.put(dbBT_no,BT_no);
        contantValues.put(dbvehicle,vehicle);
        contantValues.put(dbvehicle_no,vehicle_no);
        contantValues.put(dbtank,tank);
        contantValues.put(dbdatabasename,databasename);

        long result = db.insert(Table_Name,null,contantValues);

        return result != -1;
    }


    public Cursor getVehicleData(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+Table_Name,null);
        return res;
    }


    public ArrayList getVehicleList() {
        VehicleDataModel vehicleDataModel;
        ArrayList<VehicleDataModel> list = new ArrayList<>();
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+Table_Name,null);

        if (res.getCount()==0) Log.d("MainActivity", "MainActivity No List");

        while (res.moveToNext()){
            Log.d("DataBaseHelper2", "DataBaseHelper2 res.getString(4)"+res.getString(4));
            try {
                list.add(
                        vehicleDataModel=new VehicleDataModel(res.getString(0),
                                res.getString(1),
                                res.getString(2),
                                res.getString(3),
                                res.getString(4))
                );
            }catch (Exception e){
                Log.d("DataBaseHelper2", "DataBaseHelper2 Exception"+e);
            }

        }
        res.close();

        return list;
    }
}
