package com.brainupco.esupport;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.brainupco.esupport.service.AlarmReceiver;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity {

    public final String LOG_TAG = MainActivity.class.getSimpleName();

    // Settings
    String mAssetStatus;
    String mIMEI;

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set Action Bar Icon
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setIcon(R.drawable.ic_launcher);

        // Ensure Applying Preferences Default (Only first time)
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);

        // Check if IMEI is in preferences (testphone: 868442014378892)
        mIMEI = null; //Utility.getSavedAssetIMEI(this);
        if (mIMEI == null || mIMEI == "") {
            // Try to read Device IMEI
            mIMEI = Utility.getDeviceIMEI(this);

            // Save Value
            Utility.setAssetIMEI(this, mIMEI);
        }

        // Set Alarm to send Location Updates
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        int MyInt1 = R.integer.ALARM_START_DELAY;
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + this.getResources().getInteger(R.integer.ALARM_START_DELAY), this.getResources().getInteger(R.integer.ALARM_UPDATE_INTERVAL), pi);

//        // start the service
//        Intent tracking = new Intent(this, ESupportService.class);
//        this.startService(tracking);


//        // Create an instance of GoogleAPIClient.
//        if (mGoogleApiClient == null) {
//            mGoogleApiClient = new GoogleApiClient.Builder(this)
//                    .addConnectionCallbacks(this)
//                    .addOnConnectionFailedListener(this)
//                    .addApi(LocationServices.API)
//                    .build();
//        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Set initial or last saved status
        setAssetStatus(Utility.getSavedAssetStatus(this));

        // Report New Status
        //Utility.sendLocation(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putString("mAssetStatus",mAssetStatus);
//    }
//
//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        mAssetStatus = savedInstanceState.getString("mAssetStatus");
//    }

    // Set Button Action
    public void changeAssetStatus(View view) {
        // Toggle status (inactivo, do nothing)
        if (mAssetStatus.equalsIgnoreCase(getString(R.string.disponible))) {
            setAssetStatus(getString(R.string.ocupado));
        } else if (mAssetStatus.equalsIgnoreCase(getString(R.string.ocupado))) {
            setAssetStatus(getString(R.string.disponible));
        }

        // Report New Status
        //Utility.sendLocation(this);
    }

    public void setAssetStatus(String statusToSet) {
        // Change Text and Image to Reflect Asset Status
        ImageButton ib = (ImageButton) findViewById(R.id.btnAssetStatus);
        TextView assetStatus = (TextView) findViewById(R.id.assetStatus);

        // Set Text
        mAssetStatus = statusToSet;
        assetStatus.setText(statusToSet);

        // Set Image
        if (statusToSet.equalsIgnoreCase(getString(R.string.disponible))) {
            ib.setImageResource(R.drawable.disponible);
        } else if (statusToSet.equalsIgnoreCase(getString(R.string.inactivo))) {
            ib.setImageResource(R.drawable.inactivo);
        } else {
            ib.setImageResource(R.drawable.ocupado);

        }

        // Save Setting
        Utility.setAssetStatus(this, statusToSet);
    }
}
