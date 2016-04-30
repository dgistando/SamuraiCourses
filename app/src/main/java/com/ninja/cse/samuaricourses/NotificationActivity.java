package com.ninja.cse.samuaricourses;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by dgist on 4/26/2016.
 */

public class NotificationActivity extends AppCompatActivity {

    private MobileServiceClient mClient;
    private MobileServiceTable<courses> mCoursesTable;
    private ProgressBar mProgressBar;


    AutoCompleteTextView Department, Classes;
    ArrayList<courses> listOfCourses;
    private ArrayAdapter<String> departmentAdapter, classesAdapter;
    ArrayList<String> classeslist = new ArrayList<String>();
    Button add,notify;
    courses[] arr = new courses[2];
    TextView tv1,tv2;
    static int selection=0;

    DBHelper db;
    //removed from layout: <android:layout_alignEnd="@+id/btnTrack" under add button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        add = (Button)findViewById(R.id.btnAdd);
        notify = (Button)findViewById(R.id.getNotified);
        listOfCourses = new ArrayList<courses>();
        tv1 = (TextView)findViewById(R.id.textView);
        tv2 = (TextView)findViewById(R.id.textView2);


        db = new DBHelper(this);

        classesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, classeslist);
        Classes = (AutoCompleteTextView) findViewById(R.id.number);
        Classes.setThreshold(1);
        Classes.setAdapter(classesAdapter);
        Classes.setHint("Class");

        departmentAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.Department_array));
        Department = (AutoCompleteTextView) findViewById(R.id.department);
        Department.setAdapter(departmentAdapter);
        Department.setThreshold(1);
        Department.setHint("Department");


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

            mCoursesTable = mClient.getTable("courses",courses.class);

        Department.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listOfCourses.clear();
                String chosen = parent.getItemAtPosition(position).toString();
                listOfCourses.addAll(db.courseSearchByDepartment(chosen));

                Set<String> hashedset = new HashSet<>();
                String temp;
                for (courses number : listOfCourses) {
                    //gets rid of extra characters and duplicates
                    temp = number.getNumber();
                    temp = temp.substring(temp.indexOf("-")+1,temp.length());
                    hashedset.add(temp);
                }

                classesAdapter.clear();
                for (String each : hashedset) {
                    classesAdapter.add(each.replaceFirst("^0+(?!$)",""));
                    classesAdapter.notifyDataSetChanged();
                }

            }
        });

        Classes.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selection = position;
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Department.getText().equals("") || Classes.getText().equals("")){
                    Toast.makeText(NotificationActivity.this,"fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(tv1.getText().toString().equals("")){
                    arr[0] = listOfCourses.get(selection);
                    tv1.setText(arr[0].getCrn() + " " + arr[0].getNumber());
                    return;
                }else if(tv2.getText().toString().equals("")){
                    arr[0] = listOfCourses.get(selection);
                    tv2.setText(arr[0].getCrn() + " " + arr[0].getNumber());
                    add.setEnabled(false);
                    return;
                }else{
                    return;
                }



            }
        });

        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * This method performs an Asynchronous task that adds a list of course number to the
     * classes TextView.
     *
     * @param selectedDepartment String from the first dropdown list of textView ex:CSE
     */
    private void getActiveEnrl(final String selectedDepartment)throws ExecutionException, InterruptedException{

        AsyncTask<Void,Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try{
                    Log.d("get Query", "Just before getting query");
                    final courses course = availableClasses(selectedDepartment);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //put logic for checking enrollment here
                        }
                    });

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
    private courses availableClasses(String item) throws ExecutionException, InterruptedException{
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
