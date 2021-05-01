package com.example.navdrawernew.ui.home;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;

import com.example.navdrawernew.DATA;
import com.example.navdrawernew.DataBaseHelper1;
import com.example.navdrawernew.R;
import com.example.navdrawernew.ui.trip.TripDataModel;
import com.john.waveview.WaveView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeFragment extends Fragment {
    private CardView fuel,mileage,activity,trip;
    private HomeViewModel homeViewModel;
    Intent in;

    DataBaseHelper1 dataBaseHelper1;
    WaveView waves;
    TextView label_available_fuel;
    TextView label_available_fuel_percentage;
    TextView label_distance_to_zero, label_mileage;
    TextView setTripName, tripName, tripDate, tripFuel, tripDistance;
    ImageButton goToTrip, tripStart;
    ConstraintLayout activeTrip;
    TripDataModel tripDataModel;

    public DataBaseHelper1 getDataBaseHelper1() {
        if (dataBaseHelper1 == null)
            dataBaseHelper1 = new DataBaseHelper1(getContext());

        return dataBaseHelper1;
    }

    public void initialize() {
        String dBavailableFuel = getDataBaseHelper1().getDbavailableFuel();

        if (dBavailableFuel != null) updateFuelCard();
        if (getDataBaseHelper1().getDbActiveTrip().equalsIgnoreCase("1")) {

            activeTrip.setVisibility(View.VISIBLE);
            tripStart.setVisibility(View.INVISIBLE);
            setTripName.setVisibility(View.INVISIBLE);
            upDateTripCard();
        } else {
            activeTrip.setVisibility(View.INVISIBLE);
            tripStart.setVisibility(View.VISIBLE);
            setTripName.setVisibility(View.VISIBLE);
        }
    }


    public void updateFuelCard() {


        String availableFuel = getDataBaseHelper1().getDbavailableFuel();

        label_available_fuel.setText(String.format("%3.2f", Float.valueOf(availableFuel)));
        final int p = calculateLevel(Float.valueOf(availableFuel), DATA.getTankCapacity());
        label_available_fuel_percentage.setText(p + "%");
        waves.post(new Runnable() {
            @Override
            public void run() {
                waves.setProgress(p);
            }
        });
        int mileage = (int) (Float.valueOf(availableFuel) * Float.valueOf(String.valueOf(label_mileage.getText())));
        int color = (Integer) new ArgbEvaluator().evaluate(p, 0xC71919, 0x239760);  // red to green
    }


    public void upDateTripCard() {
        tripName.setText(getDataBaseHelper1().getDbActiveTripName());
        SimpleDateFormat sf = new SimpleDateFormat("EEEE, MMMM dd, hh:mm a");
        Date d = null;
        try {
            d = sf.parse(getDataBaseHelper1().getDbActiveTripDate());
            tripDate.setText(sf.format(d));
        } catch (ParseException e) {
            e.printStackTrace();
        }finally {
            tripDate.setText(getDataBaseHelper1().getDbActiveTripDate());
        }


        Float tripF;
        tripF = Float.valueOf(getDataBaseHelper1().getInitialFlow_meter2()) - Float.valueOf(getDataBaseHelper1().getDbFlowMeter2());
        tripF /= 1000;
        tripF = Math.abs(tripF);
        tripFuel.setText(String.format("%04.2f", tripF) + " Litres");
        tripF = Float.valueOf(getDataBaseHelper1().getInitialOdo_meter2()) - Float.valueOf(getDataBaseHelper1().getDbOdoMeter());
        tripF /= 1000;
        tripF = Math.abs(tripF);
        tripDistance.setText(String.format("%04.2f", tripF) + " Kms");
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);



        homeViewModel.getText().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        //DISTANCE & MILEAGE CARD ///////////////////////////////////////////////////////////////////////////////////

        label_mileage = root.findViewById(R.id.label_mileage);
        label_distance_to_zero = root.findViewById(R.id.label_distance_to_zero);



        //FUEL CARD ///////////////////////////////////////////////////////////////////////////////////

        fuel = root.findViewById(R.id.card_fuel);
        waves = root.findViewById(R.id.waves);
        label_available_fuel = root.findViewById(R.id.label_available_fuel);
        label_available_fuel_percentage = root.findViewById(R.id.label_available_fuel_percentage);
        fuel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                                                .addSharedElement(fuel, "openFuelFragment")
                                                .build();
                                        Navigation.findNavController(v).navigate(R.id.nav_fuel, null, null, extras);


                                    }
                                }
        );

        //TRIP CARD ///////////////////////////////////////////////////////////////////////////////////
        trip = root.findViewById(R.id.card_trip);

        tripStart = root.findViewById(R.id.tripStart);
        setTripName = root.findViewById(R.id.setTripName);
        goToTrip = root.findViewById(R.id.goToTripButton);

        tripName = root.findViewById(R.id.tripname);
        tripDate = root.findViewById(R.id.tripDate);
        tripFuel = root.findViewById(R.id.tripFuel);
        tripDistance = root.findViewById(R.id.tripDistance);

        activeTrip = root.findViewById(R.id.activeTrip);

        tripStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tripStart.setVisibility(View.INVISIBLE);
                setTripName.setVisibility(View.INVISIBLE);
                activeTrip.setVisibility(View.VISIBLE);
                tripDataModel = new TripDataModel();
                tripDataModel.setTripName(String.valueOf(setTripName.getText()));

                try {
                    Log.d("addActiveTrip", "INSERTING Values in DB: ");
                    boolean result = getDataBaseHelper1().addActiveTrip(tripDataModel);

                    if (result == true) {
                        upDateTripCard();

                    } else {
                    }
                } catch (Exception e) {
                    Log.d("addActiveTrip Exception", "INSERTING Values : " + e);
                }


            }
        });
        goToTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trip.callOnClick();
            }
        });
        trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                        .addSharedElement(trip, "openTripfragment")
                        .build();
                Navigation.findNavController(v).navigate(R.id.nav_trip, null, null, extras);
            }
        });




        //          OBSERVERS    ///////////////////////////////////////////////////////////////////////////////////

        // available Fuel
        homeViewModel.getaAvailableFuel().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s != null) {
                    updateFuelCard();
                }
            }
        });

        //odometer
        homeViewModel.getOdometer().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                s = getDataBaseHelper1().getDbOdoMeter();
                Float tripD = Float.valueOf(getDataBaseHelper1().getInitialOdo_meter2()) - Float.valueOf(s);
                tripD /= 1000;
                tripD = Math.abs(tripD);
                tripDistance.setText(String.format("%04.2f", tripD) + " Kms");

                s = String.format("%06.2f", Float.valueOf(s) / 1000);
                s += " ";
                TextView odo2 = root.findViewById(R.id.odometer2);
                odo2.setText(s);


            }
        });

        // f2
        homeViewModel.getFlowMeter2().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Float x, y, z;
                s = "";
                x = Float.valueOf(getDataBaseHelper1().getDbFlowMeter2());
                Float tripF = Float.valueOf(getDataBaseHelper1().getInitialFlow_meter2()) - x;
                tripF /= 1000;
                tripF = Math.abs(tripF);
                tripFuel.setText(String.format("%04.2f", tripF) + " Litres");
                x = x / 1000;
                s = String.valueOf(x);
                y = Float.valueOf(getDataBaseHelper1().getDbOdoMeter());
                y = y / 1000;
                s += ",";
                s += String.valueOf(y);
                z = (y / x);
                s += ",";
                s += String.valueOf(z);
                label_mileage.setText(String.format("%3.1f", Math.abs(z)));
            }
        });


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();

    }


    public int calculateLevel(float availableFuel, float tankCapacity) {
        return (int)((availableFuel/tankCapacity)*100);
    }
}