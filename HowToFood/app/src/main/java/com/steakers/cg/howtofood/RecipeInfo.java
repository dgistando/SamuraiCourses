package com.steakers.cg.howtofood;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by dgist on 12/9/2015.
 */
public class RecipeInfo extends Activity {
    TextView tvtitle,tvingredients,tvdirections;
    ArrayList<String> stuffs = new ArrayList<String>();
    DBHelper db;
    String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipeinfo);
        tvtitle = (TextView) findViewById(R.id.tvtitle);
        tvdirections = (TextView) findViewById(R.id.tvdirections);
        tvingredients = (TextView) findViewById(R.id.tvingredients);

        savedInstanceState = getIntent().getExtras();
        db = new DBHelper(this);

        if(savedInstanceState != null) {
            Scanner kbd = new Scanner(savedInstanceState.getString("extra"));
            s = kbd.nextLine();
            tvtitle.setText(s);
            kbd.close();
        }

        tvingredients.append("Ingredients:\n\n");

        stuffs = db.getRecipeI(s);
        while(stuffs.size() != 0){

            switch (stuffs.get(0)) {
                case ("0.25"):
                    stuffs.set(0, "1/4");
                    break;
                case (".25"):
                    stuffs.set(0, "1/4");
                    break;
                case (".5"):
                    stuffs.set(0, "1/2");
                    break;
                case ("0.5"):
                    stuffs.set(0, "1/2");
                    break;
                case ("0.75"):
                    stuffs.set(0, "3/4");
                    break;
                case (".75"):
                    stuffs.set(0, "3/4");
                    break;
            }

            tvingredients.append(stuffs.get(0) +" "+ stuffs.get(1)+"\n");
            stuffs.remove(0);
            stuffs.remove(0);
        }

        s = db.getRecipeD(s);
        tvdirections.setText("Directions: \n\n\u2022" + s.replace(". ",".\n\n\u2022 "));

    }
}
