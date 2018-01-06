package com.example.snap75.service;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class MyTestService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, " MyService Created ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                if (Settings.canDrawOverlays(this)) {
                    showDialog();
                } else {
                    new RetrieveFeedTask().execute();
                    //Toast.makeText(this, " MyService Started", Toast.LENGTH_LONG).show();
                }
            } else {
                new RetrieveFeedTask().execute();
                //Toast.makeText(this, " MyService Started", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Servics Stopped", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    void showDialog() {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure,You wanted to make decision");
            alertDialogBuilder.setPositiveButton("yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            new RetrieveFeedTask().execute();
                            //Toast.makeText(getApplicationContext(),"You clicked yes button",Toast.LENGTH_LONG).show();
                        }
                    });

            alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            alertDialog.show();
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {

        }

        protected String doInBackground(Void... urls) {
            try {
                URL url = new URL("https://www.thecrazyprogrammer.com/example_data/fruits_array.json");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                startService();
                return null;
            }
        }

        protected void onPostExecute(String response) {
            try {
                if(response == null) {
                    response = "THERE WAS AN ERROR";
                }
                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                Log.i("INFO", response);
                startService();
            } catch (Exception ex) {
                ex.getMessage();
                startService();
            }
        }
    }

    void startService() {
        startService(new Intent(this, MyTestService.class));
    }

}