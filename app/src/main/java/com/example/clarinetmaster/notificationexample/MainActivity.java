package com.example.clarinetmaster.notificationexample;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.clarinetmaster.notificationexample.AlarmReciever.AlarmReciever;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private int id;
    private int parsingNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parsingNumber = 159;
    }

    public void onCreateButtonClick(View v){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 5);
        id = createNotification(this, calendar);
    }

    public void onCancelButtonClick(View v){
        cancelNotification(this, id);
    }

    public int createNotification(Context context, Calendar triggerTime){

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

        Intent serviceIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION"); // this intent goes to AlarmReciever.onRecieve perameter

        serviceIntent.putExtra("item", parsingNumber); // how to parse value to notification

        int notificationID = 100; // set id for identify which notification

        int cancel = PendingIntent.FLAG_CANCEL_CURRENT; // this flag will cancel all of old one and create this one
        int one_shot = PendingIntent.FLAG_ONE_SHOT; // this flag will set this pendingIntent to can be used only once
        int update_current = PendingIntent.FLAG_UPDATE_CURRENT; // this flag will make old one with the same pendingIntent replaced with additional data this one
        int no_flag = 0; // use if not want any flags

        PendingIntent broadcast = PendingIntent.getBroadcast(context, notificationID, serviceIntent, cancel); // what to do if notification touched
        long notificationAlertTime = triggerTime.getTimeInMillis();

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, notificationAlertTime, broadcast);

        Log.i(TAG, "Notification created at "+triggerTime.getTime());

        return notificationID;

    }

    public void cancelNotification(Context context, int cancelNotificationID){

        /**
         * cancelNotificationID MUST = notoficationID on create method
         */

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent cancelServiceIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        PendingIntent cancelServicePendingIntent = PendingIntent.getBroadcast(
                context,
                cancelNotificationID, // integer constant used to identify the service
                cancelServiceIntent,
                0 //no FLAG needed for a service cancel
        );
        alarmManager.cancel(cancelServicePendingIntent);
        Log.i(TAG, "Notification canceled");
    }

}
