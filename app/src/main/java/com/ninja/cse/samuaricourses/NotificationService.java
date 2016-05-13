package com.ninja.cse.samuaricourses;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.squareup.okhttp.OkHttpClient;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

/**
 * Created by dgist on 4/29/2016.
 */
public class NotificationService extends Service {

    private MobileServiceClient mClient;
    private MobileServiceTable<courses> mCoursesTable;
    private ProgressBar mProgressBar;
    ArrayList<courses> ReceivedCourses = new ArrayList<courses>();

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


        try {
            mClient = new MobileServiceClient(
                    "https://samuraicourses.azurewebsites.net",
                    this).withFilter(new ProgressFilter());
            mClient.setAndroidHttpClientFactory(new OkHttpClientFactory() {
                /**
                 * makes an http request to the server for this application and
                 * passes the response to progress filter.
                 *
                 * @return
                 */
                @Override
                public OkHttpClient createOkHttpClient() {
                    OkHttpClient client = new OkHttpClient();
                    client.setReadTimeout(20, TimeUnit.SECONDS);
                    return client;
                }
            });

            mCoursesTable = mClient.getTable(courses.class);
            if(intent.getIntExtra("crn"+0,-99) != -99){
                getActiveEnrl(intent.getIntExtra("crn"+0,-99));
            }

            if(intent.getIntExtra("crn"+1,-99) != -99){
                getActiveEnrl(intent.getIntExtra("crn"+1,-99));
            }

            if(intent.getIntExtra("crn"+2,-99) != -99){
                getActiveEnrl(intent.getIntExtra("crn"+2,-99));
            }

            //getActiveEnrl(intent.getIntExtra("crn"+1,-99));
            //getActiveEnrl(intent.getIntExtra("crn"+2,-99));


            for(int i=0;i<ReceivedCourses.size();i++){
                Log.d("RECEIVED COURSES",ReceivedCourses.get(i).getCrn()+" "+ReceivedCourses.get(i).getNumber());

                if(intent.getBooleanExtra(ReceivedCourses.get(i).getCrn()+"CHECK5",false)){
                    if(ReceivedCourses.get(i).getSeatsAvailable() < intent.getIntExtra(ReceivedCourses.get(i).getCrn()+"i",-99)){
                        Notification(this,ReceivedCourses.get(i).getNumber(),ReceivedCourses.get(i).getSeatsAvailable()+"",0,false);
                        ReceivedCourses.remove(i);
                    }
                }else if(intent.getBooleanExtra(ReceivedCourses.get(i).getCrn()+"CHECK0",false) && ReceivedCourses.get(i).getSeatsAvailable() > 0){
                    Notification(this,ReceivedCourses.get(i).getNumber(),ReceivedCourses.get(i).getSeatsAvailable()+"",0,true);
                    ReceivedCourses.remove(i);
                }
            }


        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        serviceStarted = false;
        Toast.makeText(NotificationService.this, "Service was stopped", Toast.LENGTH_SHORT).show();
    }

    /**
     * This method performs an Asynchronous task that adds a list of course number to the
     * classes TextView.
     *
     * @param selectedDepartment String from the first dropdown list of textView ex:CSE
     */
    private void getActiveEnrl(final int selectedDepartment)throws ExecutionException, InterruptedException{

        AsyncTask<Void,Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try{
                    ReceivedCourses.clear();
                    Log.d("get Query", "Just before getting query selected department: " + selectedDepartment);
                    final courses course = availableClasses(selectedDepartment);

                    new Thread(new Runnable() {
                        public void run() {
                            //Log.d("RESULT SET:", "crn: "+course.getCrn() + " seatsAvail: " + course.getSeatsAvailable());
                            ReceivedCourses.add(course);
                        }
                    }).start();

                }catch(final Exception e){
                    e.printStackTrace();
                }
                return null;
            }

        };
        task.execute();
    }

    /**
     * Runs LINQ to database and retrieves course that the user selected.
     *
     * @param item string passed from the getActiveEnrl method and added to the LINQ query
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private courses availableClasses(int item) throws ExecutionException, InterruptedException{
        courses entity =  mCoursesTable.where().field("crn").eq(item).execute().get().get(0);
        return entity;
    }


    private class ProgressFilter implements ServiceFilter {

        /**
         * method that handles the html response from the client.
         *
         * Plan to implement the progress bar for longer wait times.
         *
         * @param request
         * @param nextServiceFilterCallback
         * @return
         */
        @Override
        public ListenableFuture<ServiceFilterResponse> handleRequest(ServiceFilterRequest request, NextServiceFilterCallback nextServiceFilterCallback) {

            final SettableFuture<ServiceFilterResponse> resultFuture = SettableFuture.create();

            ListenableFuture<ServiceFilterResponse> future = nextServiceFilterCallback.onNext(request);

            Futures.addCallback(future, new FutureCallback<ServiceFilterResponse>() {
                @Override
                public void onFailure(Throwable e) {
                    resultFuture.setException(e);
                }

                @Override
                public void onSuccess(ServiceFilterResponse response) {
                    resultFuture.set(response);
                }
            });

            return resultFuture;
        }
    }

    public void Notification(Context context, String course, String seats, int notifyID, boolean classOpening) {
        // Open NotificationView Class on Notification Click
        Intent intent = new Intent(context, MainActivity.class);

        // Open NotificationView.java Activity
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Create Notification using NotificationCompat.Builder

        String content ="";

        if(classOpening){
            content = course + " just opened with " + seats + " spot!";
        }else{
            content = course + " has " + seats + " spots left!";
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context)
                // Set Icon
                .setSmallIcon(R.drawable.testicon)
                .setVibrate(new long[]{1000,1000,1000})
                // Set Ticker Message
                //.setTicker(message)
                // Set Title;
                .setContentTitle(content)
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
