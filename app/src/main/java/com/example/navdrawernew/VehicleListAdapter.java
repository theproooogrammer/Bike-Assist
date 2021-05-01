package com.example.navdrawernew;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class VehicleListAdapter extends ArrayAdapter<VehicleDataModel> {
    ArrayList<VehicleDataModel> objects = new ArrayList<>();

    public VehicleListAdapter(Context context, int textViewResourceId, ArrayList<VehicleDataModel> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.vehicle_list, null);
        TextView vehicleModel, vehicleNumber;
        ImageView selected;
        vehicleModel = rowView.findViewById(R.id.vehicleModel);
        vehicleNumber = rowView.findViewById(R.id.vehicleNumber);
        vehicleModel.setText(objects.get(position).getVehicleModel());
        vehicleNumber.setText(objects.get(position).getVehicleNumber());
        selected = rowView.findViewById(R.id.selected);
        selected.setVisibility(View.INVISIBLE);
        if (DataBaseHelper1.getDatabase_Name().equalsIgnoreCase(objects.get(position).getDataBaseName())) {
            selected.setVisibility(View.VISIBLE);
        }
        return rowView;
    }
}
