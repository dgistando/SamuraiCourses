import java.util.*;

class setOfLectures {
    public static void main(String[] args) {
        ArrayList<String> lect = new ArrayList<String>();
        ArrayList<ArrayList<String>> setsOfLectures = new ArrayList<ArrayList<String>>();
        
        lect.add("MATH-021-01");
        lect.add("MATH-021-02");
        lect.add("CSE-021-01");
        lect.add("SPAN-001-02");
        lect.add("SPAN-001-02");
        Map <String,Integer> countClasses = new HashMap<String,Integer>();
       // countClasses
       
        //System.out.println(countClasses.get(lect.get(0).substring(0,8)));
        for(int i=0;i<lect.size(); i++){
            if(lect.get(i).indexOf('-')==4){//&& lect.get(i).substring(0,8)!=lect.get(i+1).substring(0,8)){
                if(countClasses.get(lect.get(i).substring(0,8))==null){
                    int count = countClasses.get(lect.get(i).substring(0,8));
                    countClasses.put(lect.get(i).substring(0,8),count++);
                }
                else{
                    countClasses.put(lect.get(i).substring(0,8), 1);
                }
            }
            else if(lect.get(i).indexOf('-')==3){  // && lect.get(i).substring(0,7)!=lect.get(i+1).substring(0,7)){
                if(countClasses.get(lect.get(0).substring(0,7))==null){
                    int count = countClasses.get(lect.get(i).substring(0,7));
                    countClasses.put(lect.get(i).substring(0,7),count++);
                }
                else{
                    countClasses.put(lect.get(i).substring(0,7), 1);
                }
            }
        }
        System.out.println(countClasses.get("MATH"));
       // for(int j=0;)
        //System.out.println(lect.get(0).substring(0,8));
        //setsOfLectures.get(0).add(lect.get(0));
        //System.out.println(setsOfLectures.get(0));
        /*for(int i=0;i<lect.size();i++){
         for(int j=0;j<lect.size();j++){
         if(lect.get(i+1)==null){
         break;
         }
         if(lect.get(i).indexOf('-')==4 && lect.get(i).substring(0,8)!=lect.get(i+1).substring(0,8)){
         setOfLectures.add(lect.get(i));
         }	
         else if(lect.get(i).indexOf('-')==3 && lect.get(i).substring(0,7)!=lect.get(i+1).substring(0,7)){
         setOfLectures.add(lect.get(i));
         
         }
         }
         
         //System.out.println(lect.get(i) + " Found first '-' at index:" + lect.get(i).indexOf('-'));
         
         
         }*/
        
    }
}