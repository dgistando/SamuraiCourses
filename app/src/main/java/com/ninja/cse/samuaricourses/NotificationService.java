package com.ninja.cse.samuaricourses;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by dgist on 4/29/2016.
 */
public class NotificationService extends Service {

    public static boolean serviceStarted = false;

    public NotificationService() {
        super();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceStarted = true;
        Toast.makeText(NotificationService.this, "Service was started", Toast.LENGTH_SHORT).show();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        serviceStarted = false;
        Toast.makeText(NotificationService.this, "Service was stopped", Toast.LENGTH_SHORT).show();
    }
}
