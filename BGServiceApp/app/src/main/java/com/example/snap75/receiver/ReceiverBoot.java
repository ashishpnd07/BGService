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
        try {
            mContext.startService(new Intent(mContext, MyTestService.class));
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Log.e("onReceiveBoot","in onReceive");
            mContext = context;
            String action = intent.getAction();
            if (action.equalsIgnoreCase(BOOT_ACTION)) {
                startService();
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
    }
}