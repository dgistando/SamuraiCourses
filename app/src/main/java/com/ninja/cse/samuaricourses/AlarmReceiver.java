package com.ninja.cse.samuaricourses;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String ACTION_ALARM_RECEIVER = "ACTION_ALARM_RECEIVER";

    @Override
    public void onReceive(Context context, Intent intent) {

        ArrayList<courses> trackedCourses = intent.getParcelableArrayListExtra("CLASSES");

        // this toast is a test for our recurring task
        //Toast.makeText(context, "oh hi", Toast.LENGTH_SHORT).show();

        // not the right way to do, but ok for now
        // assume connection good
        // make a query for the list of courses being tracked; check if seats are a certain value/range, return that value
        // pass course and seats to Notification
//        for(int i=0; i<trackedCourses.size(); i++){
//            //mToDoTable.where().startsWith("number", trackedCourses.get(i)).and().lect.execute().get();
//
//            Notification(context, trackedCourses.get(i), seats, i);
//        }

        intent = new Intent(context, NotificationService.class);

        int i=0;
        for(courses entity : trackedCourses){
            Log.d("TRACKEDCOURSES",entity.getCrn()+" "+entity.getNumber());

            intent.putExtra("crn" + i++,entity.getCrn());

            if(entity.getSeatsAvailable() > 0){
                intent.putExtra(entity.getCrn()+"CHECK5",true);
                intent.putExtra(entity.getCrn()+"i",entity.getSeatsAvailable());
            }else{
                intent.putExtra(entity.getCrn()+"CHECK0",true);
            }

        }
        intent.putExtra("trackedCourses.size()",trackedCourses.size());

        context.startService(intent);


        //Notification(context, intent.getStringExtra("testval"), "5", 0);

        // intent = new Intent(context, NotificationService.class);
        context.stopService(intent);
        //Notification(context, trackedCourses.get(1), "4", 1);
    }

    public void Notification(Context context, String course, String seats, int notifyID) {
        // Open NotificationView Class on Notification Click
        Intent intent = new Intent(context, MainActivity.class);

        // Open NotificationView.java Activity
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Create Notification using NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context)
                // Set Icon
                .setSmallIcon(R.drawable.testicon)
                // Set Ticker Message
                //.setTicker(message)
                // Set Title;
                .setContentTitle(course + " has " + seats + " spots left!")
                // Set Text
                .setContentText("Register Now!")
                // Add an Action Button below Notification
                //.addAction(R.drawable.testicon, "Action Button", pIntent)
                // Set PendingIntent into Notification
                .setContentIntent(pIntent)
                // Dismiss Notification
                .setAutoCancel(true);

        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        notificationmanager.notify(notifyID, builder.build());

    }
}