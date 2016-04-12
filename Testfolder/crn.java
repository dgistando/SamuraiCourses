import java.io.*;
import java.util.ArrayList;

class CRN{
    String room, days, courseNum, courseTitle, actv, instructor, dates;
    int crn, startTime, endTime, units;
    
    CRN(
        int _crn,
        String _courseNum,
        String _courseTitle,
        int _units,
        String _actv,
        String _days,
        String _time,
        String _room,
        String _dates,
        String _instructor){
            
        crn = _crn;
        courseNum = _courseNum;
        courseTitle = _courseTitle;
        units = _units;
        actv = _actv;
        days = _days;
        startTime = setStartTime(_time);
        endTime = setEndTime(_time);
        room = _room;
        dates = _dates;
        instructor = _instructor;
    }
    
    private int setStartTime(String time){
        //	10:00-12:50pm(example time)
      	int Start_Time = 0;
        String meridian = time.substring(time.length()-2,time.length());
        String endtime = time.substring(time.indexOf("-")+1,time.length()-2);
        String starttime = time.substring(0,time.indexOf("-"));
        
         if(timeToInt(starttime,meridian) > timeToInt(endtime,meridian))
         {
            if(meridian.equals("pm")){
                Start_Time = timeToInt(starttime,"am");
            }else{
                Start_Time = timeToInt(starttime,"pm");
            }
	    }
        
        return Start_Time;
    }
    
    private int setEndTime(String time){
        String  meridian = time.substring(time.length()-2,time.length());
        String endtime = time.substring(time.indexOf("-")+1,time.length()-2);
        
        return timeToInt(endtime, meridian);
    }
    
    public int getStartTime(){
        return this.startTime;
    }
    
    public int getEndTime(){
        return this.endTime;
    }
    
    private int timeToInt(String time, String meridian){
        time = time.replaceAll(":","");
        
        int int_time =  Integer.parseInt(time);
        
        if(meridian.equals("pm") && int_time < 1200){
            return int_time+1200;
        }else if (meridian.equals("am") && (int_time >= 1200 && int_time <= 1300)){
             return int_time-1200;
        }else{
            return int_time;
        }
    }
    
    
    
}