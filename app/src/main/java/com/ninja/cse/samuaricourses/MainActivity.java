package com.ninja.cse.samuaricourses;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by ezequielcontreras on 4/25/16.
 */
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

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
                Intent intent = new Intent(MainActivity.this, Notification.class);
                startActivity(intent);
            }
        });
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

    }
}
