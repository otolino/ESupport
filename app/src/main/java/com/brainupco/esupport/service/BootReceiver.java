package com.brainupco.esupport.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.brainupco.esupport.R;

/**
 * Created by jsantos on 08/abr/2016.
 * Used to start the service after a reboot.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        // Set Alarm to send Location Updates
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + context.getResources().getInteger(R.integer.ALARM_START_DELAY), context.getResources().getInteger(R.integer.ALARM_UPDATE_INTERVAL), pi);
    }
}
