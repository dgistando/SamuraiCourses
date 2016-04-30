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

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {


        ArrayList<courses> listOfCourses = new ArrayList<courses>();
        int numOfSchedules = this.getIntent().getIntExtra("ScheduleSize", 0);
        //scheduleSize = this.getIntent().getIntExtra("ScheduleSize", 0);
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

        Calendar startTime;// = Calendar.getInstance();
        Calendar endTime;// = (Calendar) startTime.clone();
        for (int i = 0; i < numOfSchedules; i++) {
            listOfCourses.clear();
            listOfCourses.addAll((ArrayList) this.getIntent().getParcelableArrayListExtra("schedule: " + i));
            for (int k = 0; k < listOfCourses.size(); k++) {

                for (int d = 0; d < listOfCourses.get(k).getDays().length(); d++) {
                    startTime=Calendar.getInstance();
                    startTime.set(Calendar.HOUR_OF_DAY, listOfCourses.get(k).getStartTime() / 100);
                    startTime.set(Calendar.DAY_OF_MONTH, 22);
                    startTime.set(Calendar.MINUTE, listOfCourses.get(k).getStartTime() % 100);
                    startTime.set(Calendar.MONTH, 6);


                    //original:6,set month one month before the set month of august
                    startTime.add(Calendar.MONTH, 1);//original:1,adding one month to July, makes August

                    //if(((newMonth-2)+1)==8) {
                    //Log.d("Schedule", i + "");
                    //Log.d("Months 8!", (newMonth - 2) + 1 + "");
                    //Log.d("Course Name: ", listOfCourses.get(k).getNumber() + "");
                    //Log.d("Course Days: ",listOfCourses.get(k).getDays()+"");
                    //Log.d("Day: ", listOfCourses.get(k).getDays().charAt(d) + "");
                    //}

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
                    startTime.add(Calendar.WEEK_OF_YEAR, i);//0-number of schedules,0:first week
                    startTime.set(Calendar.YEAR, newYear);
                    endTime = (Calendar) startTime.clone();
                    endTime.set(Calendar.HOUR_OF_DAY, listOfCourses.get(k).getEndTime() / 100);
                    endTime.set(Calendar.MINUTE, listOfCourses.get(k).getEndTime() % 100);
                    WeekViewEvent event = new WeekViewEvent(0, listOfCourses.get(k).getNumber() + "\n" + listOfCourses.get(k).getTime(), listOfCourses.get(k).getNumber(),
                            startTime, endTime);
                    event.setColor(getResources().getColor(listOfCourses.get(k).getColor()));
                    events.add(event);
                }
                startTime=Calendar.getInstance();
            }
            //return events;

        }
        return events;
    }
}