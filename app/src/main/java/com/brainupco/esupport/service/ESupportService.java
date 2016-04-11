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

import com.brainupco.esupport.R;
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
    LocationManager lm;
    String best;

    public ESupportService() {
        super("ESupportService");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            best = lm.getBestProvider(criteria, true);

            // Check if has to request for location
            if (Utility.inServicewindow(this)) {
                lm.requestLocationUpdates(best, this.getResources().getInteger(R.integer.ALARM_UPDATE_INTERVAL), 100, this);
            }
        } catch (SecurityException e) {

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

//    @Override
//    protected void onHandleIntent(Intent intent) {
//    }

    @Override
    public void onLocationChanged(Location currentLocation) {
        if (currentLocation != null) {
            // Get Current Location Values
            String assetLatitude = Double.toString(currentLocation.getLatitude());
            String assetLongitude = Double.toString(currentLocation.getLongitude());

            // Save Current Location Values
            Utility.setAssetLatitude(this, assetLatitude);
            Utility.setAssetLongitude(this, assetLongitude);

        }

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        updateLocation();
    }

    private void updateLocation() {

        // Assume it doesn't has to run
        Boolean updateLocation = false;

        // Get Saved Values
        String assetIMEI = Utility.getSavedAssetIMEI(this);
        String assetStatus = Utility.getSavedAssetStatus(this);
        String assetLatitude = Utility.getSavedAssetLatitude(this);
        String assetLongitude = Utility.getSavedAssetLongitude(this);


        // Check if has to run
        if (Utility.inServicewindow(this)) {
            // Is in service windows, check if status is correct
            if (assetStatus.equalsIgnoreCase(getString(R.string.inactivo))) {
                // Set Status to Available
                assetStatus = getString(R.string.disponible);
                Utility.setAssetStatus(this, assetStatus);

            }

            // Must run
            updateLocation = true;

        } else {
            // Not in service windows, check if has to inactivate
            if (!assetStatus.equalsIgnoreCase(getString(R.string.inactivo))) {
                // Set Status to Inactive and notify it
                assetStatus = getString(R.string.inactivo);
                Utility.setAssetStatus(this, assetStatus);

                // Must run to notify
                updateLocation = true;
            }
        }

//        // If no Location info, cancel request
//        if (assetLatitude == "" || assetLatitude == "0") {
//            updateLocation = false;
//        }

        if (updateLocation) {

            // Execut only if its in Service Window
            Log.v(LOG_TAG, "Sending Location");

//            String assetLatitude = "19.4984899";
//            String assetLongitude = "-99.1692977";

            // Use URl Connection to Call Web Service
            HttpURLConnection urlConnection = null;

            try {

                // Try to Get Last Know Location
//                LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
//                Criteria criteria = new Criteria();
//                String best = lm.getBestProvider(criteria, true);
                Location currentLocation = lm.getLastKnownLocation(best);

                // Set values if location found
                // If not found it will send the las saved location
                if (currentLocation != null) {
                    // Get Current Location Values
                    assetLatitude = Double.toString(currentLocation.getLatitude());
                    assetLongitude = Double.toString(currentLocation.getLongitude());

                    // Save Current Location Values
                    Utility.setAssetLatitude(this, assetLatitude);
                    Utility.setAssetLongitude(this, assetLongitude);
                }

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
                        .appendQueryParameter(STATUS_PARAM, Utility.getWSStatus(this))
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
//                if (inputStream == null) {
//                    // Nothing to do.
//                    return;
//                }

                // for now, we just ignore response
                // DO NOTHING

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error->IOException: ", e);
            } catch (SecurityException e) {
                Log.e(LOG_TAG, "Error->SecurityException: ", e);
            } catch (IllegalArgumentException e) {
                Log.e(LOG_TAG, "Error->IllegalArgumentException: ", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(LOG_TAG, ">>>OnDestroy()");
    }
}
