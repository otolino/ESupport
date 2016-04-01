package com.brainupco.esupport;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.format.Time;

/**
 * Created by jsantos on 29/mar/2016.
 */
public class Utility {

    public static String getSavedAssetStatus(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_AssetStatus_key),
                context.getString(R.string.disponible));
    }

    public static void setAssetStatus(Context context, String newValue){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.pref_AssetStatus_key), newValue);
        editor.commit();
    }

    public static String getSavedAssetIMEI(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_AssetIMEI_key),
                "");
    }

    public static void setAssetIMEI(Context context, String newValue){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.pref_AssetIMEI_key), newValue);
        editor.commit();
    }

    public static String getDeviceIMEI(Context context){
        // (testphone: 868442014378892)
        TelephonyManager telephonyManager = (TelephonyManager)  context.getSystemService(Context.TELEPHONY_SERVICE);
        String devID = telephonyManager.getDeviceId();
        //String andID = System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        //return telephonyManager.getDeviceId();
        return devID;
    }

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

}
