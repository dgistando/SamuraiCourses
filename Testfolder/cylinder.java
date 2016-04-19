import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;


public class cylinder {
	
	
public static class schedule{
    courses LECT;
    ArrayList<courses> DISC,LAB,OTHER;
    int count;
    static int counter=0;
	
    schedule(courses _lect){
        LECT = _lect;
        DISC = new ArrayList<courses>();
        LAB = new ArrayList<courses>();
        OTHER = new ArrayList<courses>();
        counter++;
        count = counter;
    }
    
    schedule(){
    	
    }
    
    public void insert(courses temp){
    	if(temp.getActivity().equals("DISC")){
    		DISC.add(temp);
    	}else if(temp.getActivity().equals("LAB")){
    		LAB.add(temp);
    	}else{
    		OTHER.add(temp);
    	}
    }
    
    public boolean check(){
    	
    	return false;
    }
    
    public void generate(){    	

    	
    	//for(int i=0; i < courseList.size(); i++){
    		
    	//}
    	
    	//courses[] arr = courseList.toArray(new courses[courseList.size()]);
    	
    	//week.addAll(permute(arr,1));
    	
    	//return check();
    }
    
    public ArrayList<ArrayList<courses>> permute(courses a[],int k){
    	ArrayList<ArrayList<courses>> lista = new ArrayList<ArrayList<courses>>();
    	
    	
        if (k == a.length) 
        {
        	lista.add(new ArrayList<courses>());
            for (int i = 0; i < a.length; i++) 
            {
            	//add to list of courses
            	lista.get(lista.size()-1).add(a[i]);
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
    
    public void setCount(int i){
    	counter=i;
    }
    
    public int getCount(){
    	return count;
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



public static boolean testSchdeule(ArrayList<courses> list){
	
	for(int i=0;i<list.size();i++){
		for(int j=i+1;j<list.size();j++){
			
			if(list.get(i).conflicts(list.get(j))){
				System.out.println("CONFLICTS");
				return false;
			}
			
		}
	}
	
	System.out.println("PASS AND VALID");
	return true;
}

public static ArrayList<schedule> Kcombinations(schedule[] sequence, int n){
	ArrayList<schedule> list = new ArrayList<schedule>();
	int N = sequence.length,ct = 1;
	String str = "";
	schedule[] temp = new schedule[N];
	 //param int[] sequence, 
    //N = arr size.
    //n = num elements in subsets or number of different classes
    int[] binary = new int[(int) Math.pow(2, N)];
    for (int i = 0; i < Math.pow(2, N); i++) 
    {
        int b = 1;
        binary[i] = 0;
        int num = i, count = 0;
        while (num > 0) 
        {
            if (num % 2 == 1)
                count++;
            binary[i] += (num % 2) * b;
            num /= 2;
            b = b * 10;
        }
        if (count == n) 
        {
        	ct=0;
            System.out.print("{ ");
            for (int j = 0; j < N; j++) 
            {
                if (binary[i] % 10 == 1)
                {
                    if(!sequence[j].LECT.getNumber().substring(0, sequence[j].LECT.getNumber().indexOf('-')+4).equals(str)){
                    	ct++;
                    }
                	
                    
                    
                	str = sequence[j].LECT.getNumber().substring(0, sequence[j].LECT.getNumber().indexOf('-')+4);
                    System.out.print(sequence[j].LECT.getNumber() + " ");
                    
                }
                binary[i] /= 10;
            }
            System.out.println("}");
            
            if(ct != n){
            	System.out.print(ct +" ::Rejected:: \n");
            	
            }else{
            	list.addAll(Arrays.asList(temp));
            }
        }
    }
	
	return list;
}

public static void main(String[] args) throws FileNotFoundException{
	
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
				//System.out.println("test: "+entity.getNumber());
				Lectures.add(new courses(entity));//added
				Lectures.get(Lectures.size()-1).setNumber(s);//edit entry
				
				//entity.setNumber(s);
				//Lectures.add(entity);
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
				if(!Lectures.get(i).getNumber().equals(Lectures.get(j).getNumber()) && Title.equals("-sentinel")){
					Title = Lectures.get(j).getNumber();
				}
				
				if(Lectures.get(j).getNumber().equals(Title) && !Lectures.get(j).conflicts(Lectures.get(i)) && deleteIt){
					which = j;
				}
				
				if(Lectures.get(j).getNumber().equals(Title) && Lectures.get(j).conflicts(Lectures.get(i)) && !deleteIt){
					deleteIt = true;
					which = i;
				}
			}
			
			if(deleteIt){
				Lectures.remove(which);
			}
		}

		System.out.println();
		
		//checking to see if same classes are there
		String classNumbers = Lectures.get(0).getNumber();
		int ClassesAfter = 1;
		
		for(int i=0;i<Lectures.size();i++){
			if(!classNumbers.equals(Lectures.get(i).getNumber())){
				ClassesAfter++;
			}
			
			classNumbers = Lectures.get(i).getNumber();
			System.out.println(Lectures.get(i).getCrn() +" "+ Lectures.get(i).getNumber()+" "+Lectures.get(i).getStartTime()+" "+ClassesAfter);
		}
		
		if(numDiffClasses != ClassesAfter){
			System.out.println("THIS DOESN'T PASS");
			return;
		}
		
		System.out.println();
		
		 
		//This is where you should add to the new finalList.
		ArrayList<courses> finalList = new ArrayList<courses>();
		
		/**
		 * if lecture and in Lectures list then its okay to add everything
		 * after until you reach a lecture that's not in the list. if the lecture isn't in
		 * list then its not okay to add everything after.
		 */
		boolean okay = false;
		for(courses course : coursesList){
			
			//System.out.println("\n: "+course.getNumber());
			
			if(course.getActivity().equals("LECT")){
				
				for(int i=0;i<Lectures.size();i++){
					
					//System.out.println("comparing: "+course.getCrn()+" and "+Lectures.get(i).getCrn());
					if(course.getCrn() == Lectures.get(i).getCrn()){
						okay=true;
						break;
					}else{
						okay=false;
					}
				}
				//okay = (Lectures.contains(course)) ? true : false;
			}
			
			if(okay){
				finalList.add(course);
				//System.out.println(course.getCrn() +" "+course.getNumber());
			}
		}
		
		
		for(int i=0;i<finalList.size();i++){
			System.out.println(finalList.get(i).getCrn() +" "+finalList.get(i).getNumber());
		}
		
		String str="";
		ArrayList<ArrayList<courses>> Valid = new ArrayList<ArrayList<courses>>();
		ArrayList<schedule> Scheduler = new ArrayList<schedule>();
		schedule c = new schedule();
		
		for(courses entity: finalList){
			
			if(entity.getActivity().equals("LECT")){
				
				if(!entity.getNumber().substring(0, entity.getNumber().indexOf('-')+4).equals(str)){
					c.setCount(0);
				}
				
				str = entity.getNumber().substring(0, entity.getNumber().indexOf('-')+4);
				okay = true;
				
				Scheduler.add(new schedule(entity));
				continue;
			}
			
			if(okay){
				Scheduler.get(Scheduler.size()-1).insert(entity);
			}
		}
		
		Kcombinations(Scheduler.toArray(new schedule[Scheduler.size()]),numDiffClasses);
		
		/*for(int i=0;i<Scheduler.size();i++){
			//stop after first class
			if(i!=0 && Scheduler.get(i).count == 1){
				break;
			}
			
			ArrayList<courses> temp  = new ArrayList<courses>();
			System.out.println(Scheduler.get(i).LECT.getNumber()+" count:"+ Scheduler.get(i).getCount());
			
			//Add Lectures
			for(int j=i+1;j<Scheduler.size();j++){
				temp.add(Scheduler.get(i).LECT);
				
				if(Scheduler.get(j).count > i){
					continue;
				}else{
					temp.add(Scheduler.get(j).LECT);
				}
				
			}
			
		}*/
		
		
	}
}