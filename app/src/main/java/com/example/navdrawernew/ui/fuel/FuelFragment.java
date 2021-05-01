package com.example.navdrawernew.ui.fuel;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.navdrawernew.DATA;
import com.example.navdrawernew.DataBaseHelper1;
import com.example.navdrawernew.MainActivity;
import com.example.navdrawernew.R;
import com.john.waveview.WaveView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FuelFragment extends Fragment {


    public int tankCapacity = 7;
    private float availableFuel = 0.0f;

    private FuelViewModel fuelViewModel;

    DataBaseHelper1 dataBaseHelper1;
    WaveView waves;
    TextView label_available_fuel;
    TextView label_available_fuel_percentage;

    EditText getValue1,getValue2;
    Button set,setAlertButton;
    Spinner sp;
    TextView setAlertMessage;
    EditText setAlertText;
    TextView label_tank_capacity;
    ImageButton openAlert,closeAlert;

    TextView f1Volume, f1Date, f1time, f1Rupees;

    int p;

    public DataBaseHelper1 getDataBaseHelper1() {
        if (dataBaseHelper1 == null)
            dataBaseHelper1 = new DataBaseHelper1(getContext());
        return dataBaseHelper1;
    }

    public void initialize() {

        String dBavailableFuel = getDataBaseHelper1().getDbavailableFuel();
        setAlertText.setText(getDataBaseHelper1().getAlert()[1]);
        if (getDataBaseHelper1().getAlert()[0].equalsIgnoreCase("%"))
            sp.setSelection(0);
        else {
            sp.setSelection(1);
        }
        if (dBavailableFuel != null) availableFuelChanged();
        updatef1History();
        // label_tank_capacity.setText(getDataBaseHelper1().getTankCapacity());

    }

    public void availableFuelChanged() {

        String availableFuel = getDataBaseHelper1().getDbavailableFuel();
        if (Float.valueOf(availableFuel) <= 4.08) {


        }

        label_available_fuel.setText(String.format("%3.2f", Float.valueOf(availableFuel)));
        //int p = calculateLevel(Float.valueOf(s), DATA.getTankCapacity());
        DATA.setTankCapacity(getContext());
        Log.d("FuelFragment ", " FuelFragment.tankCapacity : "+ DATA.getTankCapacity());
        final int p = calculateLevel(Float.valueOf(availableFuel), DATA.getTankCapacity());
        label_available_fuel_percentage.setText(p + "%");
        waves.post(new Runnable() {
            @Override
            public void run() {
                waves.setProgress(p);

            }
        });

    }

    private void updatef1History() {

        String[] f1latest = getDataBaseHelper1().getF1History();

        f1Volume.setText(String.format("%3.2f", Float.valueOf(f1latest[0])));
        String date = "", time = "";
        String rupees = String.valueOf((int)(Float.valueOf(f1latest[0])*75));
        try {
            Date d = new Date(f1latest[1]);
            DateFormat df = new SimpleDateFormat("EEEE, MMMM dd");
            date = df.format(d);
            df = new SimpleDateFormat("hh:mm a");
            time = df.format(d);
        } catch (Exception e) {

        }
        f1Rupees.setText(rupees);
        f1Date.setText(date);
        f1time.setText(time);

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Transition transition = TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move);
        setSharedElementEnterTransition(transition);
        setSharedElementReturnTransition(null);
        try{
            Log.d("HomeFragment", "INSERTING Values in DB: ");
            Log.d("HomeFragment Exception", "INSERTING Values : "+DataBaseHelper1.Database_Name);


        }catch (Exception e){
            Log.d("FuelFragment Exception", "INSERTING Values : " + e);
        }
 }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fuelViewModel =
                ViewModelProviders.of(this).get(FuelViewModel.class);
        View root = inflater.inflate(R.layout.fragment_fuel, container, false);
        fuelViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        final TextView label_tank_capacity = root.findViewById(R.id.label_tank_capacity);
        label_tank_capacity.setText("TANK CAPACITY : 7 Litres");
        waves = root.findViewById(R.id.waves);
        label_available_fuel = root.findViewById(R.id.label_available_fuel);
        label_available_fuel_percentage = root.findViewById(R.id.label_available_fuel_percentage);
        set = root.findViewById(R.id.set_button);


        f1Volume = root.findViewById(R.id.f1_volume);
        f1Date = root.findViewById(R.id.f1_date);
        f1time = root.findViewById(R.id.f1_time);
        f1Rupees = root.findViewById(R.id.f1_rupees);
        sp = root.findViewById(R.id.set_alert_spinner);
        setAlertMessage = root.findViewById(R.id.set_alert_display_message);
        setAlertText = root.findViewById(R.id.set_alert_text);
        setAlertButton = root.findViewById(R.id.set_alert_button);

        openAlert = root.findViewById(R.id.open_alert);
        closeAlert = root.findViewById(R.id.close_alert);


        closeAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlert.setVisibility(View.VISIBLE);
                closeAlert.setVisibility(View.INVISIBLE);
                setAlertMessage.setVisibility(View.GONE);
                setAlertText.setVisibility(View.GONE);
                sp.setVisibility(View.GONE);
                setAlertButton.setVisibility(View.GONE);
            }
        });

        openAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlert.setVisibility(View.INVISIBLE);
                closeAlert.setVisibility(View.VISIBLE);
                setAlertMessage.setVisibility(View.VISIBLE);
                setAlertText.setVisibility(View.VISIBLE);
                sp.setVisibility(View.VISIBLE);
                setAlertButton.setVisibility(View.VISIBLE);
            }
        });


        setAlertButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String type = String.valueOf(sp.getSelectedItem());
                String value = String.valueOf(setAlertText.getText());
                getDataBaseHelper1().setAlert(type, value);
                Toast.makeText(getActivity(), "Alert Set", Toast.LENGTH_LONG).show();
                MainActivity a = (MainActivity) getActivity();
                a.notifyLowFuelAlert();

            }
        });

        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("%");
        arrayList.add("Litres");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(arrayAdapter);

        fuelViewModel.getaAvailableFuel().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    availableFuelChanged();
                }
            }
        });

        fuelViewModel.getF1().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {


                updatef1History();
            }
        });

        initialize();

        return root;
    }


    @Override
    public void onResume() {

        super.onResume();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public void showmsg(String title,String msg){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setMessage(title);
        builder.setMessage(msg);
        builder.show();

    }

    public int calculateLevel(float availableFuel, float tankCapacity) {
        return (int)((availableFuel/tankCapacity)*100);
    }

}