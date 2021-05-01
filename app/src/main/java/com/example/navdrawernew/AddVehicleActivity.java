package com.example.navdrawernew;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import me.aflak.bluetooth.Bluetooth;
import me.aflak.bluetooth.interfaces.BluetoothCallback;
import me.aflak.bluetooth.interfaces.DeviceCallback;
import me.aflak.bluetooth.interfaces.DiscoveryCallback;

public class AddVehicleActivity extends AppCompatActivity {
    Button nextButton,prevButton;
    ConstraintLayout page1,page2;
    ImageButton refreshImageButton;
    TextView status ;
    ListView availalbleListView;
    Bluetooth bluetooth;
    private  ListView pairedListView;


    List<BluetoothDevice> paired_Devices = new ArrayList<BluetoothDevice>(); //Store Paired Devices
    List<BluetoothDevice>  pairedDevicesList = new ArrayList<>();            // Extra list required
    ArrayAdapter pairedDevicesAdapter;                                       // Adapter for Paired Devices
    List<BluetoothDevice> available_Devices = new ArrayList<BluetoothDevice>();  //Store available devices
    ArrayAdapter availableDevicesAdapter;                                        //Adapter for available devices

    private BluetoothAdapter mbluetoothAdapter;                               //most reliable default source when library fails

    //=========================================================================================
    //ADD VEHCILE COMPONENTS
    SessionManager sessionManager;
    public DataBaseHelper1 dataBaseHelper1;
    public DataBaseHelper2 dataBaseHelper2;
    public static String vehicleModel = "";
    public static String pairedDeviceAddress = "";
    public VehicleDataModel vehicle;
    public static   Float   tank=0.0f;


    public EditText Vehicle_no;
    public Button addButton;
    public Button submit;
    public Spinner spinner;

    public void addListenerOnSpinnerItemSelection () { spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener()); }

    //=========================================================================================

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

            bluetooth.startScanning();
            pairedDevicesList = bluetooth.getPairedDevices();
            for (BluetoothDevice b: pairedDevicesList){
                if(b.getName().equalsIgnoreCase("hc-05")) {
                    paired_Devices.add(b);
                    pairedDevicesAdapter.notifyDataSetChanged();
                }

            }
            //pairedDevices.setAdapter(pairedDevicesAdapter);
            pairedDevicesAdapter.notifyDataSetChanged();

        }

        @Override
        public void onUserDeniedActivation() {
            // handle activation denial...
        }
    };

    private DiscoveryCallback discoveryCallback  = new DiscoveryCallback() {
        @Override
        public void onDiscoveryStarted() {
            if( available_Devices!=null) {
                available_Devices.clear();
                availableDevicesAdapter.notifyDataSetChanged();
            }
            refresh.setRepeatCount(-1);
            refresh.setDuration(1000);
            refreshImageButton.setAnimation(refresh);
            status.setText("SCANNING");
            //Log.d("Discovery", "Started....");
           // Toast.makeText(AddVehicleActivity.this, "Discovery Started", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onDiscoveryFinished() {
            status.setText("REFRESH");
            refreshImageButton.clearAnimation();
            //Log.d("Discovery", "Finished....");
           // Toast.makeText(AddVehicleActivity.this, "Discovery Finished", Toast.LENGTH_LONG).show();
            //Log.d("DETECTED DEVICES",available_Devices.toString());
            //Log.d("ADAPTER DEVICES",String.valueOf(availableDevicesAdapter.getCount()));
            //Log.d("ADAPTER DEVICES","END");
        }

        @Override
        public void onDeviceFound(BluetoothDevice device) {
            //Log.d("FOUND!!!", "Found a device" + device.getName());
            //Toast.makeText(BluetoothActivity.this, "FOUND DEVICE", Toast.LENGTH_LONG).show();
            //label_pairedDevices.setText(device.getName());
            //Add code to check if device is hc 05

            if (device.getName().equalsIgnoreCase("hc-05")) {
                if(!bluetooth.getPairedDevices().contains(device)) {
                    available_Devices.add(device);
                    availableDevicesAdapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void onDevicePaired(BluetoothDevice device) {
            paired_Devices.add(device);
            available_Devices.remove(device);
            availableDevicesAdapter.notifyDataSetChanged();
            pairedDevicesAdapter.notifyDataSetChanged();
            //bluetooth.connectToDevice(device);
            pairedDeviceAddress = device.getAddress();
            addButton.setEnabled(true);

        }

        @Override
        public void onDeviceUnpaired(BluetoothDevice device) {
            paired_Devices.remove(device);
            pairedDevicesAdapter.notifyDataSetChanged();
            bluetooth.startScanning();


        }

        @Override
        public void onError(int errorCode) {
            Log.d("ERROR","ERROR while unpairing");
           // Toast.makeText(AddVehicleActivity.this, "SOme ERror ", Toast.LENGTH_LONG).show();

        }
    };

    private DeviceCallback deviceCallback = new DeviceCallback() {
        @Override
        public void onDeviceConnected(BluetoothDevice device) {


        }

        @Override
        public void onDeviceDisconnected(BluetoothDevice device, String message) {

        }

        @Override
        public void onMessage(byte[] message) {
           /* Log.d("RECEIVED","messages");
            String s = new String(message);
            if(s!=null){ DATA.setDATA(s,getApplicationContext());}
            Log.d("RECEIVED","message: "+s);
            Log.i("RECEIVED","message: "+s);
            */
        }

        @Override
        public void onError(int errorCode) {

        }

        @Override
        public void onConnectError(BluetoothDevice device, String message) {

        }
    };

    //========================================================================================  q1=
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        dataBaseHelper1=new DataBaseHelper1(this);
        dataBaseHelper2=new DataBaseHelper2(this);
        sessionManager=new SessionManager(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 200);
        }

        final ImageView check= findViewById(R.id.imageView3);
        TextView bmol = findViewById(R.id.textView9);
        bmol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check.getVisibility()==View.VISIBLE){
                check.setVisibility(View.INVISIBLE);
                addButton.setEnabled(false);

                }
                else {
                    check.setVisibility(View.VISIBLE);
                    addButton.setEnabled(true);

                }
            }
        });

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        nextButton = findViewById(R.id.nextButton);
        prevButton = findViewById(R.id.backButton);
        page1 = findViewById(R.id.page_1);
        page2 = findViewById(R.id.page_2);
        refreshImageButton = findViewById(R.id.refreshButton);
        status = findViewById(R.id.status_add_vehicle);
        availalbleListView = findViewById(R.id.list_available_devices);
        pairedListView =findViewById(R.id.myListView);
        addButton= findViewById(R.id.addButton);
        submit= findViewById(R.id.submit);
        mbluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //=========================================================================================

        Vehicle_no= findViewById(R.id.Vehicle_no);
        spinner = findViewById(R.id.spinner);

        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Activa 5G");
        arrayList.add("Access 125");
        arrayList.add("Aviator");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        addListenerOnSpinnerItemSelection();

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Scan","Starting scan");
                mbluetoothAdapter.startDiscovery();
                Log.d("Scan","Scanning started");

            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            boolean result;
            @Override
            public void onClick(View v) {
                try{

                    Log.d("AddVehicleActivity", " Values  of BT_Num: " + pairedDeviceAddress);
                    Log.d("AddVehicleActivity", " Values  of vehicleModel: " + vehicleModel);
                    Log.d("AddVehicleActivity", " Values  of Vehicle_no: "+Vehicle_no.getText().toString());
                    Log.d("AddVehicleActivity", " Values  of tank: "+tank);



                    Log.d("AddVehicleActivity", " DataBaseHelper.Database_Name "+ DataBaseHelper1.Database_Name);

                    vehicle = new VehicleDataModel();
                    vehicle.setBluetoothAddress("00:19:09:10:03:8C");
                    vehicle.setVehicleModel(vehicleModel);
                    vehicle.setVehicleNumber(Vehicle_no.getText().toString());
                    vehicle.setvehicleTank(String.valueOf(tank));
                    vehicle.setDataBaseName();

                    Log.d("AddVehicleActivity", "INSERTING Values in DB: ");
                    result = dataBaseHelper2.addVehicle(vehicle);
                    if (result==true){


                        sessionManager.setDatabase(vehicle.getDataBaseName());
                        sessionManager.addItem(AddVehicleActivity.this, vehicle);

                        // dataBaseHelper1.setDatabase_Name(DataBaseHelper1.Database_Name);
                        //if (dataBaseHelper1 == null)
                        //  dataBaseHelper1 = new DataBaseHelper1(AddVehicleActivity.this);
                        Log.d("AddVehicleActivity", "INSERTING Values in DB: ");

                    }

                    else
                        Log.d("AddVehicleActivity", " Not INSERTING Values in DB: ");


                }catch (Exception e){ Log.d("AddVehicle Exception", "INSERTING Values : "+e); }


                try{

                    Log.d("AddVehicleActivity", "GETTING Values in DB: ");
                    ArrayList<VehicleDataModel> vehicleList;

                    vehicleList = dataBaseHelper2.getVehicleList();
                    StringBuffer buffer=new StringBuffer();
                    for (VehicleDataModel vm : vehicleList) {

                        buffer.append("BT_no " + vm.getBluetoothAddress() + "\n");
                        buffer.append("vehicle " + vm.getVehicleModel() + "\n");
                        buffer.append("vehicle no." + vm.getVehicleNumber() + "\n");
                        buffer.append("tank  " + vm.getvehicleTank() + "\n");
                        buffer.append("DATABASE  " + vm.getDataBaseName() + "\n\n");
                    }


                    showmsg("AddVehicleActivity Data",buffer.toString());

                }catch (Exception e){ Log.d("AddVehicle Exception", "GETTING Values : "+e); }

                if (result==true){
                    //Navigation.findNavController(v).navigate(R.id.nav_home);

                   // Toast.makeText(AddVehicleActivity.this,"VEHICLE ADDED",Toast.LENGTH_LONG).show();

                }
                //else
                   // Toast.makeText(AddVehicleActivity.this,"VEHICLE NOT ADDED",Toast.LENGTH_LONG).show();



            }
        });


        //=========================================================================================

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page1.setVisibility(View.GONE);
                page2.setVisibility(View.VISIBLE);
                if(bluetooth.isEnabled()!=true){
                    bluetooth.showEnableDialog(AddVehicleActivity.this);
                }else {
                    bluetooth.stopScanning();
                    bluetooth.startScanning();

                }

            }
        });


        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page2.setVisibility(View.GONE);
                page1.setVisibility(View.VISIBLE);
                if(mbluetoothAdapter.isDiscovering()){
                    mbluetoothAdapter.cancelDiscovery();
                }

            }
        });
        ////////////////////

        bluetooth = new Bluetooth(this);            //initialize
        bluetooth.setBluetoothCallback(bluetoothCallback); //assigning receiver for on/off
        bluetooth.setDiscoveryCallback(discoveryCallback); // assigning receiver for discovery and pairing
        bluetooth.setDeviceCallback(deviceCallback);
        bluetooth.onStart();
        pairedDevicesAdapter = new ArrayAdapter<BluetoothDevice>(this,  android.R.layout.simple_list_item_single_choice, paired_Devices) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                // Replace text with my own
                view.setText(getItem(position).getAddress());
                return view;
            }
        };

        availableDevicesAdapter = new ArrayAdapter<BluetoothDevice>(this,  android.R.layout.simple_list_item_single_choice, available_Devices) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                // Replace text with my own
                view.setText(getItem(position).getName());
                return view;
                //
            }
        };

        availalbleListView.setAdapter(availableDevicesAdapter);
        pairedListView.setAdapter(pairedDevicesAdapter);

        if(bluetooth.isEnabled()==true){
            bluetooth.disable();
        }

        refreshImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mbluetoothAdapter.isDiscovering()){
                    mbluetoothAdapter.cancelDiscovery();
                }else{
                    bluetooth.startScanning();
                }

            }
        });
        availalbleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                BluetoothDevice bt = (BluetoothDevice) parent.getItemAtPosition(position);
                bluetooth.pair(bt);

                //Toast.makeText(AddVehicleActivity.this, "Pairing with "+bt.getName(), Toast.LENGTH_LONG).show();
                //Log.d("CHECK",bt.getName()+"===="+name);
            }
        });

        pairedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice bt = (BluetoothDevice) parent.getItemAtPosition(position);
                bluetooth.unpair(bt);
                pairedDevicesList.remove(bt);
                pairedDevicesAdapter.notifyDataSetChanged();

            }
        });


        //////////////////////////////////////////  RUNTIME PERMISSION CHECK  ////////////////////////////////////////
        //this part is required as devices aren't discovered until runtime premission is allowed once for every app
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("PERMISSION","NO ACCESS");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 200);
        }else{
            Log.d("PERMISSION","HAS ACCESS");
        }

    }

    public void showmsg(String title,String msg){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setMessage(title);
        builder.setMessage(msg);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Intent in = new Intent(AddVehicleActivity.this, MainActivity.class);
                startActivity(in);
            }
        });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bluetooth.onStop();
    }


    Animation refresh = new RotateAnimation(0.0f,720.0f,
            Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
}
