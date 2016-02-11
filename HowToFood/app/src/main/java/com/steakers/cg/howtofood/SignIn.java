package com.steakers.cg.howtofood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by dgist on 12/10/2015.
 */
public class SignIn extends Activity{
    Button bsignin;
    EditText etUsername,etPassword;
    TextView tvwrong;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        bsignin = (Button)findViewById(R.id.bsignin);
        etUsername = (EditText)findViewById(R.id.etUsername);
        etPassword = (EditText)findViewById(R.id.etPassword);
        tvwrong = (TextView)findViewById(R.id.tvwrong);
        db = new DBHelper(this);

        bsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] check = new String[3];
                if(etUsername.getText().length() == 0 || etPassword.getText().length() == 0){
                    tvwrong.setText("Blank fields");
                    return;
                }
                check = db.getPassword(etUsername.getText().toString(),MD5(etPassword.getText().toString()));

                if(check[2] == "false"){
                    tvwrong.setText("Incorrect password");
                    etPassword.getText().clear();
                }else{
                    Intent myIntent = new Intent(SignIn.this,HTFActivity.class);
                    myIntent.putExtra("password",check[0]);
                    myIntent.putExtra("id",check[1]);
                    myIntent.putExtra("name",check[2]);
                    myIntent.putExtra("bool",check[3]);
                    startActivity(myIntent);
                    SignIn.this.finish();
                }
            }
        });



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


    @Override
    public void onBackPressed(){
        Intent myIntent = new Intent(SignIn.this,Start.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(myIntent);
        SignIn.this.finish();
    }

}
