package com.example.udacity.earthquakereport;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class Earthquake{

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
    private static String LOCATION_SEPRATOR = " of";

    double mMagnitude;
    String mLocationOffset;
    String mPrimaryLocation;
    String mDate;
    String mTime;
    String mUrl;

    public Earthquake(Double magnitude, String place, Long time, String url){
        mMagnitude = magnitude;
        splitPlace(place);
        convertToDate(time);
        mUrl = url;
    }

    private void splitPlace(String place){
        if(place.contains(LOCATION_SEPRATOR)){
            String temp[] = place.split(LOCATION_SEPRATOR);
            mLocationOffset = temp[0] + LOCATION_SEPRATOR;
            mPrimaryLocation = temp[1].trim();
        }else{
            mLocationOffset = null;
            mPrimaryLocation = place;
        }
    }

    private void convertToDate(Long time){

        Date dateObject = new Date(time);
        mDate = dateFormat.format(dateObject);
        mTime = timeFormat.format(dateObject);
    }


    public double getMagnitude(){
        return mMagnitude;
    }

    public String getLocationOffset(){
        return mLocationOffset;
    }

    public String getPrimaryLocation(){
        return mPrimaryLocation;
    }

    public String getDate(){
        return mDate;
    }

    public String getTime(){
        return mTime;
    }

    public String getUrl(){
        return mUrl;
    }
}
