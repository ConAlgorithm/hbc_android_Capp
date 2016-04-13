package com.hugboga.custom.utils;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

/**
 * Created by dyt on 16/4/12.
 */
public class LocationUtils {

    public static void cleanLocationInfo(Context context){
        new SharedPre(context).saveStringValue("lat","");
        new SharedPre(context).saveStringValue("lng","");
    }

    public static void saveLocationInfo(Context context,String lat,String lng){
        new SharedPre(context).saveStringValue("lat",lat);
        new SharedPre(context).saveStringValue("lng",lng);
    }

    public static void saveLocationCity(Context context,String cityId,String cityName,String countryId,String countryName){
        new SharedPre(context).saveStringValue("cityId",cityId);
        new SharedPre(context).saveStringValue("cityName",cityName);
        new SharedPre(context).saveStringValue("countryId",countryId);
        new SharedPre(context).saveStringValue("countryName",countryName);
    }

    public static void gpsIsOpen(Context context){
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    public static void openGPSSeting(Context context){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        try {
            context.startActivity(intent);
        }catch (Exception e){
            intent.setAction(Settings.ACTION_SETTINGS);
            try {
                context.startActivity(intent);
            } catch (Exception e1) {
            }
        }
    }


}
