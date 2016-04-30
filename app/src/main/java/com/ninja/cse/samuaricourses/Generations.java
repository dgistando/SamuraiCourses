package com.ninja.cse.samuaricourses;

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
    static boolean check = false;
    static int time=0;
    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        time++;
        ArrayList<courses> listOfCourses = new ArrayList<courses>();
        int numOfSchedules = this.getIntent().getIntExtra("ScheduleSize", 0);
        //int scheduleSize = this.getIntent().getIntExtra("ScheduleSize", 0);
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

        Calendar startTime = Calendar.getInstance();
        Calendar endTime = (Calendar) startTime.clone();
        Log.d("", "++++++++++++++++++++++++++");

        if(time%3==0) {
        for (int i = 0; i < numOfSchedules; i++) {
            listOfCourses.clear();
            listOfCourses.addAll((ArrayList) this.getIntent().getParcelableArrayListExtra("schedule: " + i));
            Log.d("++++SCHEDULE+++ ", "" + i);
            //Log.d("Size of schedukes",""+ events.size());
            for (int k = 0; k < listOfCourses.size(); k++) {

                for (int d = 0; d < listOfCourses.get(k).getDays().length(); d++) {
                    startTime = Calendar.getInstance();
                    startTime.set(Calendar.HOUR_OF_DAY, listOfCourses.get(k).getStartTime() / 100);
                    startTime.set(Calendar.DAY_OF_MONTH, 22);
                    startTime.set(Calendar.MINUTE, listOfCourses.get(k).getStartTime() % 100);
                    startTime.set(Calendar.MONTH, 6);//original:6,set month one month before the set month of august
                    startTime.add(Calendar.MONTH, 1);//original:1,adding one month to July, makes August
                    switch (listOfCourses.get(k).getDays().charAt(d)) {
                        case 'M':
                            startTime.add(Calendar.DAY_OF_MONTH, 0);
                            break;
                        case 'T':
                            startTime.add(Calendar.DAY_OF_MONTH, 1);
                            break;
                        case 'W':
                            startTime.add(Calendar.DAY_OF_MONTH, 2);
                            break;
                        case 'R':
                            startTime.add(Calendar.DAY_OF_MONTH, 3);
                            break;
                        case 'F':
                            startTime.add(Calendar.DAY_OF_MONTH, 4);
                            break;
                    }
                    startTime.add(Calendar.WEEK_OF_YEAR, i);
                    startTime.set(Calendar.YEAR, newYear);
                    int year=newYear;
                    Log.d("YEAR",""+year);
                    int month = startTime.get(Calendar.MONTH);
                    //Log.d("Schedule # ", "" + i);
                    Log.d("CRN",""+listOfCourses.get(k).getCrn());
                    //Log.d("Course", " " + listOfCourses.get(k).getCrn() + " " + listOfCourses.get(k).getNumber() + " " + listOfCourses.get(k).getDays().charAt(d));
                    //Log.d("Date m/d/y: ", "" + (month+1) + "/" + startTime.get(Calendar.DATE) + "/" + startTime.get(Calendar.YEAR));
                    endTime = (Calendar) startTime.clone();
                    endTime.set(Calendar.HOUR_OF_DAY, listOfCourses.get(k).getEndTime() / 100);
                    endTime.set(Calendar.MINUTE, listOfCourses.get(k).getEndTime() % 100);
                    WeekViewEvent event = new WeekViewEvent(0,
                            listOfCourses.get(k).getCrn() + "\n"
                            + listOfCourses.get(k).getNumber() + "\n"
                            + listOfCourses.get(k).getDays() + "\n"
                            + listOfCourses.get(k).getTime()/*(month + 1) + startTime.get(Calendar.DATE)listOfCourses.get(k).getTime()*/,
                            listOfCourses.get(k).getNumber(),
                            startTime, endTime);
                    event.setColor(getResources().getColor(listOfCourses.get(k).getColor()));
                    events.add(event);
                }
                startTime = Calendar.getInstance();
            }
        }
        return events;
    }

        //List<WeekViewEvent> eventss = new ArrayList<WeekViewEvent>();
        //Calendar starTime = Calendar.getInstance();
        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 30);
        startTime.set(Calendar.MONTH, 1);
        startTime.set(Calendar.YEAR, 1990);
        endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.HOUR_OF_DAY, 4);
        endTime.set(Calendar.MINUTE, 30);
        endTime.set(Calendar.MONTH, 1);
        WeekViewEvent event = new WeekViewEvent(0, "n", "hey",
                startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_02));
        events.add(event);
        return events;
    }
}