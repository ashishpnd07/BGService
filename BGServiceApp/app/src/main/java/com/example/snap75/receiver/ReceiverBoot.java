package com.example.snap75.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.snap75.service.MyTestService;

import java.util.Calendar;

/**
 * Created by snap75 on 5/1/18.
 */

public class ReceiverBoot extends BroadcastReceiver {

    Context mContext;
    private final String BOOT_ACTION = "android.intent.action.BOOT_COMPLETED";

    private void startService() {
        //here, you will start your service
        mContext.startService(new Intent(mContext, MyTestService.class));
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(mContext, MyTestService.class);
        PendingIntent pintent = PendingIntent.getService(mContext, 0, intent, 0);

        AlarmManager alarm = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        // Start service every 20 seconds
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),20* 1000, pintent);
        Log.e("onReceiveBoot","in startService");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("onReceiveBoot","in onReceive");
        mContext = context;
        String action = intent.getAction();
        if (action.equalsIgnoreCase(BOOT_ACTION)) {
            startService();
        }
    }
}