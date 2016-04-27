package com.ninja.cse.samuaricourses;

import android.app.Activity;
import android.os.Bundle;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

/**
 * Created by dgist on 4/26/2016.
 */

public class NotificationActivity extends Activity {

    private MobileServiceClient mClient;
    private MobileServiceTable<courses> mCoursesTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.notification);

    }
}
