package com.ninja.cse.samuaricourses;

import java.util.ArrayList;

/**
 * Created by dgist on 4/20/2016.
 */
public class Generator {
    courses LECT;
    ArrayList<courses> DISC,LAB,OTHER;
    int count;
    static int counter=0;

    Generator(courses _lect){
        LECT = _lect;
        DISC = new ArrayList<courses>();
        LAB = new ArrayList<courses>();
        OTHER = new ArrayList<courses>();
        counter++;
        count = counter;
    }

    Generator(){

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

    public void setCount(int i){
        counter=i;
    }

    public int getCount(){
        return count;
    }

}
