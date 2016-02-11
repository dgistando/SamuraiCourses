package com.steakers.cg.howtofood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

/**
 * Created by dgist on 12/10/2015.
 */
public class SignUp extends Activity{
    TextView tvSignup,wrong;
    EditText etFirst,etLast,etUsername,etPassword;
    Button bcontinue;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        tvSignup = (TextView)findViewById(R.id.tvSignup);
        wrong = (TextView)findViewById(R.id.wrong);
        etFirst = (EditText)findViewById(R.id.etFirst);
        etLast = (EditText)findViewById(R.id.etLast);
        etUsername = (EditText)findViewById(R.id.etUsername);
        etPassword = (EditText)findViewById(R.id.etPassword);
        bcontinue = (Button)findViewById(R.id.bcontinue);
        db = new DBHelper(this);

        bcontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etFirst.getText().length() == 0 ||
                        etLast.getText().length() == 0 ||
                        etUsername.getText().length() == 0 ||
                        etPassword.getText().length() == 0){
                    wrong.setText("Blank field");
                    return;
                }

                if(db.inuname(etFirst.getText().toString(),
                               etLast.getText().toString(),
                               etUsername.getText().toString(),
                               MD5(etPassword.getText().toString()))){

                    Toast.makeText(SignUp.this,"Welcone "+etFirst.getText().toString() + "!",Toast.LENGTH_SHORT);
                }else{
                    wrong.setText("Username is taken");
                    return;
                }

                Intent myIntent = new Intent(SignUp.this,HTFActivity.class);
                myIntent.putExtra("password", etPassword.getText().toString());
                myIntent.putExtra("id","0");
                myIntent.putExtra("name",etFirst.getText().toString()+" "+etLast.getText().toString());
                myIntent.putExtra("bool","true");
                startActivity(myIntent);
                SignUp.this.finish();
            }
        });





    }

    @Override
    public void onBackPressed(){
        Intent myIntent = new Intent(SignUp.this,Start.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(myIntent);
        SignUp.this.finish();
    }

    //Password encryption
    private String MD5(String password){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            //Converting to hex
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }

}
