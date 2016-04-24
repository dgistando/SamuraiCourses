package com.ninja.cse.samuaricourses;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.sql.DatabaseMetaData;

/**
 * Created by dgist on 4/22/2016.
 */
public class DBHelper extends SQLiteOpenHelper{
    static final String DATABASE_NAME = "OfflineStore.db";
    static final String TABLE_NAME = "courses";

    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

}
