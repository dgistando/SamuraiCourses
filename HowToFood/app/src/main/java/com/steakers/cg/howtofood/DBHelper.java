package com.steakers.cg.howtofood;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by dgist on 12/9/2015.
 */
public class DBHelper extends SQLiteOpenHelper{
    static final String DATABASE_NAME = "recipe.db";
    static final String DATABASE_PATH = "/data/data/com.steakers.cg.howtofood/";
    /*public static final String TABLE_RECIPE = "recipe";
    public static final String TABLE_LIST = "list";
    public static final String R_NAME = "r_name";
    public static final String R_DIRECTIONS = "r_directions";
    public static final String R_RATING = "r_rating";
    public static final String R_CATEGORY = "r_category";
    public static final String R_YIELD = "r_yield";
    public static final String R_DIFFICULTY = "r_difficulty";*/

    SQLiteDatabase myDatabase;
    private final Context myContext;

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
        this.myContext = context;
    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDatabase();
        if(dbExist){
            //does nothing
        }
        else{
            this.getReadableDatabase();
            try{
                copyDataBase();
            }catch(IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDatabase(){
        SQLiteDatabase checkDB = null;
        try{
            String myPath = DATABASE_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READONLY);
        }catch(SQLiteException e){
            //database dne
        }
        if(checkDB != null){
            checkDB.close();
        }
        return checkDB != null ? true :false;
    }

    private void copyDataBase() throws IOException{

        InputStream myInput = myContext.getAssets().open("asset_recipe.db");
        String outFileName = DATABASE_PATH+DATABASE_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int size;

        while((size = myInput.read(buffer))>0){
            myOutput.write(buffer,0,size);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDatabase() throws SQLException {
        String myPath = DATABASE_PATH+DATABASE_NAME;
        myDatabase = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READWRITE);
    }

    public  ArrayList<String> getRecipeK(String RK_NAME){

        Cursor res=null;
        SQLiteDatabase db=null;

        ArrayList<String> result= new ArrayList<String>();
        try{
            String query = "SELECT r_name FROM recipe where r_name like '%" + RK_NAME + "%' ";
            db = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            res = db.rawQuery(query, null);
            while(res.moveToNext()) {
                result.add(res.getString(0));
            }


        }
        catch(Exception e) {
            Log.d("DB", e.getMessage());
        }finally {
            db.close();
            res.close();
        }
        return result;
    }

    public String getRecipeD(String RD_NAME){

        String result = " ";
        try{
            String query = "SELECT r_directions FROM recipe WHERE r_name ='" + RD_NAME + "';";
            SQLiteDatabase db = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            Cursor res = db.rawQuery(query, null);
            while(res.moveToNext()) {
                result =  res.getString(0) + "\n";
            }
            db.close();
        }
        catch(Exception e) {
            Log.d("DB", e.getMessage());
        }
        return result;
    }

    public ArrayList<String> getRecipeI(String RI_NAME){

        ArrayList<String> result = new ArrayList<String>();
        try{
            String query = "SELECT i_quantity,i_measure,i_item FROM ingredient WHERE i_rname = '" + RI_NAME + "';";
            SQLiteDatabase db = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME,null,SQLiteDatabase.OPEN_READWRITE);
            Cursor res = db.rawQuery(query,null);
            while(res.moveToNext()){
                result.add(res.getString(0));
                result.add(" " + res.getString(1) + " " + res.getString(2) + "\n");
            }

            db.close();
        }catch(Exception e){
            Log.d("DB",e.getMessage());
        }
        return result;
    }

    public boolean inuname(String fname,String lname,String uname,String pw){
        Cursor res = null;
        SQLiteDatabase db = null;
        try{
            String query = "SELECT u_uname FROM user WHERE u_uname ='"+uname+"';";
            db = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            res = db.rawQuery(query, null);
            while(res.moveToNext()) {
                if (res.getString(0).equals(uname)) {
                    return false;
                }
            }

            db.execSQL("INSERT INTO user(u_id,u_fname,u_lname,u_uname,u_pw) " +
                    "VALUES(0,'" + fname + "','" + lname + "','" + uname + "','" + pw + "');");

        }
        catch(Exception e) {
            Log.d("DB", e.getMessage());
        }finally {
            db.close();
            res.close();
        }
        return true;
    }

    public  ArrayList<String> getRecipeY(String RY_NAME){

        ArrayList<String> result= new ArrayList<String>();
        try{
            String query = "SELECT r_name FROM recipe WHERE r_yield = '"+RY_NAME+"';";
            SQLiteDatabase db = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            Cursor res = db.rawQuery(query, null);
            while(res.moveToNext()) {
                result.add(res.getString(0));
            }
            db.close();
        }
        catch(Exception e) {
            Log.d("DB", e.getMessage());
        }
        return result;
    }

    public  ArrayList<String> getRecipeRT(){

        ArrayList<String> result= new ArrayList<String>();
        try{
            String query = "SELECT r_name,r_rating FROM recipe ORDER BY r_rating DESC";
            SQLiteDatabase db = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            Cursor res = db.rawQuery(query, null);
            while(res.moveToNext()) {
                result.add(res.getString(0) + "\n\nRating: "+ res.getString(1)+ "\n");
            }
            db.close();
        }
        catch(Exception e) {
            Log.d("DB", e.getMessage());
        }
        return result;
    }

    public  ArrayList<String> getRecipeSub(String RI_NAME){
        SQLiteDatabase db=null;
        Cursor res= null;
        ArrayList<String> result= new ArrayList<String>();
        try{
            String query = "SELECT s_name FROM substitutes where s_item ='" + RI_NAME+ "';";
            db = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            res = db.rawQuery(query, null);
            while(res.moveToNext()) {
                result.add("Substitute:" + res.getString(0) + "\n");
            }
        }
        catch(Exception e) {
            Log.d("DB", e.getMessage());
        }finally {
            db.close();
            res.close();
        }
        return result;
    }

    public  ArrayList<String> getRecipeR(){
        //Average rating of a recipe
        ArrayList<String> result= new ArrayList<String>();
        try{
            String query = "SELECT ru_name,avg(ru_rating) FROM recuser GROUP BY ru_name";
            SQLiteDatabase db = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            Cursor res = db.rawQuery(query, null);
            while(res.moveToNext()) {
                result.add("Recipe:" + res.getString(0) + "Average Rating: "+res.getString(1)+ "\n");
            }
            db.close();
        }
        catch(Exception e) {
            Log.d("DB", e.getMessage());
        }
        return result;
    }

    public String[] getPassword(String R_uname,String R_pass){
        String[] result = new String[4];
        try{
            String query = "SELECT u_pw,u_id,u_fname,u_lname FROM user WHERE u_pw ='" + R_pass + "' AND u_uname = '"+R_uname+"';";
            SQLiteDatabase db = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            Cursor res = db.rawQuery(query, null);
            while(res.moveToNext()) {
                result[0] =  res.getString(0);
                result[1] = res.getString(1);
                result[2] = (res.getString(2) + " " +res.getString(3));
            }

            db.close();
            if(result[0].equals(R_pass)){
                result[3] = "true";
            }else{
                result[3] = "false";
            }

        }
        catch(Exception e) {
            Log.d("DB", e.getMessage());
        }
        return result;
    }

    public  ArrayList<String> getRecipeIng(String query){
        Cursor res =null;
        SQLiteDatabase db =null;
        ArrayList<String> result= new ArrayList<String>();
        /*String [] ingr_arr=ingr.split(",");
        int ingrcount =ingr_arr.length;

        // if(ingrcount==1||ingrcount==0){
        //   result.add("Need more than 1 ingredient!");
        //  return result;
        //}*/
        try {
            /*StringBuilder sb = new StringBuilder();
            sb.append("SELECT DISTINCT i_rname FROM ingredient WHERE i_item like '" + ingr_arr[0] + "%'");
            for (int i = 0; i < ingrcount - 1; i++) {
                sb.append(" INTERSECT SELECT DISTINCT i_rname FROM ingredient WHERE i_item like '" + ingr_arr[i + 1] + "%'");
            }*/
            /*String[] arr;
            StringBuffer buffer= new StringBuffer();

            String a = "intersect select distinct i_rname from ingredient where i_item like ";

            buffer.append("select distinct i_rname from ingredient where i_item like '"+ingr_arr[0]+"%' ");
            for(int i=1;i< ingr_arr.length;i++){
                buffer.append(a+"'"+ingr_arr[i]+"%'");
            }*/


            //String query = sb.toString();
            //String query = "SELECT DISTINCT i_rname FROM ingredient WHERE i_item like 'flour%' INTERSECT SELECT DISTINCT i_rname FROM ingredient WHERE i_item like 'white%'";
            db = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            res = db.rawQuery(query, null);
            while (res.moveToNext()) {
                result.add(res.getString(0));
            }
        } catch (Exception e) {
            Log.d("DB", e.getMessage());
        }finally{
            db.close();
            res.close();
        }
        return result;


    }

    public  ArrayList<String> getRecipebytype(String category ){
        Cursor res =null;
        SQLiteDatabase db=null;
        ArrayList<String> result= new ArrayList<String>();
        try{
            String query = "SELECT r_name FROM recipe WHERE r_category='" + category + "'";
            db = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            res = db.rawQuery(query, null);
            while(res.moveToNext()) {
                result.add(res.getString(0));
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

    public  ArrayList<String> getEasyRecipes(){
        Cursor res =null;
        SQLiteDatabase db=null;
        ArrayList<String> result= new ArrayList<String>();
        try{
            String query = "SELECT r_name FROM recipe WHERE r_difficulty=1";
            db = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            res = db.rawQuery(query, null);
            while(res.moveToNext()) {
                result.add(res.getString(0));
            }
        }
        catch(Exception e) {
            Log.d("DB", e.getMessage());
        }
        finally{
            db.close();
            res.close();
        }
        return result;
    }

    @Override
    public void onCreate(SQLiteDatabase db){

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }


}
