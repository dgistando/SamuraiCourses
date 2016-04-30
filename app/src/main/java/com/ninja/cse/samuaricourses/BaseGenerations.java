package com.ninja.cse.samuaricourses;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * This is a base activity which contains week view and all the codes necessary to initialize the
 * week view.
 * Created by Raquib-ul-Alam Kanak on 1/3/2014.
 * Website: http://alamkanak.github.io
 */
public abstract class BaseGenerations extends AppCompatActivity implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener {
    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    private WeekView mWeekView;
    Calendar testDate = Calendar.getInstance();
    Calendar weekIncr=Calendar.getInstance();
    private int scheduleIndex = 1;
    // public static int scheduleSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generations);

        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) findViewById(R.id.weekView);

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);

        // Set long press listener for empty view
        mWeekView.setEmptyViewLongPressListener(this);

        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional.
        setupDateTimeInterpreter(false);

        testDate.set(2016, 8-1, 22);
        mWeekView.goToDate(testDate);

        mWeekView.setHourHeight(90);
        mWeekView.goToHour(7);
        //mWeekView.goToToday();
        final TextView tv = (TextView)findViewById(R.id.textScheduleCounter);
        final int numOfSchedules = this.getIntent().getIntExtra("ScheduleSize", 0);

        int day=testDate.get(Calendar.DATE);
        int month=testDate.get(Calendar.MONTH);
        int year=testDate.get(Calendar.YEAR);

        tv.setText(scheduleIndex + "/" + numOfSchedules/*+": "+(month+1)+"/"+day+"/"+year*/);

        final Button btnNext = (Button)findViewById(R.id.btnNext);
        final Button btnPrev = (Button)findViewById(R.id.btnPrev);

        //PREV button is disabled when app starts
        btnPrev.setEnabled(false);

        Log.d("Number of schedule",scheduleIndex+"");
        Log.d("Size of schedules",numOfSchedules+"");
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(scheduleIndex==numOfSchedules){
                    btnNext.setEnabled(false);
                    btnPrev.setEnabled(true);
                }
                if ((scheduleIndex < numOfSchedules)) {
                    btnNext.setEnabled(true);
                    btnPrev.setEnabled(true);
                    scheduleIndex = scheduleIndex + 1;

                    testDate.add(Calendar.WEEK_OF_YEAR, 1);
                    mWeekView.goToDate(testDate);
                    tv.setText(scheduleIndex + "/" + numOfSchedules/*+": "+(1+month)+"/"+day+"/"+year*/);
                }
                else {
                    btnNext.setEnabled(false);
                    btnPrev.setEnabled(true);
                }
            }
        });


        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (scheduleIndex >1) {
                    btnNext.setEnabled(true);
                    btnPrev.setEnabled(true);

                    scheduleIndex = scheduleIndex - 1;
                    testDate.add(Calendar.WEEK_OF_YEAR, -1);

                    mWeekView.goToDate(testDate);
                    tv.setText(scheduleIndex + "/" + numOfSchedules/*+": "+(1+month)+"/"+day+"/"+year*/);
                }

                else {
                    btnPrev.setEnabled(false);
                    btnNext.setEnabled(true);
                }
//                testDate.add(Calendar.DATE, -7);
//                mWeekView.goToDate(testDate);
            }
        });
    }
    /**
     * Set up a date time interpreter which will show short date values when in week view and long
     * date values otherwise.
     * @param shortDate True if the date values should be short.
     */
    private void setupDateTimeInterpreter(final boolean shortDate) {

        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
            }
        });
    }

    protected String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(this, event.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(this, "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {
        Toast.makeText(this, "Empty view long pressed: " + getEventTitle(time), Toast.LENGTH_SHORT).show();
    }

    public WeekView getWeekView() {
        return mWeekView;
    }
}
