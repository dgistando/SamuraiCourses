package com.ninja.cse.samuaricourses;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A basic example of how to use week view library.
 * Created by Raquib-ul-Alam Kanak on 1/3/2014.
 * Website: http://alamkanak.github.io
 */
public class Generations extends BaseGenerations {

    int j=0;
    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        ArrayList<courses> listOfCourses = new ArrayList<courses>();
        int j = this.getIntent().getIntExtra("ScheduleSize", 0);

        for(int i=0;i<j;i++){
            listOfCourses.addAll((ArrayList) this.getIntent().getParcelableArrayListExtra("schedule: " + i));
            Log.d("RESULTS: ", listOfCourses.get(i).getNumber() + "");
        }

        // Populate the week view with some events.
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.DAY_OF_MONTH, 22);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, 6);//set month one month before the set month of august
        startTime.add(Calendar.MONTH,1);//adding one month to July, makes August
        startTime.add(Calendar.WEEK_OF_YEAR,5);//now you can increment by weeks, number means, + or - week
        startTime.set(Calendar.YEAR, newYear);
        Calendar endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR, 1);
        WeekViewEvent event = new WeekViewEvent(0,
                "ANTH-005-02L\n" +
                "Biological Anthropology\n" +
                "8:00-8:50am\n" +
                "SSM 107\n" +
                "Staff",
                startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_01));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.DAY_OF_MONTH, 30);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, 7);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR, 4);
        event = new WeekViewEvent(0, "Class02", startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_01));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 30);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.HOUR_OF_DAY, 4);
        endTime.set(Calendar.MINUTE, 30);
        endTime.set(Calendar.MONTH, newMonth-1);
        event = new WeekViewEvent(1, "another blah", startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_02));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 4);
        startTime.set(Calendar.MINUTE, 20);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.HOUR_OF_DAY, 5);
        endTime.set(Calendar.MINUTE, 0);
        event = new WeekViewEvent(2, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_03));
        events.add(event);

        return events;
    }

}
