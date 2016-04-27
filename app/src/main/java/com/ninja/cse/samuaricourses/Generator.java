package com.ninja.cse.samuaricourses;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by dgist on 4/20/2016.
 */
public class Generator{
    courses LECT;
    ArrayList<courses> DISC,LAB,OTHER;
    int count;
    static int counter=0;
    int numDiffClasses = 0;
    Generator(){

    }

    Generator(courses _lect){
        LECT = _lect;
        DISC = new ArrayList<courses>();
        LAB = new ArrayList<courses>();
        OTHER = new ArrayList<courses>();
        counter++;
        count = counter;
    }

    Generator(ArrayList<courses> coursesList){
        for(int i=0; i<coursesList.size(); i++){

        }
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

    }

    public ArrayList<courses> sortLectures(ArrayList<courses> coursesList) {
        numDiffClasses = 0;
        String className = "";
        ArrayList<courses> Lectures = new ArrayList<courses>();

        for (courses entity : coursesList) {
            if (entity.getActivity().equals("LECT")) {
                String s = entity.getNumber();
                s = s.substring(0, s.indexOf('-') + 4);
                //System.out.println("test: "+entity.getNumber());
                Lectures.add(new courses(entity));//added
                Lectures.get(Lectures.size() - 1).setNumber(s);//edit entry

                if (!className.equals(s)) {
                    numDiffClasses++;
                }

                className = s;
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
            ArrayList<courses> failed = new ArrayList<courses>();
            return failed;
        }

        return Lectures;
    }

    public void setCount(int i){
        counter=i;
    }

    public int getCount(){
        return count;
    }

    public ArrayList<ArrayList<courses>> getFinalList(ArrayList<courses> Lectures, ArrayList<courses> coursesList){
        ArrayList<courses> finalList = new ArrayList<courses>();
        boolean okay = false;

        ArrayList<ArrayList<courses>> Valid = new ArrayList<ArrayList<courses>>();
        ArrayList<Generator> Scheduler = new ArrayList<Generator>();
        ArrayList<ArrayList<Generator>> semifinal = new ArrayList<ArrayList<Generator>>();

        /**
         * if lecture and in Lectures list then its okay to add everything
         * after until you reach a lecture that's not in the list. if the lecture isn't in
         * list then its not okay to add everything after.
         */
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
                System.out.println("FINAL LIST: "+course.getCrn() +" "+course.getNumber());
            }
        }

        String str="";
        Generator c = new Generator();

        for(courses entity: finalList){

            if(Scheduler.size() > 0){
                Log.d("SCHEDULER",Scheduler.size()+"");
            }

            if(entity.getActivity().equals("LECT")){

                if(!entity.getNumber().substring(0, entity.getNumber().indexOf('-')+4).equals(str)){
                    c.setCount(0);
                }

                str = entity.getNumber().substring(0, entity.getNumber().indexOf('-')+4);
                okay = true;

                Scheduler.add(new Generator(entity));
                continue;
            }

            if(okay){
                Scheduler.get(Scheduler.size()-1).insert(entity);
            }
        }

        semifinal.addAll(Kcombinations(Scheduler.toArray(new Generator[Scheduler.size()]),numDiffClasses));
        Scheduler.clear();

        for(ArrayList<Generator> test : semifinal){
            for(ArrayList<courses> list : df(test.toArray(new Generator[test.size()]))){
                Valid.add(new ArrayList<courses>());
                Valid.get(Valid.size()-1).addAll(list);
            }
        }

        return Valid;
    }

    public static ArrayList<ArrayList<courses>> df(Generator[] arr){
        ArrayList<ArrayList<courses>> coursesList = new ArrayList<ArrayList<courses>>();
        ArrayList<courses> list =  new ArrayList<courses>();


	/*for(int i=0;i<arr.length;i++){
		//list.add(arr[i].LECT);
		for (int j = 0 ;j<arr.length;j++){
			list.add(arr[j].LECT);

			if(arr[i].DISC.size() != 0){
				list.add(arr[i].DISC.get(0));
			}

			if(arr[i].LAB.size() !=0){
				list.add(arr[i].LAB.get(0));
			}

		}

		if(testSchedule(list)){
			coursesList.add(list);
		}

	}*/

        for(int j = 0; j < arr.length; j++)
        {
            list = new ArrayList<courses>();
            for(int i = 0; i<arr.length; i++)
            {
                list.add(arr[i].LECT);

                if(arr[i].DISC.size() != 0 && j < arr[i].DISC.size())
                {
                    list.add(arr[i].DISC.get(j));
                }

                if(arr[i].LAB.size() !=0 && j < arr[i].LAB.size())
                {
                    list.add(arr[i].LAB.get(j));
                }

                if(arr[i].OTHER.size() != 0 && j < arr[i].OTHER.size())
                {
                    list.add(arr[i].OTHER.get(j));
                }
            }
            if(testSchedule(list))
            {
                //for(courses entity : list){
                //System.out.print(entity.getCrn());
                //}
                coursesList.add(list);
            }
        }

        return coursesList;
    }

    public static boolean testSchedule(ArrayList<courses> list){

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

    public static ArrayList<ArrayList<Generator>> Kcombinations(Generator[] sequence, int n){
        ArrayList<ArrayList<Generator>> list = new ArrayList<ArrayList<Generator>>();
        int N = sequence.length,ct = 1;
        String str = "";
        //Generator[] temp = new Generator[n];
        //param int[] sequence,
        //N = arr size.
        //n = num elements in subsets or number of different classes
        Log.d("TEST VALUEs: ",N + "");
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
                list.add(new ArrayList<Generator>());
                System.out.print("{ ");
                for (int j = 0; j < N; j++)
                {
                    if (binary[i] % 10 == 1)
                    {
                        if(!sequence[j].LECT.getNumber().substring(0, sequence[j].LECT.getNumber().indexOf('-')+4).equals(str)){
                            ct++;
                        }

                        list.get(list.size()-1).add(sequence[j]);

                        str = sequence[j].LECT.getNumber().substring(0, sequence[j].LECT.getNumber().indexOf('-')+4);
                        //System.out.print(sequence[j].LECT.getNumber() + " ");

                    }
                    binary[i] /= 10;
                }
                //System.out.println("}");

                if(ct != n){
                    //System.out.print(ct +" ::Rejected:: \n");
                    list.remove(list.size()-1);
                }
            }
        }

        return list;
    }
}
