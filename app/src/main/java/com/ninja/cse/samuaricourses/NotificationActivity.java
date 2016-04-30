package com.ninja.cse.samuaricourses;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dgist on 4/26/2016.
 */

public class NotificationActivity extends AppCompatActivity {

    AutoCompleteTextView Department, Classes;
    ArrayList<courses> listOfCourses;
    private ArrayAdapter<String> departmentAdapter, classesAdapter;
    ArrayList<String> classeslist = new ArrayList<String>();
    Button add,notify;
    courses[] arr = new courses[2];
    TextView tv1,tv2;


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

        Department.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String chosen = parent.getItemAtPosition(position).toString();
                listOfCourses.addAll(db.courseSearchByDepartment(chosen));

                Set<String> hashedset = new HashSet<>();
                String temp;
                for (courses number : listOfCourses) {
                    //gets rid of extra characters and duplicates
                    temp = number.getNumber();
                    temp = temp.substring(temp.indexOf("-"),temp.length());
                    hashedset.add(temp);
                }

                classesAdapter.clear();
                for (String each : hashedset) {
                    classesAdapter.add(each.replaceFirst("^0+(?!$)",""));
                    classesAdapter.notifyDataSetChanged();
                }

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
                    return;
                }else if(true){
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

    }




}
