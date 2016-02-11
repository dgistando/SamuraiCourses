package com.steakers.cg.howtofood;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


public class HTFActivity extends ListActivity {
    String search = "";

    Button press,bsignout;
    EditText esearch;
    ArrayList<String> stuffs = new ArrayList<String>();
    ListView lv;
    ArrayAdapter<String> adapter;
    ArrayAdapter<CharSequence> adapter1;
    DBHelper db;
    Spinner spinpress;
    TextView tvinas;
    boolean tr = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_htf);
        press = (Button)findViewById(R.id.bpress);
        esearch = (EditText)findViewById(R.id.esearch);
        bsignout = (Button)findViewById(R.id.bsignout);
        tvinas = (TextView)findViewById(R.id.tvinas);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,stuffs);
        spinpress = (Spinner)findViewById(R.id.spinpress);
        adapter1 = ArrayAdapter.createFromResource(this, R.array.spin_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinpress.setAdapter(adapter1);
        setListAdapter(adapter);
        db = new DBHelper(this);
        bsignout.setText(R.string.back);
         savedInstanceState = getIntent().getExtras();
        if(savedInstanceState != null) {
            if (savedInstanceState.getString("bool").equals("true")) {
                tvinas.setText("                             User:"+savedInstanceState.getString("name"));
                tr = true;
            }
            bsignout.setText(R.string.sign_out);
        }
        spinpress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                search = adapter1.getItem(position).toString();

                esearch.setHint("                          " + search);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bsignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tr){
                    onBackPressed(tr);
                }
                onBackPressed();
            }
        });
        press.setOnClickListener(new View.OnClickListener() {
            Cursor res = null;
            ArrayList<String> List = new ArrayList<String>();


            @Override
            public void onClick(View v) {
                stuffs.clear();


                if(search.equals("Name")) {
                    List = db.getRecipeK(esearch.getText().toString());
                    esearch.getText().clear();

                    while (List.size() != 0) {
                        addItem(List.get(0).toString());
                        List.remove(0);
                    }
                }
                if(search.equals("Ingredients")) {
                    String [] ingr_arr=esearch.getText().toString().split(",");
                    int ingrcount =ingr_arr.length;
                    StringBuilder sb = new StringBuilder();
                    sb.append("SELECT DISTINCT i_rname FROM ingredient WHERE i_item like '" + ingr_arr[0] + "%'");
                    for (int i = 0; i < ingrcount - 1; i++) {
                        sb.append(" INTERSECT SELECT DISTINCT i_rname FROM ingredient WHERE i_item like '" + ingr_arr[i + 1] + "%'");
                    }

                    List = db.getRecipeIng(sb.toString());
                    esearch.getText().clear();

                    while (List.size() != 0) {
                        addItem(List.get(0).toString());
                        List.remove(0);
                    }

                }
                if(search.equals("Category")){
                    List = db.getRecipebytype(esearch.getText().toString());
                    esearch.getText().clear();

                    while (List.size() != 0) {
                        addItem(List.get(0).toString());
                        List.remove(0);
                    }

                }
                if(search.equals("Rating")){
                    List = db.getRecipeRT();
                    esearch.getText().clear();

                    while (List.size() != 0) {
                        addItem(List.get(0).toString());
                        List.remove(0);
                    }
                }
                if(search.equals("Difficulty")){
                    List = db.getEasyRecipes();
                    esearch.getText().clear();

                    while (List.size() != 0) {
                        addItem(List.get(0).toString());
                        List.remove(0);
                    }
                }
                if(search.equals("Yield")){
                    List = db.getRecipeY(esearch.getText().toString());
                    esearch.getText().clear();

                    while (List.size() != 0) {
                        addItem(List.get(0).toString());
                        List.remove(0);
                    }

                }

            }
        });











    }



    public void addItem(String s){
        stuffs.add(s);
        adapter.notifyDataSetChanged();
        //stuffs.clear();
    }

    @Override
    protected void onListItemClick (ListView l, View v, int position, long id){
        super.onListItemClick(l, v, position, id);

        String Data = l.getItemAtPosition(position).toString();

        //String Data="deffwrgr";
        Intent myIntent = new Intent(HTFActivity.this,RecipeInfo.class);
        myIntent.putExtra("extra", Data);
        startActivity(myIntent);


    }

   // @Override
    public void onBackPressed(boolean tr){
        Intent myIntent = new Intent(HTFActivity.this,Start.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if(tr){
            myIntent.putExtra("bool","false");
        }
        startActivity(myIntent);
        HTFActivity.this.finish();
    }

    @Override
    public void onBackPressed(){
        Intent myIntent = new Intent(HTFActivity.this,Start.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(myIntent);
        HTFActivity.this.finish();
    }
}
