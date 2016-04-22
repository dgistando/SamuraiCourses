package com.ninja.cse.samuaricourses;

/**
 * Created by dgist on 4/5/2016.
 */
public class courses {
    /**
    * Item id
    * Item crn
    * Item number
    * Item title
    * Item units
    * Item activity
    * Item days
    * Item time
    * Item room
    * Item length
    * Item instruction
    * Item maxEnrl
    * Item seatsAvailable
    * Item activeEnrl
    * Item sem_id
    */
    @com.google.gson.annotations.SerializedName("id")
    private String mId;
    @com.google.gson.annotations.SerializedName("crn")
    private int crn;
    @com.google.gson.annotations.SerializedName("number")
    private String number;
    @com.google.gson.annotations.SerializedName("title")
    private String title;
    @com.google.gson.annotations.SerializedName("units")
    private int units;
    @com.google.gson.annotations.SerializedName("activity")
    private String activity;
    @com.google.gson.annotations.SerializedName("days")
    private String days;
    @com.google.gson.annotations.SerializedName("time")
    private String time;
    @com.google.gson.annotations.SerializedName("room")
    private String room;
    @com.google.gson.annotations.SerializedName("length")
    private String length;
    @com.google.gson.annotations.SerializedName("instruction")
    private String instruction;
    @com.google.gson.annotations.SerializedName("maxEnrl")
    private int maxEnrl;
    @com.google.gson.annotations.SerializedName("seatsAvailable")
    private int seatsAvailable;
    @com.google.gson.annotations.SerializedName("activeEnrl")
    private int activeEnrl;
    @com.google.gson.annotations.SerializedName("sem_id")
    private int sem_id;

    private int startTime=0, endTime=0;

    public courses(){

    }

    public courses(int mid) {
        this.setId(mId);
        this.setCrn(crn);
        this.setNumber(number);
        this.setTitle(title);
        this.setUnits(units);
        this.setActivity(activity);
        this.setDays(days);
        this.setStartTime(time);
        this.setEndTime(time);
        //this.setTime(time);
        this.setRoom(room);
        this.setLength(length);
        this.setInstruction(instruction);
        this.setMaxEnrl(maxEnrl);
        this.setSeatsAvailable(seatsAvailable);
        this.setActiveEnrl(activeEnrl);
        this.setSem_id(1);
    }

    public courses(courses temp){
        this.setId(temp.getId());
        this.setCrn(temp.getCrn());
        this.setNumber(temp.getNumber());
        this.setTitle(temp.getTitle());
        this.setUnits(temp.getUnits());
        this.setActivity(temp.getActivity());
        this.setDays(temp.getDays());
        this.startTime = temp.getStartTime();
        this.endTime = temp.getEndTime();
        //this.setTime(time);
        this.setRoom(temp.getRoom());
        this.setLength(temp.getLength());
        this.setInstruction(temp.getInstruction());
        this.setMaxEnrl(temp.getMaxEnrl());
        this.setSeatsAvailable(temp.getSeatsAvailable());
        this.setActiveEnrl(temp.getActiveEnrl());
        this.setSem_id(1);
    }

    public String getId() {
    return mId;
    }

    public int getCrn() {
    return crn;
    }

    public String getNumber() {
    return number;
    }

    public String getTitle() {
    return title;
    }

    public int getUnits() {
    return units;
    }

    public String getActivity() {
    return activity;
    }

    public String getDays() {
    return days;
    }

    public String getTime() {
    return time;
    }

    public String getRoom() {
    return room;
    }

    public String getLength() {
    return length;
    }

    public String getInstruction() {
    return instruction;
    }

    public int getMaxEnrl() {
    return maxEnrl;
    }

    public int getSeatsAvailable() {
    return seatsAvailable;
    }

    public int getActiveEnrl() {
    return activeEnrl;
    }

    public int getSem_id() {
    return sem_id;
    }

    public int getStartTime(){
        return this.startTime;
    }

    public int getEndTime(){
        return this.endTime;
    }


    public void setId(String id){mId = id;}

    public void setCrn(int crn) {
    this.crn = crn;
    }

    public void setNumber(String number) {
    this.number = number;
    }

    public void setTitle(String title) {
    this.title = title;
    }

    public void setUnits(int units) {
    this.units = units;
    }

    public void setActivity(String activity) {
    this.activity = activity;
    }

    public void setDays(String days) {
    this.days = days;
    }

    public void setTime(String time) {
    this.time = time;
    }

    public void setRoom(String room) {
    this.room = room;
    }

    public void setLength(String length) {
    this.length = length;
    }

    public void setInstruction(String instruction) {
    this.instruction = instruction;
    }

    public void setMaxEnrl(int maxEnrl) {
    this.maxEnrl = maxEnrl;
    }

    public void setSeatsAvailable(int seatsAvailable) {
    this.seatsAvailable = seatsAvailable;
    }

    public void setActiveEnrl(int activeEnrl) {
    this.activeEnrl = activeEnrl;
    }

    public void setSem_id(int sem_id) {
    this.sem_id = sem_id;
    }

    private void setStartTime(String time){
        //	10:00-12:50pm(example time)
        //int Start_Time = 0;
        String meridian = time.substring(time.length()-2,time.length());
        String endtime = time.substring(time.indexOf("-")+1,time.length()-2);
        String starttime = time.substring(0,time.indexOf("-"));

        //System.out.println(starttime+"::"+endtime+"::"+meridian);
        //System.out.println(timeToInt(starttime,meridian)+"::"+timeToInt(endtime,meridian));

        if(timeToInt(starttime,meridian) > timeToInt(endtime,meridian))
        {
            if(meridian.equals("pm")){
                this.startTime = timeToInt(starttime,"am");
            }else{
                this.startTime = timeToInt(starttime,"pm");
            }
        }
        else
        {
            this.startTime = timeToInt(starttime,meridian);
        }
    }

    private void setEndTime(String time){
        String meridian = time.substring(time.length()-2,time.length());
        String endtime = time.substring(time.indexOf("-")+1,time.length()-2);

        this.endTime =  timeToInt(endtime, meridian);
    }

    private int timeToInt(String time, String meridian){
        time = time.replaceAll(":","");

        int int_time =  Integer.parseInt(time);

        //System.out.println("test of time:"+int_time);

        if(meridian.equals("pm") && int_time < 1200){
            return int_time+1200;
        }else if (meridian.equals("am") && (int_time >= 1200 && int_time <= 1300)){
            return int_time-1200;
        }else{
            return int_time;
        }
    }
}