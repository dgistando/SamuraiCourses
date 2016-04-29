package com.ninja.cse.samuaricourses;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.ArrayList;

/**
 * Created by dgist on 4/26/2016.
 */

public class NotificationActivity extends AppCompatActivity {

    AutoCompleteTextView Department, Classes;
    private ArrayAdapter<String> departmentAdapter, classesAdapter;
    ArrayList<String> classeslist = new ArrayList<>();
    Button add,notify;


    DBHelper db;
    //removed from layout: <android:layout_alignEnd="@+id/btnTrack" under add button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        add = (Button)findViewById(R.id.btnAdd);
        notify = (Button)findViewById(R.id.notify);

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





        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Department.getText().equals("") || Classes.getText().equals("")){
                    Toast.makeText(NotificationActivity.this,"fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }




            }
        });

    }




}
