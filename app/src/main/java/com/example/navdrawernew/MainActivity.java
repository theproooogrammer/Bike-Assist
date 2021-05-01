package com.example.navdrawernew;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import me.aflak.bluetooth.Bluetooth;
import me.aflak.bluetooth.interfaces.BluetoothCallback;
import me.aflak.bluetooth.interfaces.DeviceCallback;

public class MainActivity extends AppCompatActivity {
    DataBaseHelper1 dataBaseHelper1;

    private AppBarConfiguration mAppBarConfiguration;
    private Menu mMenu ;
    public ImageView switchUserImageButton;
    public TextView checkUser, vehicleNumber;
    public TextView vehicle_name;
    public TextView bluetoothStatusText;
    private Dialog mDialog;
    public DataBaseHelper2 dataBaseHelper2;
    SessionManager sessionManager;
    Bluetooth bluetooth;
    private VehicleDataModel vehicle;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void sendNotification(Context context, String title, String text) {
        Notification fuelAlert;
        NotificationManager notificationManager;


        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_low_fuel_alert)
                .setContentTitle(title)
                .setContentText(text);


        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.createNotificationChannel(new NotificationChannel("neeew", "Fuel Alert", NotificationManager.IMPORTANCE_HIGH));
        builder.setChannelId("neeew");
        fuelAlert = builder.build();

        notificationManager.notify(440, fuelAlert);

    }

    public DataBaseHelper1 getDataBaseHelper1() {
        if (dataBaseHelper1 == null)
            dataBaseHelper1 = new DataBaseHelper1(this);
        return dataBaseHelper1;
    }

    //==================================================================================================================
    private DeviceCallback deviceCallback = new DeviceCallback() {
        @Override
        public void onDeviceConnected(BluetoothDevice device) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setGreenStatus("connected");
                }
            });

        }

        @Override
        public void onDeviceDisconnected(BluetoothDevice device, String message) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setGreyStatus("disconnected");
                }
            });
        }

        @Override
        public void onMessage(byte[] message) {
            Log.d("RECEIVED", "messages");
            String s = new String(message);
            if (s != null) {
                DATA.setDATA(s, getApplicationContext());
            }
            Log.d("RECEIVED", "message: " + s);
            //Log.i("RECEIVED", "message: " + s);

        }

        @Override
        public void onError(int errorCode) {

        }

        @Override
        public void onConnectError(BluetoothDevice device, String message) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    setGreyStatus("could not connect");
                }
            });
        }
    };
    private BluetoothCallback bluetoothCallback = new BluetoothCallback() {
        @Override
        public void onBluetoothTurningOn() {
        }

        @Override
        public void onBluetoothTurningOff() {
        }

        @Override
        public void onBluetoothOff() {

        }

        @Override
        public void onBluetoothOn() {
            setGreyStatus("connecting...");
            try {
                bluetooth.connectToAddress(vehicle.getBluetoothAddress());
            } catch (Exception e) {
                setGreyStatus("Error connecting unknown");
            }


        }

        @Override
        public void onUserDeniedActivation() {
            // handle activation denial...
        }
    };

    public void initialize() {
        try {
            Log.d("MainActivity", "Initializing................ ");

            if (sessionManager.getDatabase() != null) {
                Log.d("MainActivity", "Existing Vehicle................ ");
                Log.d("MainActivity", "DATABASE NAME sessionManager.getDatabase(): " + sessionManager.getDatabase());
                DataBaseHelper1.setDatabase_Name(sessionManager.getDatabase());
                dataBaseHelper1 = new DataBaseHelper1(this);

                //getDataBaseHelper1().resetDbOdoMeter("0");
                //getDataBaseHelper1().resetDbFlowMeter2(0f);
                //getDataBaseHelper1().resetDbFlowMeter1(0f);
                //getDataBaseHelper1().setDbavailableFuel(0f);
                //getDataBaseHelper1().setDbFlowMeter1(5000f);
                updateActivity();


            } else {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 200);
                }
                Log.d("MainActivity", "New Vehicle................ ");
                Intent in = new Intent(MainActivity.this, AddVehicleActivity.class);

                startActivity(in);
                this.onStop();

            }


        } catch (Exception e) {
            Log.d("MainActivity", "MainActivity Exception" + e);
        }

    }

    public void updateActivity() {


        String[] vehInfo = SessionManager.getFavoriteList(this);
        vehicle = new VehicleDataModel(vehInfo);
        Log.d("MainActivity", "MainActivity " + vehInfo[0]);
        Log.d("MainActivity", "MainActivity " + vehInfo[1]);
        Log.d("MainActivity", "MainActivity " + vehInfo[2]);
        Log.d("MainActivity", "MainActivity " + vehInfo[3]);
        Log.d("MainActivity", "MainActivity " + vehInfo[4]);
        vehicle_name = findViewById(R.id.vehicle_name);//TAKE IS VALUES FOR SHARED PREFRENCE
        vehicleNumber.setText(vehicle.getVehicleNumber());
        vehicle_name.setText(vehicle.getVehicleModel() + " " + vehicle.getVehicleNumber());
        checkUser.setText(vehicle.getVehicleModel());

        DATA.USER = String.valueOf(vehInfo[1]);

        String dBdata = "";
        dBdata = getDataBaseHelper1().getDbavailableFuel();
        dBdata += ",";
        dBdata += getDataBaseHelper1().getDbOdoMeter();
        dBdata += ",";
        dBdata += getDataBaseHelper1().getDbFlowMeter1();
        dBdata += ",";
        dBdata += getDataBaseHelper1().getDbFlowMeter2();
        Log.d("MainActivity", "dBdata " + dBdata);

        notifyLowFuelAlert();

    }


    public int calculateLevel(float availableFuel, float tankCapacity) {
        return (int) ((availableFuel / tankCapacity) * 100);
    }

    public void notifyLowFuelAlert() {
        String availableFuel = getDataBaseHelper1().getDbavailableFuel();
        final int p = calculateLevel(Float.valueOf(availableFuel), DATA.getTankCapacity());
        String title1 = "", message1 = "";
        String[] alert = getDataBaseHelper1().getAlert();
        if (!alert[1].equalsIgnoreCase("")) {
            if (alert[0].equalsIgnoreCase("%")) {
                if (p < Integer.valueOf(alert[1])) {

                    title1 = "Fuel alert for your " + vehicle.getVehicleModel() + " " + vehicle.getVehicleNumber();
                    message1 = "Fuel is less than " + alert[1] + "%";
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        MainActivity.sendNotification(getApplicationContext(), title1, message1);
                    }
                }


            } else {
                if (Float.valueOf(availableFuel) < Float.valueOf(alert[1])) {

                    message1 = "Fuel is less than " + alert[1] + " Litres";
                    title1 = "Fuel alert for your" + vehicle.getVehicleModel() + " " + vehicle.getVehicleNumber();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        MainActivity.sendNotification(getApplicationContext(), title1, message1);
                    }
                }
            }


        }
    }

    public void setGreenStatus(String message) {
        bluetoothStatusText.setText(message);
        bluetoothStatusText.setBackgroundColor(0xff3DAD48);
        Snackbar.make(MainActivity.this.findViewById(R.id.fab), message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void setGreyStatus(String message) {
        bluetoothStatusText.setText(message);
        bluetoothStatusText.setBackgroundColor(0xffcdcdcd);
        Snackbar.make(MainActivity.this.findViewById(R.id.fab), message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void setRedStatus(String message) {
        bluetoothStatusText.setText(message);
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetooth = new Bluetooth(this);            //initialize
        bluetooth.setBluetoothCallback(bluetoothCallback); //assigning receiver for on/off
        //bluetooth.setDiscoveryCallback(discoveryCallback); // assigning receiver for discovery and pairing
        bluetooth.setDeviceCallback(deviceCallback);
        bluetooth.onStart();

        sessionManager = new SessionManager(MainActivity.this);

        VehicleDataModel vehicleDataModel=new VehicleDataModel();

        //////////////////////////////////////////  RUNTIME PERMISSION CHECK  ////////////////////////////////////////
        //this part is required as devices aren't discovered until runtime premission is allowed once for every app
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 200);
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (bluetooth.isEnabled()) {
                    if (bluetooth.isConnected()) {
                        setGreyStatus("disconnecting...");
                        bluetooth.disconnect();
                    } else {
                        setGreyStatus("connecting...");

                        try {
                            bluetooth.connectToAddress(vehicle.getBluetoothAddress());
                        } catch (Exception e) {
                            setGreyStatus("error connecting uknown");

                        }
                    }
                } else {
                    setGreyStatus("bluetooth off");
                    bluetooth.showEnableDialog(MainActivity.this);
                }
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //         .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_fuel, R.id.nav_slideshow,
                R.id.nav_trip, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        hideActionBar();

        bluetoothStatusText = findViewById(R.id.bluetoothStatus);
        if (!bluetooth.isEnabled()) {
            setGreyStatus("bluetooth off");
        } else {
            if (bluetooth.isConnected()) {
                bluetooth.disconnect();
            }
            setGreyStatus("bluetooth on");

        }

        View header = navigationView.getHeaderView(0);
        switchUserImageButton = header.findViewById(R.id.switchUser);

        checkUser = header.findViewById(R.id.vehicleModel);
        vehicleNumber = header.findViewById(R.id.vehicleNumber);

        switchUserImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showVehicleListDialog(MainActivity.this);

            }
        });

        initialize();
        //sendNotification(getApplicationContext());


        DATA.getAvailableFuel().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                notifyLowFuelAlert();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.mMenu = menu;
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public Menu getmMenu() {
        return mMenu;
    }


    public void showVehicleListDialog(Activity activity) {
        mDialog = new Dialog(activity);
        mDialog.setContentView(R.layout.switch_user_popup);
        ListView vehicleListView = mDialog.findViewById(R.id.vehicle_listview);
        ImageView addVehicleImageView = mDialog.findViewById(R.id.addVehicleImageView);
        addVehicleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                Intent in = new Intent(MainActivity.this, AddVehicleActivity.class);
                startActivity(in);
            }
        });


        sessionManager=new SessionManager(MainActivity.this);
        dataBaseHelper2=new DataBaseHelper2(this);

        ArrayList<VehicleDataModel> list = dataBaseHelper2.getVehicleList();
        VehicleListAdapter adapter = new VehicleListAdapter(MainActivity.this, R.layout.vehicle_list, list);
        vehicleListView.setAdapter(adapter);

        vehicleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                VehicleDataModel vm = (VehicleDataModel) parent.getItemAtPosition(position);

                Log.d("MainActivity", "DATABASE NAME vm.getDataBaseName(): "+vm.getDataBaseName());
                Log.d("MainActivity", "DATABASE NAME vm.getDataBaseName(): "+vm.getBluetoothAddress());
                Log.d("MainActivity", "DATABASE NAME vm.getDataBaseName(): "+vm.getVehicleModel());
                Log.d("MainActivity", "DATABASE NAME vm.getDataBaseName(): "+vm.getVehicleNumber());
                Log.d("MainActivity", "DATABASE NAME vm.getDataBaseName(): "+vm.getvehicleTank());

                sessionManager.setDatabase(vm.getDataBaseName());
                DataBaseHelper1.setDatabase_Name(vm.getDataBaseName());
                sessionManager.addItem(MainActivity.this, vm);
                mDialog.dismiss();

                ActivityCompat.recreate(MainActivity.this);
            }
        });
        mDialog.show();

    }

    public void hideActionBar(){
        if(this.getActionBar() != null){
            this.getActionBar().hide();
        }
        if(this.getSupportActionBar()!= null) {
            this.getSupportActionBar().hide();
        }
    }
    public void unhideActionBar(){
        if(this.getActionBar() != null){
            this.getActionBar().show();
        }

        if(this.getSupportActionBar()!= null) {
            this.getSupportActionBar().show();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        bluetooth.onStop();
    }
}
