package com.ninja.cse.samuaricourses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;
import java.sql.Blob;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;

/**
 * Created by dgist on 4/22/2016.
 */
public class DBHelper extends SQLiteOpenHelper{
    static final String DATABASE_NAME = "OfflineStore";
    static final String DATABASE_PATH = "/data/data/com.ninja.cse.samuaricourses/databases/";
    static final String TABLE_NAME = "courses";

    SQLiteDatabase mDatabase;// = SQLiteDatabase.openDatabase(DATABASE_PATH+DATABASE_NAME,null,SQLiteDatabase.OPEN_READONLY);

    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db = SQLiteDatabase.openOrCreateDatabase(DATABASE_PATH + DATABASE_NAME,null,null);

        db.execSQL("CREATE TABLE IF NOT EXISTS localsave(scheduleId int NOT NULL" +
                "crn int NOT NULL," +
                "number varchar (100) NOT NULL," +
                "title varchar (500) NOT NULL," +
                "units int NOT NULL," +
                "activity varchar (60) NOT NULL," +
                "days varchar (20) NOT NULL," +
                "time varchar (20) NOT NULL," +
                "room varchar (20) NOT NULL," +
                "length varchar (20)," +
                "instructor varchar (40)," +
                "maxEnrl int," +
                "seatsAvailable int," +
                "activeEnrl int," +
                "sem_id int  NOT NULL);");

        db.close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    public boolean isTableExtant(String tableName) {

        mDatabase = SQLiteDatabase.openDatabase(DATABASE_PATH+DATABASE_NAME,null,SQLiteDatabase.OPEN_READONLY);
        boolean openDb = mDatabase.isOpen();

        if(openDb) {
            if(mDatabase == null || !mDatabase.isOpen()) {
                mDatabase = getReadableDatabase();
            }

            if(!mDatabase.isReadOnly()) {
                mDatabase.close();
                mDatabase = getReadableDatabase();
            }
        }

        Cursor cursor = mDatabase.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {//table does exist
                cursor.close();
                mDatabase.close();
                return true;
            }
            cursor.close();
        }
        mDatabase.close();
        return false;
    }

    public void DROPCOURSES(){
        SQLiteDatabase db=null;
        try{
            String query = "DROP TABLE courses";
            db = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            db.rawQuery(query, null);
        }
        catch(Exception e) {
            e.printStackTrace();
        }finally{
            db.close();
        }
    }

    public ArrayList<courses> courseSearchByDepartment(String dep){
        Cursor res =null;
        SQLiteDatabase db=null;
        ArrayList<courses> result= new ArrayList<courses>();
        try{
            Log.d("SELECTED", dep);
            String query = "SELECT * FROM courses WHERE number like '%" + dep + "%' order by number";
            Log.d("SELECTED",query);
            db = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READONLY);

            res = db.rawQuery(query, null);
            while(res.moveToNext()) {
                if(res.getString(2).equals("TBD-TBD") || res.getString(6).equals("?") || res.getString(1).charAt(dep.length()) != '-'){
                    continue;
                }
                result.add(new courses());
                result.get(result.size()-1).setId(res.getString(0));
                result.get(result.size()-1).setNumber(res.getString(1));
                result.get(result.size()-1).setTime(res.getString(2));
                result.get(result.size()-1).setActivity(res.getString(3));
                result.get(result.size()-1).setUnits(Integer.parseInt(res.getString(4)));
                result.get(result.size()-1).setRoom(res.getString(5));
                result.get(result.size()-1).setDays(res.getString(6));
                result.get(result.size()-1).setTitle(res.getString(7));
                result.get(result.size()-1).setLength(res.getString(8));
                result.get(result.size()-1).setActiveEnrl(Integer.parseInt(res.getString(9)));
                result.get(result.size()-1).setMaxEnrl(Integer.parseInt(res.getString(10)));
                result.get(result.size()-1).setSeatsAvailable(Integer.parseInt(res.getString(11)));
                result.get(result.size()-1).setSem_id(Integer.parseInt(res.getString(12)));
                result.get(result.size()-1).setInstruction(res.getString(13));
                result.get(result.size()-1).setCrn(Integer.parseInt(res.getString(14)));

                Log.d("DATABASE QUERY", res.getString(1) + "::\n");
            }
        }
        catch(Exception e) {
            Log.d("DB", e.getMessage());
        }finally{
            db.close();
            res.close();
        }
        return result;
    }

}
