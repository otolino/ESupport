package com.brainupco.esupport.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.brainupco.esupport.Utility;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 */
public class ESupportService extends IntentService implements LocationListener {

    public final String LOG_TAG = ESupportService.class.getSimpleName();

    public ESupportService() {
        super("ESupportService");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        try{
            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String best = lm.getBestProvider(criteria, true);
            lm.requestLocationUpdates(best, 5 * 60 * 1000, 100, this);
        }
        catch (SecurityException e){

        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onLocationChanged(Location currentLocation) {
        if (currentLocation != null){
            // Get Current Location Values
            String assetLatitude = Double.toString(currentLocation.getLatitude());
            String assetLongitude = Double.toString(currentLocation.getLongitude());

            // Save Current Location Values
            Utility.setAssetLatitude(this,assetLatitude);
            Utility.setAssetLongitude(this, assetLongitude);
        }

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            // Execut only if its in Service Window
            Log.v(LOG_TAG, "Sending Location");

            // Get Saved Values
            String assetIMEI = Utility.getSavedAssetIMEI(this);
            String assetStatus = Utility.getWSStatus(this);
            String assetLatitude = Utility.getSavedAssetLatitude(this);
            String assetLongitude = Utility.getSavedAssetLongitude(this);
//            String assetLatitude = "19.4984899";
//            String assetLongitude = "-99.1692977";

            // If no Location info, cancel request
            if (assetLatitude == "" || assetLatitude == "0")
            {
                return;
            }

            // Use URl Connection to Call Web Service
            HttpURLConnection urlConnection = null;

            try {

//                // Get Location
//                LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
//                Criteria criteria = new Criteria();
//                String best = lm.getBestProvider(criteria, true);
//                Location currentLocation = lm.getLastKnownLocation(best);
//
//                // Set values if location found
//                // If not found it will send the las saved location
//                if (currentLocation != null){
//                    // Get Current Location Values
//                    assetLatitude = Double.toString(currentLocation.getLatitude());
//                    assetLongitude = Double.toString(currentLocation.getLongitude());
//
//                    // Save Current Location Values
//                    Utility.setAssetLatitude(this,assetLatitude);
//                    Utility.setAssetLongitude(this, assetLongitude);
//                }

                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                final String Mobile_Location_BASE_URL = "http://web-mobilelocation.azurewebsites.net/Home/UMPE?";
                final String IMEI_PARAM = "IMEI";
                final String STATUS_PARAM = "S";
                final String LAT_PARAM = "Lat";
                final String LON_PARAM = "Lon";

                Uri builtUri = Uri.parse(Mobile_Location_BASE_URL).buildUpon()
                        .appendQueryParameter(IMEI_PARAM, assetIMEI)
                        .appendQueryParameter(STATUS_PARAM, assetStatus)
                        .appendQueryParameter(LAT_PARAM, assetLatitude)
                        .appendQueryParameter(LON_PARAM, assetLongitude)
                        .build();

                URL url = new URL(builtUri.toString());

                // Create Request
                Log.v(LOG_TAG, "Sending Status");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream == null) {
                    // Nothing to do.
                    return;
                }

                // for now, we just ignore response
                // DO NOTHING

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
            } catch (SecurityException e) {
                Log.e(LOG_TAG, "Error ", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }
    }

}
