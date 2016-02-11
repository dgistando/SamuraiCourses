package com.steakers.cg.howtofood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by dgist on 12/10/2015.
 */
public class Start extends Activity {
    Button signIn,signUp,search;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        signIn = (Button)findViewById(R.id.bsignin);
        signUp = (Button)findViewById(R.id.bsignup);
        search = (Button)findViewById(R.id.bsearch);
        db = new DBHelper(this);

        try{
            db.createDataBase();
        }catch (IOException ioe){
            throw new Error("Can't create databse");
        }

        try{
            db.openDatabase();
        }catch(SQLException sqle){
            sqle.printStackTrace();
        }

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Start.this, SignIn.class);
                startActivity(myIntent);
                Start.this.finish();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Start.this,SignUp.class);
                startActivity(myIntent);
                Start.this.finish();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Start.this,HTFActivity.class);
                startActivity(myIntent);
                myIntent.putExtra("bool","false");
                Start.this.finish();
            }
        });

    }


}
