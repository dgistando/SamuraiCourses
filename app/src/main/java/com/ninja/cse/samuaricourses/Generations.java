package com.ninja.cse.samuaricourses;

import android.util.Log;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
        scheduleSize = this.getIntent().getIntExtra("ScheduleSize", 0);
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
        List<WeekViewEvent> finalEvents = new ArrayList<WeekViewEvent>();

        for(int i=0; i<scheduleSize; i++){
            listOfCourses.addAll((ArrayList) this.getIntent().getParcelableArrayListExtra("schedule: " + i));
        }

        Log.d("SCHEDULESIZE: ", Integer.toString(scheduleSize));
        for(int i=0; i<listOfCourses.size(); i++){
            for(int j=0; j < scheduleSize; j++)
                Log.d("RESULTS: ", listOfCourses.get(i).getNumber() + "");
            Log.d("RESULTS: ", "--------------------------");
        }

        int numTotalEvents = 0;
        for(int i=0; i < listOfCourses.size(); i++){
            ;
            numTotalEvents = numTotalEvents + listOfCourses.get(i).getDays().length();

        }

        int scheduleCounter = 0;
        int eventCounter = 0;
        for (int i = 0; i < listOfCourses.size(); i++) {

            for (int k = 0; k < scheduleSize+1; k++) {

                for (int d = 0; d < listOfCourses.get(i).getDays().length(); d++) {

                    Calendar startTime = Calendar.getInstance();
                    startTime.set(Calendar.MONTH, 7);
                    startTime.set(Calendar.DAY_OF_MONTH, 22);
                    startTime.set(Calendar.YEAR, 2016);
                    startTime.add(Calendar.DATE, 7 * scheduleCounter);
                    startTime.set(Calendar.HOUR_OF_DAY, listOfCourses.get(i).getStartTime() / 100);
                    startTime.set(Calendar.MINUTE, listOfCourses.get(i).getStartTime() % 100);

                    Calendar endTime = (Calendar) startTime.clone();
                    endTime.set(Calendar.HOUR_OF_DAY, listOfCourses.get(i).getEndTime() / 100);
                    endTime.set(Calendar.MINUTE, listOfCourses.get(i).getEndTime() % 100);

                    switch (listOfCourses.get(i).getDays().charAt(d)) {
                        case 'M':
                            startTime.add(Calendar.DATE, 0);
                            endTime.add(Calendar.DATE, 0);
                            break;
                        case 'T':
                            startTime.add(Calendar.DATE, 1);
                            endTime.add(Calendar.DATE, 1);
                            break;
                        case 'W':
                            startTime.add(Calendar.DATE, 2);
                            endTime.add(Calendar.DATE, 2);
                            break;
                        case 'R':
                            startTime.add(Calendar.DATE, 3);
                            endTime.add(Calendar.DATE, 3);
                            break;
                        case 'F':
                            startTime.add(Calendar.DATE, 4);
                            endTime.add(Calendar.DATE, 4);
                            break;
                    }

                    WeekViewEvent event = new WeekViewEvent(eventCounter,
                            listOfCourses.get(i).getCrn() + "\n"
                                    + listOfCourses.get(i).getNumber() + "\n"
                                    + listOfCourses.get(i).getTime() + "\n"
                                    + listOfCourses.get(i).getRoom() + "\n",
                            listOfCourses.get(i).getNumber(),
                            startTime, endTime);
                    event.setColor(getResources().getColor(listOfCourses.get(i).getColor()));
                    events.add(event);

                    eventCounter = eventCounter + 1;
                }

                i = i+1;
            }
            scheduleCounter = scheduleCounter + 1;
            i = i - 1;
        }

        for(int i=0; i< events.size(); i++){
            if(events.get(i).getStartTime().get(Calendar.MONTH) == newMonth)
                finalEvents.add(events.get(i));
        }
        return finalEvents;
    }
}