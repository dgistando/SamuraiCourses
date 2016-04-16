import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class cylinder {
	
public static class schedule{
	//Never used this class. I will use it to compare times once i get permutations
    ArrayList<ArrayList<courses>> week = new ArrayList<ArrayList<courses>>();    
    
    schedule(){
        int daysPerWeek = 5;
        
        for(int i = 0;i < daysPerWeek; i++){
        	week.add(new ArrayList<courses>());
        }   
    }
    
    public void addToList(courses Course){
    	
    }
    
    public boolean check(){
    	
    	return false;
    }
    
    public boolean generate(ArrayList<courses> list){
    	courses[] arr = list.toArray(new courses[list.size()]);
    	
    	week.addAll(permute(arr,0));
    	
    	return check();
    }
    
    public ArrayList<ArrayList<courses>> permute(courses a[],int k){
    	ArrayList<ArrayList<courses>> lista = new ArrayList<ArrayList<courses>>();
    	
    	
        if (k == a.length) 
        {
        	lista.add(new ArrayList<courses>());
            for (int i = 0; i < a.length; i++) 
            {
            	//add to list of courses
            	lista.get(lista.size()).add(a[i]);
                System.out.print(" [" + a[i].getCrn() + "] ");
            }
            System.out.println();
        } 
        else 
        {
            for (int i = k; i < a.length; i++) 
            {
            	//Collections.swap(a, i, k);
                courses temp = a[k];
                a[k] = a[i];
                a[i] = temp;
 
                permute(a, k + 1);
 
                //Collections.swap(a, i, k);
                temp = a[k];
                a[k] = a[i];
                a[i] = temp;
            }
        }
    return lista;
    }
    
}

public static class courses {
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
    //@com.google.gson.annotations.SerializedName("id")
    private String mId = "";
    //@com.google.gson.annotations.SerializedName("crn")
    private int crn = 0;
    //@com.google.gson.annotations.SerializedName("number")
    private String number = "";
    //@com.google.gson.annotations.SerializedName("title")
    private String title = "";
    //@com.google.gson.annotations.SerializedName("units")
    private int units = 0;
    //@com.google.gson.annotations.SerializedName("activity")
    private String activity = "";
    //@com.google.gson.annotations.SerializedName("days")
    private String days = "";
    //@com.google.gson.annotations.SerializedName("time")
    private String time = "";
    //@com.google.gson.annotations.SerializedName("room")
    private String room = "";
    //@com.google.gson.annotations.SerializedName("length")
    private String length = "";
    //@com.google.gson.annotations.SerializedName("instruction")
    private String instruction = "";
    //@com.google.gson.annotations.SerializedName("maxEnrl")
    private int maxEnrl = 0;
    //@com.google.gson.annotations.SerializedName("seatsAvailable")
    private int seatsAvailable = 0;
    //@com.google.gson.annotations.SerializedName("activeEnrl")
    private int activeEnrl = 0;
    //@com.google.gson.annotations.SerializedName("sem_id")
    private int sem_id = 0;
    
    private int startTime=0, endTime=0;

    public courses(){
    	
    }
    
    public courses(int crn) {
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

    public String getId(){return mId;}


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


    private void setStartTime(String time){
        //	10:00-12:50pm(example time)
      	int Start_Time = 0;
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

    //public void setTime(String time) {
    //    this.time = time;
    //}

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
    
    private boolean conflicts(courses temp){
    	//return false if the start time or finish time falls in
    	//in the middle of other class time
    	
    	//convert days to list and compare.
    	char[] a = getDays().toCharArray();
    	ArrayList<Character> days = new ArrayList<Character>();
    	for(char c : a){days.add(c);}
    	
    	a = temp.getDays().toCharArray();
    	ArrayList<Character> days1 = new ArrayList<Character>();
    	for(char c : a){days1.add(c);}
    	
    	days.retainAll(days1);
    	
    	if(days.size() == 0){
    		System.out.print("No days in common");
    		return false;
    	}
    	
    	if(this.startTime >= temp.startTime && this.startTime <= temp.endTime)
    	{
    		return true;
    	}
    	else if(this.endTime >= temp.startTime && this.endTime <= temp.endTime)
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
	//converts time to an integer in 24-hour time.
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

public static void main(String[] args) throws FileNotFoundException{
	
		//Master list with everything in it.
		ArrayList<courses> coursesList = new ArrayList<courses>();
		
	
	
		//courses course = new courses();
		
		File file = new File("classes.txt");
		String line = "";
		int p =0;
		Scanner input = new Scanner(file);
		System.out.println("put information for the courses");
		for(int i =0;i<23;i++){
			courses course = new courses();
	
			//System.out.println("start:");
			
			p = (int)Double.parseDouble(input.nextLine());
			//p = (int)input.nextDouble();//input.next();
			//System.out.println(p);
			course.setCrn(p);

			line = input.nextLine();
			//System.out.println("Number: "+line);
			course.setNumber(line);
			
			line = input.nextLine();
			//System.out.println(line);
			course.setTitle(line);

			p = (int)input.nextDouble();
			//System.out.println(p);
			course.setUnits(p);
			
			line = input.next();
			//System.out.println(line);
			course.setActivity(line);
			
			line = input.next();
			//System.out.println(line);
			course.setDays(line);
			
			line = input.next();
			//System.out.println(line);
			course.setStartTime(line);
			
			//input.next();
			//System.out.println(line);
			course.setEndTime(line);
						
			
			line = input.next()+input.next();
			//System.out.println("Room: "+line);
			course.setRoom(line);
			
			line = input.next()+input.next();
			//System.out.println("length: "+line);
			course.setLength(line);

			line = input.nextLine();line = input.next();
			//System.out.println(line);
			course.setInstruction(line);
		
			p = (int)input.nextDouble();
			//System.out.println(p);
			course.setMaxEnrl(p);
			
			p = (int)input.nextDouble();
			//System.out.println(p);
			course.setSeatsAvailable(p);
			
			p = (int)input.nextDouble();
			//System.out.println(p);
			course.setActiveEnrl(p);

			p = (int)input.nextDouble();
			//System.out.println(p);
			course.setSem_id(p);
			
			if(!input.hasNextLine()){
				break;
			}
			
			input.nextLine();
			
			//System.out.println("this is rediculous");
			coursesList.add(course);	
		}
		
		input.close();
		
	
		ArrayList<courses> Lectures = new ArrayList<courses>();
		int numDiffClasses = 0;
		String className = "";
		
		for(courses entity:coursesList){
			if(entity.getActivity().equals("LECT")){
				String s = entity.getNumber();
				s = s.substring(0,s.indexOf('-')+4);
				entity.setNumber(s);
				Lectures.add(entity);
				//className = s;
				
				if(!className.equals(s)){
					numDiffClasses++;
				}
				
				className = s;
				System.out.println(entity.getNumber());//+" "+entity.getStartTime()+" - "+entity.getEndTime()+" "+numDiffClasses);
			}
		}
		
		for(int i=0;i<Lectures.size();i++)
		{
			boolean deleteIt = false;
			String Title = "-sentinel";
			int which = -9999;
			
			for(int j=i+1;j<Lectures.size();j++)
			{
				
				if(Lectures.get(i).getNumber() != Lectures.get(j).getNumber() && Title.equals("-sentinel")){
					Title = Lectures.get(j).getNumber();
				}
				
				if(Lectures.get(j).getNumber() == Title && !Lectures.get(j).conflicts(Lectures.get(i)) && deleteIt){
					which = j;
				}
				
				if(Lectures.get(j).getNumber() == Title && Lectures.get(j).conflicts(Lectures.get(i)) && !deleteIt){
					deleteIt = true;
					which = i;
				}
			}
			
			if(deleteIt){
				Lectures.remove(which);
			}
		}

		System.out.println();
		
		String classNumbers = Lectures.get(0).getNumber();
		int ClassesAfter = 1;
		
		//Test to see if the new list passes.
		for(int i=1;i<Lectures.size();i++){
			if(!classNumbers.equals(Lectures.get(i).getNumber())){
				ClassesAfter++;
			}
			
			classNumbers  =Lectures.get(i).getNumber();
			System.out.println(Lectures.get(i).getNumber());//+" "+Lectures.get(i).getStartTime()+" "+ClassesAfter);
		}
		
		if(numDiffClasses != ClassesAfter){
			System.out.println("THIS DOESN'T PASS");
			return;
		}
		
		//This is where you should add to the new finalList.
		ArrayList<courses> finalList = new ArrayList<courses>();
		
		for(int i = 0; i < coursesList.size(); i++){
			for(int j =0;j<Lectures.size();j++){
				//finalList.add(j);
				n
			}
		}
		
		
		
	}
}
