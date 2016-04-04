package com.brainupco.esupport;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jsantos on 29/mar/2016.
 */
public class Utility {

    // Status
    public static String getSavedAssetStatus(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_Asset_Status_key),
                context.getString(R.string.disponible));
    }

    public static void setAssetStatus(Context context, String newValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.pref_Asset_Status_key), newValue);
        editor.commit();
    }

    // IMEI
    public static String getSavedAssetIMEI(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_Asset_IMEI_key),
                "");
    }

    public static void setAssetIMEI(Context context, String newValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.pref_Asset_IMEI_key), newValue);
        editor.commit();
    }

    // Latitude
    public static String getSavedAssetLatitude(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_Asset_Latitude_key),
                "");
    }

    public static void setAssetLatitude(Context context, String newValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.pref_Asset_Latitude_key), newValue);
        editor.commit();
    }

    // Longitude
    public static String getSavedAssetLongitude(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_Asset_Longitude_key),
                "");
    }

    public static void setAssetLongitude(Context context, String newValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.pref_Asset_Longitude_key), newValue);
        editor.commit();
    }

    // Start Time
    public static String getSavedAssetStartTime(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_Asset_Start_Time_key),
                "");
    }

    public static void setAssetStartTime(Context context, String newValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.pref_Asset_Start_Time_key), newValue);
        editor.commit();
    }

    // End Time
    public static String getSavedAssetEndTime(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_Asset_End_Time_key),
                "");
    }

    public static void setAssetEndTime(Context context, String newValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.pref_Asset_End_Time_key), newValue);
        editor.commit();
    }

    public static String getDeviceIMEI(Context context) {
        // (testphone: 868442014378892)
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String devID = telephonyManager.getDeviceId();
        //String andID = System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        //return telephonyManager.getDeviceId();
        return devID;
    }

    public static String getWSStatus(Context context) {

        // Assume Status eq WS Status
        String assetStatus = getSavedAssetStatus(context);
        String wsStatus = assetStatus;

        // Map Statuses
        if (assetStatus.equalsIgnoreCase(context.getString(R.string.disponible))) {
            wsStatus = "Activo Libre";
        } else if (assetStatus.equalsIgnoreCase(context.getString(R.string.ocupado))) {
            wsStatus = "Atendiendo";
        }

        return wsStatus;
    }

    public static Boolean inServicewindow(Context context) {

        Boolean inWindow = false;
        String pattern = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        // Get Service Window
        String startTime = getSavedAssetStartTime(context);
        String endTime = getSavedAssetEndTime(context);

        try {
            // Get Current Time
            Calendar now = Calendar.getInstance();
            int hour = now.get(Calendar.HOUR);
            int minute = now.get(Calendar.MINUTE);
            Date currentTime = sdf.parse(hour + ":" + minute);

            // Get Window Service
            Date start = sdf.parse(startTime);
            Date end = sdf.parse(endTime);

            // Check if in service window
            inWindow = ( start.compareTo(currentTime) <=0 && end.compareTo(currentTime) >= 0 && now.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY );

        } catch (ParseException e) {
            // Exception handling goes here
        }
        return inWindow;
    }

//    public static void sendLocation(Context context){
//        // Call Service Using an Intent
//        Intent intent = new Intent(context, ESupportService.class);
//        context.startService(intent);
//    }


}
