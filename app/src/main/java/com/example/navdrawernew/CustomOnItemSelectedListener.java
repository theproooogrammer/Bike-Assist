package com.example.navdrawernew;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;


public class CustomOnItemSelectedListener implements OnItemSelectedListener {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       // Toast.makeText(parent.getContext(),
       //         "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
         //     Toast.LENGTH_SHORT).show();

        AddVehicleActivity.vehicleModel = parent.getItemAtPosition(position).toString();
        Log.d("ItemSelectedListener", " DataBaseHelper.Database_Name "+ DataBaseHelper1.Database_Name);

        if (AddVehicleActivity.vehicleModel.equals("Activa 5G"))
            AddVehicleActivity.tank=5.0f;


        else if (AddVehicleActivity.vehicleModel.equals("Access 125"))
            AddVehicleActivity.tank=5.6f;


        else if (AddVehicleActivity.vehicleModel.equals("Aviator"))
            AddVehicleActivity.tank=5.3f;

        else {
            AddVehicleActivity.tank = 6f;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
