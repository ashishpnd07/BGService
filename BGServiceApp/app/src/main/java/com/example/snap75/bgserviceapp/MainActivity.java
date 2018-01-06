package com.example.snap75.bgserviceapp;

import java.util.Calendar;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.snap75.service.MyTestService;
//http://www.sanfoundry.com/java-android-program-start-service-alarm-manager/
public class MainActivity extends Activity implements OnClickListener {

    public final static int REQUEST_CODE = -1010101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // Show alert dialog to the user saying a separate permission is needed
            // Launch the settings activity if the user prefers
            Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            startActivity(myIntent);
        }*/
       if (Build.VERSION.SDK_INT >= 23) {
            checkDrawOverlayPermission();
        } else {
           onCreateCode();
       }
    }
    void onCreateCode() {
        Button start = (Button) findViewById(R.id.start);
        Button end = (Button) findViewById(R.id.end);
        start.setOnClickListener(this);
        end.setOnClickListener(this);
        start.callOnClick();
        finish();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.start:
                startService();
                break;
            case R.id.end:
                stopService(new Intent(getBaseContext(), MyTestService.class));
                break;
            default:
                break;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkDrawOverlayPermission() {
        Log.v("App","Package Name: "+getApplicationContext().getPackageName());

        /** check if we already  have permission to draw over other apps**/
        if (!Settings.canDrawOverlays(MainActivity.this)) {
            Log.v("App","Requesting Permission"+Settings.canDrawOverlays(MainActivity.this));
            /** if not construct intent to request permission**/
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" +getApplicationContext().getPackageName()));
            /* request permission via start activity for result */
            startActivityForResult(intent, REQUEST_CODE);
        }else{
            Log.v("App","We already have permission for it.");
            onCreateCode();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        Log.v("App","OnActivity Result.");
        //check if received result code
        //  is equal our requested code for draw permission
        if (requestCode == REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    onCreateCode();
                }
            }
        } else {
            onCreateCode();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(this)) {
                onCreateCode();
            } else {
                onCreateCode();
            }
        }
    }

    void startService() {
        startService(new Intent(this, MyTestService.class));
        /*Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(this, MyTestService.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);

        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        // Start service every 20 seconds
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),20* 1000, pintent);*/
    }
}