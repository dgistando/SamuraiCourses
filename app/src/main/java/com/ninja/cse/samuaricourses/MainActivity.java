package com.ninja.cse.samuaricourses;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;

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
import com.microsoft.windowsazure.mobileservices.table.query.Query;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOperations;
import com.microsoft.windowsazure.mobileservices.table.sync.MobileServiceSyncContext;
import com.microsoft.windowsazure.mobileservices.table.sync.MobileServiceSyncTable;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.ColumnDataType;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.MobileServiceLocalStoreException;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.SQLiteLocalStore;
import com.microsoft.windowsazure.mobileservices.table.sync.synchandler.SimpleSyncHandler;
import com.squareup.okhttp.OkHttpClient;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by ezequielcontreras on 4/25/16.
 */
public class MainActivity extends Activity {

    private MobileServiceClient mClient;
    private MobileServiceSyncTable<courses> mCoursesTable;
    private ProgressBar mProgressBar;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        db = new DBHelper(this);
        db.createTables();
        final Animation animRotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);
        final Animation animTranslate = AnimationUtils.loadAnimation(this, R.anim.anim_translate);
        if(!isNetworkAvailable()){
            ShowNetworkAlert();
        }

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


            mCoursesTable = mClient.getSyncTable("courses", courses.class);
            initLocalStore();
            Button btnPlan=(Button)findViewById(R.id.plan);
            Button btnNotify=(Button)findViewById(R.id.notify);

            btnPlan.setOnClickListener(new Button.OnClickListener(){
                @Override

                public void onClick(View arg0) {
                    arg0.startAnimation(animTranslate);
                    Intent intent = new Intent(MainActivity.this, NinjaActivity.class);
                    startActivity(intent);
                    // overridePendingTransition(R.anim.left_out,R.anim.right_in);


                }});
            btnNotify.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View arg0) {
                    arg0.startAnimation(animTranslate);
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, NotificationActivity.class);
                    startActivity(intent);
                    //finish();
                    //overridePendingTransition(0,0);
                    // overridePendingTransition(R.anim.right_in,R.anim.left_out);
                    overridePendingTransition(android.R.anim.slide_out_right,android.R.anim.slide_in_left);
                }

            });
/*
            findViewById(R.id.plan).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, NinjaActivity.class);
                    startActivity(intent);
                }
            });
            findViewById(R.id.notify).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                    startActivity(intent);
                }
            });
            */
       /*     @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,NinjaActivity.class);
                startActivity(intent);

            }
        });
        Button sch = (Button)findViewById(R.id.plan);
        sch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NinjaActivity.class));
            }
        });
        Button ntf = (Button)findViewById(R.id.notify);
        sch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NinjaActivity.class));
            }
        });
*/

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void ConnectToServerAndInitialize(){
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

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mCoursesTable = mClient.getSyncTable("courses", courses.class);
    }

    /**
     * Initialize local storage
     * @return
     * @throws MobileServiceLocalStoreException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private AsyncTask<Void, Void, Void> initLocalStore() throws MobileServiceLocalStoreException, ExecutionException, InterruptedException {

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    MobileServiceSyncContext syncContext = mClient.getSyncContext();

                    if (syncContext.isInitialized())
                        return null;

                    SQLiteLocalStore localStore = new SQLiteLocalStore(mClient.getContext(), "OfflineStore", null, 1);


                    Map<String, ColumnDataType> tableDefinition = new HashMap<String, ColumnDataType>();
                    tableDefinition.put("crn", ColumnDataType.Integer);
                    tableDefinition.put("number", ColumnDataType.String);
                    tableDefinition.put("title", ColumnDataType.String);
                    tableDefinition.put("units", ColumnDataType.Integer);
                    tableDefinition.put("activity", ColumnDataType.String);
                    tableDefinition.put("days", ColumnDataType.String);
                    tableDefinition.put("time", ColumnDataType.String);
                    tableDefinition.put("room", ColumnDataType.String);
                    tableDefinition.put("length", ColumnDataType.String);
                    tableDefinition.put("instruction", ColumnDataType.String);
                    tableDefinition.put("maxEnrl", ColumnDataType.Integer);
                    tableDefinition.put("seatsAvailable", ColumnDataType.Integer);
                    tableDefinition.put("activeEnrl", ColumnDataType.Integer);
                    tableDefinition.put("sem_id", ColumnDataType.Integer);

                    localStore.defineTable("courses", tableDefinition);
                    SimpleSyncHandler handler = new SimpleSyncHandler();

                    syncContext.initialize(localStore, handler).get();

                    syncContext.push().get();
                    Query query = QueryOperations.tableName("courses");
                    mCoursesTable.pull(query).get();
                    //mCoursesTable = mClient.getSyncTable(courses.class);
                } catch (final Exception e) {
                    e.printStackTrace();
                }

                return null;
            }
        };

        return runAsyncTask(task);
    }

    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void ShowNetworkAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Application functions best with a network connection.");
        builder.setCancelable(true);
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Network Connection Error");
        alert.show();
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


            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.VISIBLE);
                }
            });

            ListenableFuture<ServiceFilterResponse> future = nextServiceFilterCallback.onNext(request);

            Futures.addCallback(future, new FutureCallback<ServiceFilterResponse>() {
                @Override
                public void onFailure(Throwable e) {
                    resultFuture.setException(e);
                }

                @Override
                public void onSuccess(ServiceFilterResponse response) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.GONE);
                        }
                    });

                    resultFuture.set(response);
                }
            });

            return resultFuture;
        }
    }

}
