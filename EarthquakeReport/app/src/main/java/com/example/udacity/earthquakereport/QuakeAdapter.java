package com.example.udacity.earthquakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;


public final class QuakeAdapter extends ArrayAdapter<Earthquake>{

    private Context mContext;

    public QuakeAdapter(@NonNull Context context, @NonNull List objects){
        super(context, 0, objects);

        mContext = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

        View view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

        Earthquake data = (Earthquake) getItem(position);
        TextView magnitudeView = (TextView) view.findViewById(R.id.tv_magnitude);
        Double magnitude = data.getMagnitude();
        DecimalFormat formatter = new DecimalFormat("0.0");
        magnitudeView.setText(formatter.format(magnitude));

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(data.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        String offset = data.getLocationOffset();

        TextView locationOffset = (TextView) view.findViewById(R.id.tv_location_offset);
        if(offset == null) {
            locationOffset.setVisibility(View.GONE);
        } else {
            locationOffset.setText(offset);
        }

        TextView primaryLocation = (TextView) view.findViewById(R.id.tv_primary_location);
        primaryLocation.setText(data.getPrimaryLocation());

        TextView date = (TextView) view.findViewById(R.id.tv_date);
        date.setText(data.getDate());

        TextView time = (TextView) view.findViewById(R.id.tv_time);
        time.setText(data.getTime());

        return view;
    }

    private int getMagnitudeColor(double magnitude){

        int colorId =0;
        int mag = (int) Math.floor(magnitude);

        switch(mag){
            case 1:
                colorId = R.color.magnitude1;
                break;
            case 2:
                colorId = R.color.magnitude2;
                break;
            case 3:
                colorId = R.color.magnitude3;
                break;
            case 4:
                colorId = R.color.magnitude4;
                break;
            case 5:
                colorId = R.color.magnitude5;
                break;
            case 6:
                colorId = R.color.magnitude6;
                break;
            case 7:
                colorId = R.color.magnitude7;
                break;
            case 8:
                colorId = R.color.magnitude8;
                break;
            case 9:
                colorId = R.color.magnitude9;
                break;
            default:
                colorId = R.color.magnitude10plus;
                break;
        }

        return ContextCompat.getColor(getContext(), colorId);
    }
}
