package com.brainupco.esupport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

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

        // Check if IMEI is in preferences (testphone: 868442014378892)
        mIMEI = null; //Utility.getSavedAssetIMEI(this);
        if (mIMEI == null || mIMEI == "") {
            // Try to read Device IMEI
            mIMEI = "359837050577255"; //Utility.getDeviceIMEI(this);

            // Save Value
            Utility.setAssetIMEI(this, mIMEI);
        }

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Set initial or last saved status
        setAssetStatus(Utility.getSavedAssetStatus(this));
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
        // Toggle status
        if (mAssetStatus == getString(R.string.disponible)) {
            setAssetStatus(getString(R.string.ocupado));
        } else {
            setAssetStatus(getString(R.string.disponible));
        }
    }

    private void setAssetStatus(String statusToSet) {
        // Change Text and Image to Reflect Asset Status
        ImageButton ib = (ImageButton) findViewById(R.id.btnAssetStatus);
        TextView assetStatus = (TextView) findViewById(R.id.assetStatus);

        // Set Text
        mAssetStatus = statusToSet;
        assetStatus.setText(statusToSet);

        // Set Image
        if (statusToSet == getString(R.string.disponible)) {
            ib.setImageResource(R.drawable.disponible);
        } else {
            ib.setImageResource(R.drawable.ocupado);

        }

        // Save Setting
        Utility.setAssetStatus(this, statusToSet);
    }
}
