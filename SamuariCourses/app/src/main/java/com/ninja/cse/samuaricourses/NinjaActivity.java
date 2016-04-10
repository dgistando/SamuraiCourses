package com.ninja.cse.samuaricourses;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
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
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.xml.transform.Result;

import static com.microsoft.windowsazure.mobileservices.table.query.QueryOperations.*;



public class NinjaActivity extends AppCompatActivity {

    private MobileServiceClient mClient;
    private MobileServiceTable<courses> mCoursesTable;
    private ProgressBar mProgressBar;
    //private scheduleDatabase db = new scheduleDatabase();

    private AutoCompleteTextView department,classes;
    private ArrayAdapter<String> departmentAdapter,classesAdapter;
    ArrayList<String> classeslist = new ArrayList<String>();

    /**
     * This is the current starting point for the app. It loads the
     * autocomplete textviews and the buttons.
     *
     * @param savedInstanceState saves the state of the app when moving to a new onCreate. You
     *                           can save objects in the the Bundle to be carried over to other
     *                           activities
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ninja);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        classesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, classeslist);
        classes = (AutoCompleteTextView) findViewById(R.id.number);
        classes.setThreshold(1);
        classes.setAdapter(classesAdapter);
        classes.setHint("Class");

        departmentAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.Department_array));
        department = (AutoCompleteTextView) findViewById(R.id.department);
        department.setAdapter(departmentAdapter);
        department.setThreshold(1);
        department.setHint("Department");

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

                        department.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Log.d("Item Clicked", "Item was clicked");
                                ArrayAdapter<String> departmentTagAdapter = new ArrayAdapter<String>(NinjaActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Department_tag_array));
                                ArrayAdapter<String> departmentTempAdapter = new ArrayAdapter<String>(NinjaActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Department_array));

                                String chosen = parent.getItemAtPosition(position).toString();
                                int newpos = departmentTempAdapter.getPosition(chosen);
                                chosen = departmentTagAdapter.getItem(newpos);
                                Log.w("EndCheck", chosen);
                                setClasseslist(chosen);
                            }
                        });

        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

        Button btn = (Button)findViewById(R.id.generateBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NinjaActivity.this, Generations.class));
            }
        });

    }


    /**
     * This method adds an options menu to the top at the start of the app.
     *
     * @param menu functions like a list to edit the options
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ninja, menu);
        return true;
    }

    /**
     * Handle action bar item clicks here. The action bar will
     * automatically handle clicks on the Home/Up button, so long
     * as you specify a parent activity in AndroidManifest.xml.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    {/*private class scheduleDatabase extends AsyncTask<String,Void,ArrayList<courses>> {
        private ArrayList<courses> coursesList;


        @Override
        protected ArrayList<courses> doInBackground(String... params) {
            try {

                coursesList = availableClasses(params[0]);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Set<String> hashedset = new HashSet<>();
                        String temp;
                        for (courses number : coursesList) {
                            //gets rid of extra characters and duplicates
                            temp = number.getNumber();
                            temp = temp.substring(temp.indexOf("-") + 1);
                            temp = temp.substring(0, temp.indexOf("-"));
                            hashedset.add(temp);
                        }

                        classesAdapter.clear();
                        for (String each : hashedset) {
                            classesAdapter.add(each);
                            classesAdapter.notifyDataSetChanged();
                        }
                    }
                });
                return coursesList;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        }
    }*/}

    /**
     * This method performs an Asynchronous task that adds a list of course number to the
     * classes TextView.
     *
     * @param selectedDepartment String from the first dropdown list of textView ex:CSE
     */
    private void setClasseslist(final String selectedDepartment){
    //Plan to make nested class
        AsyncTask<Void,Void,Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try{
                    Log.d("get Query", "Just before getting query");
                    final ArrayList<courses> coursesList = availableClasses(selectedDepartment);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Set<String> hashedset = new HashSet<>();
                            String temp;
                            for (courses number : coursesList) {
                                //gets rid of extra characters and duplicates
                                temp = number.getNumber();
                                temp = temp.substring(temp.indexOf("-") + 1);
                                temp = temp.substring(0, temp.indexOf("-"));
                                hashedset.add(temp);
                            }

                            classesAdapter.clear();
                            for (String each : hashedset) {
                                classesAdapter.add(each);
                                classesAdapter.notifyDataSetChanged();
                            }
                        }
                    });

                }catch(final Exception e){
                    e.printStackTrace();
                }
                return null;
            }

            /*@Override
            protected void onPostExecute(Void Result){
                return ;
            }*/

        };
        task.execute();
    }

    /**
     * Runs LINQ to database and retrieves list of courses that have cse in the title.
     *
     * @param item string passed from the setClasseslist method and added to the LINQ query
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private ArrayList<courses> availableClasses(String item) throws ExecutionException, InterruptedException{
            ArrayList<courses> entities =  mCoursesTable.where().startsWith("number",item).execute().get();
        return entities;
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
