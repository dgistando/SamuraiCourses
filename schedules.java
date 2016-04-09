import java.io.*;
import java.util.ArrayList;

class schedule{
    ArrayList<ArrayList<CRN>> week = new ArrayList<ArrayList<CRN>>();
    
    schedule(){
        int daysPerWeek = 7;
        
        for(int i = 0;i < daysPerWeek; i++){
            ArrayList<CRN> temp = new ArrayList<CRN>();

            /*Convert Strings from AM-PM time to 24-hour ex:HH:MM time for SQL.
              add a method in this class to do that, then pass.
              access string like: System.out.print(mine.week.get(0).get(0));*/

            temp.add("the string");
            
            week.add(temp);
        }
        
    }
}