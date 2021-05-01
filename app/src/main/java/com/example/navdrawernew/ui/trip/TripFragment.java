package com.example.navdrawernew.ui.trip;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.navdrawernew.DataBaseHelper1;
import com.example.navdrawernew.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TripFragment extends Fragment {

    DataBaseHelper1 dataBaseHelper1;
    EditText name;
    TextView date;
    TextView fuel;
    TextView distance;
    TextView mileage;
    TextView trip_name;
    FloatingActionButton start_trip, stop_trip;
    TripDataModel trip;
    private String active_trip;
    ConstraintLayout activeTrip;
    private String update_odometer, update_flowmeter2;

    private TripViewModel tripViewModel;
    private ConstraintLayout startTrip;

    public DataBaseHelper1 getDataBaseHelper1() {
        if (dataBaseHelper1 == null)
            dataBaseHelper1 = new DataBaseHelper1(getContext());
        return dataBaseHelper1;
    }

    public void initialize() {
        if (getDataBaseHelper1().getDbActiveTrip().equalsIgnoreCase("1")) {
            startTrip.setVisibility(View.INVISIBLE);
            activeTrip.setVisibility(View.VISIBLE);
            trip.setActive("");
            trip.setTripName(getDataBaseHelper1().getDbActiveTripName());
            trip.setSetDate(getDataBaseHelper1().getDbActiveTripDate());
            trip_name.setText(trip.getTripName());
            SimpleDateFormat sf = new SimpleDateFormat("EEEE, MMMM dd, hh:mm a");
            Date d = null;
            try {
                d = sf.parse(trip.getSetDate());
                date.setText(sf.format(d));
            } catch (ParseException e) {
                e.printStackTrace();
            }finally {
                date.setText(trip.getSetDate());
            }
            Float show_odometer = Float.valueOf(dataBaseHelper1.getDbOdoMeter()) - Float.valueOf(getDataBaseHelper1().getInitialOdo_meter2());
            show_odometer /= 1000;
            trip.setDistance(String.valueOf(show_odometer));
            updateFuelFragment();
            updateOdometerFragment();
            Log.d("WHAT IS SHOWING","TRIP IS ACTIVE");
        }else{
            Log.d("WHAT IS SHOWING","TRIP IS NOT ACTIVE");
            startTrip.setVisibility(View.VISIBLE);
            activeTrip.setVisibility(View.INVISIBLE);
        }

    }

    public void updateFuelFragment() {
        Float show_flowmeter2 = Float.valueOf(getDataBaseHelper1().getDbFlowMeter2()) - Float.valueOf(getDataBaseHelper1().getInitialFlow_meter2());
        show_flowmeter2 /= 1000;
        Log.d("UPDATE VALUES: ", getDataBaseHelper1().getDbFlowMeter2() + "-" + getDataBaseHelper1().getInitialFlow_meter2());
        trip.setFuel(String.valueOf(show_flowmeter2));
        trip.setMileage();
        this.fuel.setText(String.valueOf(show_flowmeter2+" Litres"));
        mileage.setText((trip.getMileage()+" KmpL"));

    }

    public void updateOdometerFragment() {
        Float show_odometer = Float.valueOf(dataBaseHelper1.getDbOdoMeter()) - Float.valueOf(getDataBaseHelper1().getInitialOdo_meter2());
        show_odometer /= 1000;
        Log.d("UPDATE VALUES: ", getDataBaseHelper1().getDbOdoMeter() + "-" + getDataBaseHelper1().getInitialOdo_meter2());
        trip.setDistance(String.valueOf(show_odometer));
        this.distance.setText((String.valueOf(show_odometer)+ " Kilometres"));
    }

    public void unHide() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trip = new TripDataModel();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tripViewModel =
                ViewModelProviders.of(this).get(TripViewModel.class);
        View root = inflater.inflate(R.layout.trip_new, container, false);
        tripViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });


        /////////////////

        name = root.findViewById(R.id.tripname);
        date = root.findViewById(R.id.trip_get_date);
        fuel = root.findViewById(R.id.trip_get_fuel);
        distance = root.findViewById(R.id.trip_get_distance);
        mileage = root.findViewById(R.id.trip_get_mileage);
        start_trip = root.findViewById(R.id.btnTrip);
        stop_trip = root.findViewById(R.id.stop_trip);
        activeTrip = root.findViewById(R.id.content_activeTrip);
        startTrip = root.findViewById(R.id.content_startTrip);
        trip_name = root.findViewById(R.id.trip_name);
        ///////////////////


        start_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                active_trip = "1";
                update_flowmeter2 = getDataBaseHelper1().getDbFlowMeter2();
                update_odometer = getDataBaseHelper1().getDbOdoMeter();

                trip.setActive("1");
                trip.setFuel(update_flowmeter2);
                trip.setDistance(update_odometer);
                trip.setTripName(name.getText().toString());
                SimpleDateFormat sf = new SimpleDateFormat("EEEE, MMMM dd, hh:mm a");
                Date d = new Date();
                trip.setSetDate(sf.format(d));
                Log.d("addActiveTrip", "fm2 " + update_flowmeter2 + ", odo " + update_odometer + ", active" + getDataBaseHelper1().getDbActiveTrip());
                try {
                    Log.d("addActiveTrip", "INSERTING Values in DB: ");
                    boolean result = dataBaseHelper1.addActiveTrip(trip);

                    if (result == true) {
                        trip_name.setText(trip.getTripName());
                         sf = new SimpleDateFormat("EEEE, MMMM dd, hh:mm a");
                         d = null;
                        try {
                            d = sf.parse(getDataBaseHelper1().getDbActiveTripDate());
                            date.setText(sf.format(d));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }finally {
                            date.setText(getDataBaseHelper1().getDbActiveTripDate());
                        }
                        updateFuelFragment();
                        updateOdometerFragment();
                        startTrip.setVisibility(View.INVISIBLE);
                        activeTrip.setVisibility(View.VISIBLE);

                    }

                } catch (Exception e) {
                    Log.d("addActiveTrip Exception", "INSERTING Values : " + e);
                }
            }
        });
        tripViewModel.getFlowMeter2().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (getDataBaseHelper1().getDbActiveTrip().equalsIgnoreCase("1"))
                    updateFuelFragment();
            }
        });
        tripViewModel.getOdometer().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (getDataBaseHelper1().getDbActiveTrip().equalsIgnoreCase("1"))
                    updateOdometerFragment();
            }
        });

        stop_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                active_trip = "0";
                try {
                    Log.d("updateData", "INSERTING Values in DB: ");
                    boolean result = dataBaseHelper1.deactivateTrip(trip);
                    startTrip.setVisibility(View.VISIBLE);
                    activeTrip.setVisibility(View.INVISIBLE);

                } catch (Exception e) {
                    Log.d("updateData Exception", "UPDATING Values : " + e);
                }


            }
        });
        initialize();

        return root;
    }



    public void showmsg(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setMessage(title);
        builder.setMessage(msg);
        builder.show();

    }
}