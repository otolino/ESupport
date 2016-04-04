package com.brainupco.esupport.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    public final String LOG_TAG = ESupportService.class.getSimpleName();

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "Recurring alarm; requesting location tracking.");

        // start the service
        Intent tracking = new Intent(context, ESupportService.class);
        context.startService(tracking);

//        throw new UnsupportedOperationException("Not yet implemented");
    }
}
