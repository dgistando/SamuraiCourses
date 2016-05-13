package com.ninja.cse.samuaricourses;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by dgist on 4/26/2016.
 */

public class NotificationActivity extends AppCompatActivity {

    Button btnAdd;
    Switch btnNotify;

    AutoCompleteTextView Department, Classes;
    private ArrayAdapter<String> departmentAdapter, classesAdapter;
    ArrayList<String> classeslist = new ArrayList<String>();
    ArrayList<courses> coursesToPass = new ArrayList<courses>();

    ArrayList<courses> selectedCourses = new ArrayList<courses>();

    private PendingIntent pendingIntent;
    private AlarmManager manager;
    SharedPreferences sharedPreferences;


    DBHelper db;
    //removed from layout: <android:layout_alignEnd="@+id/btnTrack" under add button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        db = new DBHelper(this);
        sharedPreferences = getSharedPreferences(this.getPackageName(),MODE_PRIVATE);

        btnAdd = (Button)findViewById(R.id.btnAdd);
        //final Button btnNotify = (Button)findViewById(R.id.btnNotify);

        btnNotify = (Switch)findViewById(R.id.btnNotify);
        btnNotify.setChecked(sharedPreferences.getBoolean("isChecked",false));

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


        Department.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCourses.clear();
                ArrayAdapter<String> departmentTagAdapter = new ArrayAdapter<String>(NotificationActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Department_tag_array));
                ArrayAdapter<String> departmentTempAdapter = new ArrayAdapter<String>(NotificationActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Department_array));

                String chosen = parent.getItemAtPosition(position).toString();
                int newpos = departmentTempAdapter.getPosition(chosen);
                chosen = departmentTagAdapter.getItem(newpos);

                selectedCourses.addAll(db.courseSearchByDepartment(chosen));

                classesAdapter.clear();
                for (courses each : selectedCourses) {
                    classesAdapter.add(each.getNumber().substring(each.getNumber().indexOf("-")+1,each.getNumber().length()).replaceFirst("^0+(?!$)", ""));
                    classesAdapter.notifyDataSetChanged();
                    //Log.d("ON LIST",classesAdapter.getItem(classesAdapter.getCount()-1));
                }
            }
        });

        final ListView listview = (ListView) findViewById(R.id.listViewNotification);
        final ArrayList<String> listToTrackCourses = new ArrayList<String>();
        final ArrayAdapter<String> listadapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_listitems, listToTrackCourses);
        listview.setAdapter(listadapter);
        //updates the list to whats in database
        listadapter.addAll(db.retrieveNotificationCourses());
        listadapter.notifyDataSetChanged();
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                if (!listview.isItemChecked(position)) {
                    String temp = listadapter.getItem(position);
                    db.deleteNotificationCourses(temp);
                    listadapter.remove(temp);
                    listadapter.notifyDataSetChanged();
                    btnNotify.setChecked(false);
                    if(sharedPreferences.getBoolean("isChecked",false)){
                        cancelAlarm();
                    }
                }
            }
        };
        listview.setOnItemClickListener(itemClickListener);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listToTrackCourses.size() > 3) {
                    Toast.makeText(NotificationActivity.this, "Tracking maxed at 3 courses!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Department.getText().toString().equals("") || Classes.getText().toString().equals("")) {
                    Toast.makeText(NotificationActivity.this, " Select valid courses", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Rebuilding the string to match tables
                int numFromNumber = Integer.parseInt(Classes.getText().toString().substring(0,Classes.getText().toString().indexOf('-')));
                String restOfString = Classes.getText().toString().substring(Classes.getText().toString().indexOf('-'), Classes.getText().toString().length());
                listadapter.add(Department.getText().toString() + "-" + String.format("%03d",numFromNumber) + restOfString);
                listview.setItemChecked(listadapter.getCount() - 1, true);
                listadapter.notifyDataSetChanged();


                classeslist.clear();
                Department.setText("");
                Classes.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(NinjaActivity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                btnNotify.setChecked(false);
                if(sharedPreferences.getBoolean("isChecked",false)) {
                    cancelAlarm();
                }
            }
        });

        btnNotify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if(
                        isChecked){
                    Intent intent = new Intent(NotificationActivity.this, AlarmReceiver.class);
                    Bundle bundle = new Bundle();

                    if (listToTrackCourses.size() == 0 || listToTrackCourses.size() > 3) {
                        return;
                    }else {

                        db.deleteNotificationCourses("ALL");
                        coursesToPass.clear();
                        for (int j = 0; j < listToTrackCourses.size(); j++) {
                            //Log.d("NOTIFY_TEST", listToTrackCourses.get(j));
                            coursesToPass.add(db.courseSearchByNumber(listToTrackCourses.get(j)));
                            Log.d("ARRCRN", coursesToPass.get(j).getCrn() + "");
                            if (!db.insertCourse(coursesToPass.get(j).getCrn(), coursesToPass.get(j).getNumber())) {
                                Log.d("INSERT", "insert failed");
                                return;
                            }
                        }

                        intent.setAction(AlarmReceiver.ACTION_ALARM_RECEIVER);
                        bundle.putParcelableArrayList("CLASSES", coursesToPass);
                        intent.putExtras(bundle);


                        // Retrieve a PendingIntent that will perform a broadcast with the intent and its extras above
                        pendingIntent = PendingIntent.getBroadcast(NotificationActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


                        //btnNotify.setText("STOP");
                        startAlarm();
                    }
                    editor.putBoolean("isChecked",true);
                }else{
                    cancelAlarm();
                    editor.putBoolean("isChecked",false);
                }
                editor.apply();
            }
        });

        /*btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //alarmOn = (MypendingIntent != null);
                //manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

                if(alarmOn){
                    cancelAlarm(v);
                    //btnNotify.setText("GET NOTIFIED");
                    return;
                }

                Intent intent = new Intent(NotificationActivity.this, AlarmReceiver.class);
                Bundle bundle = new Bundle();

                if (listToTrackCourses.size() == 0)
                    return;
                else {

                    for(int j =0;j<listToTrackCourses.size();j++) {
                        //Log.d("NOTIFY_TEST", listToTrackCourses.get(j));
                        coursesToPass.add(db.courseSearchByNumber(listToTrackCourses.get(j)));
                        Log.d("ARRCRN",coursesToPass.get(j).getCrn() + "");
                        db.deleteNotificationCourses(coursesToPass.get(j).getNumber());
                        if(!db.insertCourse(coursesToPass.get(j).getCrn(), coursesToPass.get(j).getNumber())){
                            Log.d("INSERT", "insert failed");
                            return;
                        }
                    }
<<<<<<< HEAD


                    intent.setAction(AlarmReceiver.ACTION_ALARM_RECEIVER);
=======
>>>>>>> b7ba1000a671122e69a763005778bb7ccd61bec6
                    bundle.putParcelableArrayList("CLASSES", coursesToPass);
                    intent.putExtras(bundle);
                    // Retrieve a PendingIntent that will perform a broadcast with the intent and its extras above
                    pendingIntent = PendingIntent.getBroadcast(NotificationActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


                    //btnNotify.setText("STOP");
                    startAlarm(v);

                    //alarmOn = (pendingIntent != null);
                    //manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                }
            }
        });*/
    }
    @Override
    public void onBackPressed() {
        // finish();
        super.onBackPressed();

        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    public void startAlarm() {
        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        int interval = 15000;

        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Toast.makeText(NotificationActivity.this, "You will get notified!", Toast.LENGTH_SHORT).show();
    }


    public void cancelAlarm() {
        //alarmOn = (pendingIntent != null);
        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        //if (alarmOn) {
            manager.cancel(pendingIntent);
            Toast.makeText(this, "Notifications Canceled", Toast.LENGTH_SHORT).show();
            manager = null;
        //}
    }


    private void CheckForAlarm()
    {
        //checking if alarm is working with pendingIntent #3
        Intent intent = new Intent(getApplicationContext()  , AlarmReceiver.class);//the same as up
        intent.setAction(AlarmReceiver.ACTION_ALARM_RECEIVER);//the same as up
        boolean isWorking = (PendingIntent.getBroadcast(getApplicationContext() , 1001, intent, PendingIntent.FLAG_NO_CREATE) != null);//just changed the flag
        Log.d("TAG: TEST APP:  ", "alarm is " + (isWorking ? "" : "not") + " working...");

    }

}
