package com.example.navdrawernew.ui.trip;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.navdrawernew.R;

import java.util.ArrayList;

public class GetTripListAdapter extends ArrayAdapter<TripDataModel> {
    ArrayList<TripDataModel> objects = new ArrayList<>();

    public GetTripListAdapter(Context context, int textViewResourceId, ArrayList<TripDataModel> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;

    }
    @Nullable
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.d("HEY","getView Method called");
        View rowView = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.get_trip, null);

        EditText name = rowView.findViewById(R.id.trip_name);
        TextView date = rowView.findViewById(R.id.trip_get_date);
        TextView fuela = rowView.findViewById(R.id.trip_get_fuel);
        TextView distancea= rowView.findViewById(R.id.trip_get_distance);
        TextView mileagea = rowView.findViewById(R.id.trip_get_mileage);
        name.setText(objects.get(position).getTripName() );
        date.setText(objects.get(position).getSetDate());
        fuela.setText(objects.get(position).getFuel());
        distancea.setText(objects.get(position).getDistance());
        mileagea.setText(objects.get(position).getMileage());



        return rowView;
    }
}
