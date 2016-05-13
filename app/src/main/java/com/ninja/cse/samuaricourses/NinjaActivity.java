package com.ninja.cse.samuaricourses;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.MobileServiceLocalStoreException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutionException;


public class NinjaActivity extends AppCompatActivity {

    private MobileServiceClient mClient;
    //private MobileServiceTable<courses> mCoursesTable;
    private ProgressBar mProgressBar;
    DBHelper db;
    static ArrayList<Integer> colors;

    private AutoCompleteTextView department,classes;
    private ArrayAdapter<String> departmentAdapter,classesAdapter;
    ArrayList<courses> selectedCourses = new ArrayList<courses>();
    ArrayList<courses> listToGenerateCourses = new ArrayList<courses>();
    ArrayList<String> classeslist = new ArrayList<String>();
    ArrayList<ArrayList<courses>> coursesList = new ArrayList<ArrayList<courses>>();
    /**
     * This is the current starting point for the app. It loads the
     * autocomplete textviews and the buttons.
     *
     * @param savedInstanceState saves the state of the app when moving to a new onCreate. You
     *                           can save objects in the the Bundle to be carried over to other
     *                           activities
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ninja);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        db = new DBHelper(this);
        setSupportActionBar(toolbar);
        colors = new ArrayList<Integer>();
        addColor();

        classesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, classeslist);
        classes = (AutoCompleteTextView) findViewById(R.id.number);
        classes.setThreshold(1);
        classes.setAdapter(classesAdapter);
        classes.setHint("Class");

        departmentAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.Department_array));
        department = (AutoCompleteTextView) findViewById(R.id.department);
        department.setAdapter(departmentAdapter);
        department.setThreshold(1);
        department.setHint("Department");


        department.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCourses.clear();
                Log.d("Item Clicked", "Item was clicked");
                ArrayAdapter<String> departmentTagAdapter = new ArrayAdapter<String>(NinjaActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Department_tag_array));
                ArrayAdapter<String> departmentTempAdapter = new ArrayAdapter<String>(NinjaActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Department_array));

                String chosen = parent.getItemAtPosition(position).toString();
                int newpos = departmentTempAdapter.getPosition(chosen);
                chosen = departmentTagAdapter.getItem(newpos);
                Log.w("EndCheck", chosen);

                selectedCourses.addAll(db.courseSearchByDepartment(chosen));
                Set<String> hashedset = new HashSet<>();
                String temp;
                for (courses number : selectedCourses) {
                    //gets rid of extra characters and duplicates
                    temp = number.getNumber();
                    temp = temp.substring(temp.indexOf("-") + 1);
                    temp = temp.substring(0, temp.indexOf("-"));
                    hashedset.add(temp);
                }

                classesAdapter.clear();
                for (String each : hashedset) {
                    classesAdapter.add(each.replaceFirst("^0+(?!$)", ""));
                    classesAdapter.notifyDataSetChanged();
                }
            }
        });




        Button btn = (Button)findViewById(R.id.btnGenerate);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<courses> templistToGenerate = new ArrayList<courses>();
                templistToGenerate.addAll(listToGenerateCourses);
                if(templistToGenerate.size() == 0) {
                    Toast.makeText(NinjaActivity.this, "Add courses first", Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayList<courses> forColor;
                Iterator<Integer> colorsIterator = colors.iterator();
                for(int i = templistToGenerate.size()-1; i >= 0; i--){
                    String number = templistToGenerate.get(i).getNumber();
                    templistToGenerate.remove(i);
                    forColor = new ArrayList<courses>();
                    forColor.addAll(db.courseSearchByDepartment(number.substring(0, number.indexOf('-') + 4)));
                    String previousType = "initial";
                    int C=0;
                    for(int j=0; j<forColor.size(); j++){
                        if(!previousType.equals("initial") && !previousType.equals("LECT") && colorsIterator.hasNext()){
                            previousType = forColor.get(j).getActivity();
                            C = colorsIterator.next();
                            forColor.get(j).setColor(C);
                            templistToGenerate.add(forColor.get(j));
                            continue;
                        }

                        if(forColor.get(j).getActivity().equals("LECT")  && previousType.equals("LECT")){
                            previousType = forColor.get(j).getActivity();
                            forColor.get(j).setColor(C);
                        }else if(colorsIterator.hasNext()){
                            previousType = forColor.get(j).getActivity();
                            C = colorsIterator.next();
                            forColor.get(j).setColor(C);
                        }else{
                            colorsIterator = colors.iterator();
                            forColor.get(j).setColor(colorsIterator.next());
                            previousType = forColor.get(j).getActivity();
                        }

                        templistToGenerate.add(forColor.get(j));
                    }
                }

                for(courses entity:templistToGenerate){
                    Log.d("list to generate", entity.getNumber() + "::" +entity.getCrn());
                }

                ArrayList<courses> GoodLectures = new ArrayList<courses>();

                Generator gen = new Generator();
                GoodLectures.addAll(gen.sortLectures(templistToGenerate));

                for(courses entity: GoodLectures){
                    Log.d("GoodLectures", entity.getCrn() + entity.getNumber());
                }

                if(GoodLectures.size() == 0){
                    Toast.makeText(NinjaActivity.this,"Too many conflicts",Toast.LENGTH_LONG).show();
                    return;
                }

                coursesList.addAll(gen.getFinalList(GoodLectures, templistToGenerate));
                GoodLectures.clear();

                Intent myIntent = new Intent(NinjaActivity.this,Generations.class);

                for(int i=0;i<coursesList.size();i++){
                    myIntent.putParcelableArrayListExtra("schedule: " + i, coursesList.get(i));

                    for(int j=0;j<coursesList.get(i).size();j++){
                        Log.d("GENRATIONS", i + " " + j + " " + coursesList.get(i).get(j).getCrn()+" " + " " +  coursesList.get(i).get(j).getNumber() + " " + coursesList.get(i).get(j).getDays());
                    }
                    Log.d("GERATIONS", "+++++++++++++++++++");
                }

                   myIntent.putExtra("ScheduleSize", coursesList.size());

                //myIntent.putExtra("Generated",coursesList);

                startActivity(myIntent);
                coursesList.clear();
                //startActivity(new Intent(NinjaActivity.this, Generations.class));
            }
        });


        final ListView listview = (ListView) findViewById(R.id.listViewToDo);
        final ArrayList<String> mycourses = new ArrayList<String>();
        final ArrayAdapter<String> listadapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_listitems, mycourses);
        listview.setAdapter(listadapter);
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                if(!listview.isItemChecked(position)) {
                    String num = listadapter.getItem(position).toString();
                    Log.d("Removed", listadapter.getItem(position).toString());
                    listadapter.remove(listadapter.getItem(position).toString());

                    num = num.replace(' ', '-');
                    int i = Integer.parseInt(num.substring(num.indexOf('-')+1, num.length()));
                    num = num.replaceAll(num.substring(num.indexOf('-')+1,num.length()),String.format("%03d",i));

                    for(int j = 0; j < listToGenerateCourses.size(); j++){
                        if(listToGenerateCourses.get(j).getNumber().contains(num)){
                            listToGenerateCourses.remove(j);
                        }
                    }


                    listadapter.notifyDataSetChanged();
                }
            }
        };
        listview.setOnItemClickListener(itemClickListener);


        btn = (Button)findViewById(R.id.btnAdd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(department.getText().toString().equals("") || classes.getText().toString().equals("")){
                    Toast.makeText(NinjaActivity.this," Select valid courses",Toast.LENGTH_SHORT).show();
                    return;
                }

                coursesList.clear();
                ArrayAdapter<String> departmentTagAdapter = new ArrayAdapter<String>(NinjaActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Department_tag_array));
                ArrayAdapter<String> departmentTempAdapter = new ArrayAdapter<String>(NinjaActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Department_array));

                //String chosen = "depart.";
                //int newpos = departmentTempAdapter.getPosition(department.getText().toString());
                //chosen = departmentTagAdapter.getItem(newpos);

                String validCourseNumber="-0",Selected = "-0";
                Selected = classes.getText().toString();

                for(int i = 0; i<selectedCourses.size(); i++) {
                    validCourseNumber = selectedCourses.get(i).getNumber();
                    validCourseNumber = validCourseNumber.substring(validCourseNumber.indexOf('-') + 1, validCourseNumber.indexOf('-') + 4);
                    validCourseNumber = validCourseNumber.replaceFirst("^0+(?!$)","");
                    //Log.d("compared", validCourseNumber +" compared with "+ Selected);
                    if (validCourseNumber.equals(Selected)) {

                        for(courses entity: listToGenerateCourses){
                            //if(listToGenerateCourses.contains(selectedCourses.get(i)) ){
                            if(entity.getCrn() == selectedCourses.get(i).getCrn()){
                               // Log.d("CRN CHECK: ", "selected courses.size: "+selectedCourses.size()+"   "+entity.getCrn()+entity.getNumber() +" "+ selectedCourses.get(i).getCrn()+selectedCourses.get(i).getNumber());
                                Toast.makeText(NinjaActivity.this,"Course already selected",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        listadapter.add(selectedCourses.get(i).getNumber().substring(0,selectedCourses.get(i).getNumber().indexOf('-')) + " " + validCourseNumber);
                        listview.setItemChecked(listadapter.getCount()-1, true);
                        listToGenerateCourses.add(selectedCourses.get(i));
                        listadapter.notifyDataSetChanged();
                        break;
                    }
                }
                    //listadapter.add(chosen + " " + classes.getText().toString());
                    //listview.setItemChecked(i, true);
                    //listadapter.notifyDataSetChanged();
                //Log.w("GetCheck", " " + classes.getText().toString());
                classeslist.clear();
                //for(courses entity:listToGenerateCourses){
                //    Log.d("list to generate", entity.getNumber() + "::" +entity.getCrn());
                //}
                //Log.d("list to generate", "\n");

                department.setText("");
                classes.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(NinjaActivity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

            }
        });

    }


    /**
     * This method adds an options menu to the top at the start of the app.
     *
     * @param menu functions like a list to edit the options
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ninja, menu);
        return true;
    }

    /**
     * Handle action bar item clicks here. The action bar will
     * automatically handle clicks on the Home/Up button, so long
     * as you specify a parent activity in AndroidManifest.xml.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */

    /**
     * This method performs an Asynchronous task that adds a list of course number to the
     * classes TextView.
     *
     * @param selectedDepartment String from the first dropdown list of textView ex:CSE
     */
    private void setClasseslist(final String selectedDepartment){
    //Plan to make nested class
        AsyncTask<Void,Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try{
                    Log.d("get Query", "Just before getting query");
                    final ArrayList<courses> coursesList = availableClasses(selectedDepartment);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Set<String> hashedset = new HashSet<>();
                            String temp;
                            for (courses number : coursesList) {
                                //gets rid of extra characters and duplicates
                                temp = number.getNumber();
                                temp = temp.substring(temp.indexOf("-") + 1);
                                temp = temp.substring(0, temp.indexOf("-"));
                                hashedset.add(temp);
                            }

                            classesAdapter.clear();
                            for (String each : hashedset) {
                                classesAdapter.add(each.replaceFirst("^0+(?!$)",""));
                                classesAdapter.notifyDataSetChanged();
                            }
                        }
                    });

                }catch(final Exception e){
                    e.printStackTrace();
                }
                return null;
            }

            /*@Override
            protected void onPostExecute(Void Result){
                return ;
            }*/

        };
        task.execute();
    }

    /**
     * Runs LINQ to database and retrieves list of courses that have cse in the title.
     *
     * @param item string passed from the setClasseslist method and added to the LINQ query
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private ArrayList<courses> availableClasses(String item) throws ExecutionException, InterruptedException{
           // ArrayList<courses> entities =  mCoursesTable.where().startsWith("number",item).execute().get();
        return (new ArrayList<courses>());
    }

    /**
     * Initialize local storage
     * @return
     * @throws MobileServiceLocalStoreException
     * @throws ExecutionException
     * @throws InterruptedException
     *
    private AsyncTask<Void, Void, Void> initLocalStore() throws MobileServiceLocalStoreException, ExecutionException, InterruptedException {

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    MobileServiceSyncContext syncContext = mClient.getSyncContext();

                    if (syncContext.isInitialized())
                        return null;

                    SQLiteLocalStore localStore = new SQLiteLocalStore(mClient.getContext(), "OfflineStore", null, 1);


                    Map<String, ColumnDataType> tableDefinition = new HashMap<String, ColumnDataType>();
                    tableDefinition.put("crn", ColumnDataType.Integer);
                    tableDefinition.put("number", ColumnDataType.String);
                    tableDefinition.put("title", ColumnDataType.String);
                    tableDefinition.put("units", ColumnDataType.Integer);
                    tableDefinition.put("activity", ColumnDataType.String);
                    tableDefinition.put("days", ColumnDataType.String);
                    tableDefinition.put("time", ColumnDataType.String);
                    tableDefinition.put("room", ColumnDataType.String);
                    tableDefinition.put("length", ColumnDataType.String);
                    tableDefinition.put("instruction", ColumnDataType.String);
                    tableDefinition.put("maxEnrl", ColumnDataType.Integer);
                    tableDefinition.put("seatsAvailable", ColumnDataType.Integer);
                    tableDefinition.put("activeEnrl", ColumnDataType.Integer);
                    tableDefinition.put("sem_id", ColumnDataType.Integer);

                    localStore.defineTable("courses", tableDefinition);
                    SimpleSyncHandler handler = new SimpleSyncHandler();

                    syncContext.initialize(localStore, handler).get();

                    syncContext.push().get();
                    Query query = QueryOperations.tableName("courses");
                    mCoursesTable.pull(query).get();
                    //mCoursesTable = mClient.getSyncTable(courses.class);
                } catch (final Exception e) {
                    e.printStackTrace();
                }

                return null;
            }
        };

        return runAsyncTask(task);
    }*/

    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }
    @Override
    public void onBackPressed() {
        // finish();
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
        //overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }


    private void addColor(){
        colors.add(R.color.event_color_01);
        colors.add(R.color.event_color_02);
        colors.add(R.color.event_color_03);
        colors.add(R.color.event_color_04);
        colors.add(R.color.event_color_05);
        colors.add(R.color.event_color_06);
        colors.add(R.color.event_color_07);
        colors.add(R.color.event_color_08);
        colors.add(R.color.event_color_09);
        colors.add(R.color.event_color_10);
        colors.add(R.color.event_color_11);
        colors.add(R.color.event_color_12);
        colors.add(R.color.event_color_13);
        colors.add(R.color.event_color_15);
        colors.add(R.color.event_color_16);
        colors.add(R.color.event_color_17);
        colors.add(R.color.event_color_18);
    }

    /*private AsyncTask<Void, Void, Void> sync() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    MobileServiceSyncContext syncContext = mClient.getSyncContext();
                    //syncContext.initialize();
                    syncContext.push().get();
                    Query query = QueryOperations.tableName("courses");
                    mCoursesTable.pull(query).get();
                } catch (final Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        return runAsyncTask(task);
    }*/
}
