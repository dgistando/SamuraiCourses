import java.util.*;

class setOfLectures {
	public static void main(String[] args) {
		ArrayList<String> lect = new ArrayList<String>();
		//ArrayList<ArrayList<String>> setsOfLectures = new ArrayList<ArrayList<String>>();
		
		lect.add("MATH-021-01");
		lect.add("MATH-021-02");
		lect.add("CSE-021-01");
		lect.add("SPAN-001-02");
		lect.add("SPAN-001-02");
		
		
		for(int i=0;i<lect.size();i++){
			
		System.out.println(lect.get(i) + " Found first '-' at index:" + lect.get(i).indexOf('-'));
			
		
		}
		
	}
}