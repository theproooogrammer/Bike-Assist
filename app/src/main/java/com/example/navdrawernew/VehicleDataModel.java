package com.example.navdrawernew;

public class VehicleDataModel {
    private String bluetoothAddress = "";
    private String vehicleModel = "";
    private String vehicleNumber = "";
    private String vehicleTank = "";
    private String dataBaseName = "";

    public VehicleDataModel() {

    }

    public VehicleDataModel(String bluetoothAddress,String vehicleModel, String vehicleNumber,String vehicleTank , String dataBaseName) {

        this.bluetoothAddress = bluetoothAddress;
        this.vehicleModel = vehicleModel;
        this.vehicleNumber = vehicleNumber;
        this.vehicleTank = vehicleTank;
        this.dataBaseName = dataBaseName;


    }

    public VehicleDataModel(String[] vehInfo) {
        this.bluetoothAddress = vehInfo[0];
        this.vehicleModel = vehInfo[1];
        this.vehicleNumber = vehInfo[2];
        this.vehicleTank = vehInfo[3];
        this.dataBaseName = vehInfo[4];
    }

    public String getBluetoothAddress() {
        return bluetoothAddress;
    }
    public void setBluetoothAddress(String bluetoothAddress) { this.bluetoothAddress = bluetoothAddress; }

    public String getVehicleModel() {
        return vehicleModel;
    }
    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }
    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getvehicleTank() {
        return vehicleTank;
    }
    public void setvehicleTank(String vehicleTank) {
        this.vehicleTank = vehicleTank;
    }

    public String getDataBaseName() {
        return dataBaseName;
    }
    public void setDataBaseName(String dataBaseName) {
        this.dataBaseName = dataBaseName;
    }

    public void setDataBaseName() {

        String dbName = vehicleModel;

        //FOR REMOVING THE SPACES AND REPLACING WITH THe '_'
        if (dataBaseName.contains(" "))
            dbName = dbName.replaceAll(" ", "_");

        dbName = dbName.concat("_" + vehicleNumber);

        if (dbName.contains(" "))
            dbName = dbName.replaceAll(" ", "_");

        this.dataBaseName = dbName;

    }


}
