package com.example.navdrawernew;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

public class DATA {
    static private MutableLiveData<String> f1 = new MutableLiveData<>("7"); ///Fill
    static private MutableLiveData<String> f2 = new MutableLiveData<>("0"); ///Consume
    static private MutableLiveData<String>  odometer = new MutableLiveData<>("0");
    static private MutableLiveData<String> availableFuel = new MutableLiveData<>("0");
    static private String[] data;
    static private float tankCapacity = new Float(7);
    static DataBaseHelper1 dataBaseHelper1;
    static DataBaseHelper2 dataBaseHelper2;
    static String USER = "";


    public static MutableLiveData<String> getOdometer() {
        return odometer;
    }

    public static void setOdometer(String odo,Context context) {
        odo = String.valueOf(Math.round(Float.valueOf(odo))); /////////dont change this else error
        dataBaseHelper1 = new DataBaseHelper1(context);
        if(odometer==null){
            odometer= new MutableLiveData<>();

        }
        //if(Float.valueOf(String.valueOf(availableFuel))<0){
          //  return;
        //}

        DATA.odometer.postValue(odo);
        Log.d("ODOMETER VALUE ", odo);
        try{
            // Log.d("DATA", "INSERTING Values in DB: ");
            boolean result = dataBaseHelper1.setDbOdoMeter(odo);

            if (result==true) {
                Log.d("ODOMETER ", dataBaseHelper1.getDbOdoMeter());
            } else {
            }
            //Toast.makeText(context,"DATA NOT INSERTED",Toast.LENGTH_LONG).show();

        }catch (Exception e){
            Log.d("DATA Exception", "INSERTING Values : "+e);
        }


    }


    public static MutableLiveData<String> getFlowMeter1() {
        return f1;
    }

    public static void setFlowMeter1(String f11,Context context ) {
        dataBaseHelper1 = new DataBaseHelper1(context);
        if(f1==null){
            f1= new MutableLiveData<>();

        }
        DATA.f1.postValue(f11);
        //Log.d("FsetFlowMeter1",f11);


        try{
            // Log.d("DATA", "INSERTING Values in DB: ");
            boolean result = dataBaseHelper1.setDbFlowMeter1(Float.valueOf(f11));

            if (result==true)
                Log.d("F1 ", dataBaseHelper1.getDbFlowMeter1());
            else {
            }
            //Log.d("F1 ", " NOT INSERTING Values : ");

        }catch (Exception e){
            Log.d("DATA Exception", "INSERTING Values : "+e);
        }

    }

    public static MutableLiveData<String> getFlowMeter2() {
        return f2;
    }

    public static void setFlowMeter2(String f21,Context context) {
        dataBaseHelper1 = new DataBaseHelper1(context);
        if(f2==null){
            f2= new MutableLiveData<>();
        }

        //if(Float.valueOf(String.valueOf(availableFuel))<0){
          //return;
        //}
        DATA.f2.postValue(f21);


        try{
            //Log.d("DATA", "INSERTING Values in DB: ");
            boolean result = dataBaseHelper1.setDbFlowMeter2(Float.valueOf(f21));

            if (result==true) {
                Log.d("F2 ", dataBaseHelper1.getDbFlowMeter2());
            }
            else {
            }  //Log.d("DATA ", "NOT INSERTING Values : ");

        }catch (Exception e){
            // Log.d("DATA Exception", "INSERTING Values : "+e);
        }
    }

    public static MutableLiveData<String> getAvailableFuel() {
        return availableFuel;
    }

    public static void setAvailableFuel(Context context) {
        if(availableFuel==null){
            availableFuel= new MutableLiveData<>();

        }


        dataBaseHelper1 = new DataBaseHelper1(context);
        Float a,b,c;

        a = Float.valueOf(dataBaseHelper1.getDbFlowMeter1());
        b = Float.valueOf(dataBaseHelper1.getDbFlowMeter2());

        c =  (a-b)/1000;
        Log.d("F1", String.valueOf(a));
        Log.d("F2", String.valueOf(b));
        Log.d("AF", String.valueOf(c));

        if (c < 0.5) {
           // setFlowMeter1("5000", context);
            //setAvailableFuel(context);
            return;
        }
        availableFuel.postValue(String.valueOf(c));


        try{
            Log.d("DATA", "setting availableFuel ");
            boolean result = dataBaseHelper1.setDbavailableFuel(Float.valueOf(c));

            if (result==true)
                Log.d("DATA ", "true : ");
            else
                Log.d("DATA ", "false");

        }catch (Exception e){
            Log.d("DATA Exception", "INSERTING Values : "+e);
        }
    }


    public static void setDATA(String data1,Context context){


        data = data1.split(":");
        Log.d("SETDATA ", data[1]);
        switch(data[1]){
            case "f1":
                setFlowMeter1(data[2],context);
                setAvailableFuel(context);
                break;
            case "f2":
                setFlowMeter2(data[2],context);
                //Log.d("PRINTING  F2",getFlowMeter2().getValue());
                setAvailableFuel(context);
                break;
            case "odo":
                setOdometer(data[2],context);
                break;
        }

    }




    public static float getTankCapacity() {


        return tankCapacity;

    }

    public static void setTankCapacity(Context context) {
        //dataBaseHelper2=new DataBaseHelper2(context);
        //DATA.tankCapacity=Float.valueOf(dataBaseHelper2.getDbtank(DataBaseHelper1.Database_Name));
        //Log.d("DATA ", " DATA.tankCapacity : "+ DATA.tankCapacity);
    }


}
