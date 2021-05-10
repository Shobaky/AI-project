package com.example.ga;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private String[] Parents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Parents = new String[20];
        HashMap<String,Integer> parentsFitness = new HashMap<>();
        for(int i = 0 ; i < 20 ; i++){
            Parents[i] = String.valueOf(new Random().nextInt(2));
            for(int j = 0 ; j < 29 ;j++){
                Parents[i] = Parents[i].concat(String.valueOf(new Random().nextInt(2)));
            }
            Log.d("Parents",Parents[i]);
        }
        for(int i = 0 ; i < 100 ; i++){
            Log.d("PRENTSLENGTH",Parents.length+"");
            //مجموع الاحتمالات
            int Total = 0;
            //Iterating through parents
            int Parent = 0;
            parentsFitness.clear();
            for(int j = 0 ; j < 20 ; j++){
                int onesCount = 0;
                //Calculating the fitness

                for(int k = 0 ; k < 30 ; k++){
                    if(Parents[j].charAt(k)=='1'){
                        onesCount++;
                    }
                }
                Total = Total +onesCount;

                parentsFitness.put(Parents[j],onesCount);
                Log.d("FitnessParents","Length: "+parentsFitness.size()+" "+Parents[j]+"-->"+onesCount+" Total--> "+Total);
            }
            //خليت القيمة نسبية
            HashMap<String,Float> parentsProbability = new HashMap<String, Float>();
            for(int z = 0 ; z < parentsFitness.keySet().size();z++){
                if(parentsProbability.containsKey(Parents[z])){
                    Log.d("ICONTAINSMN",Parents[z]);
                }
                parentsProbability.put(Parents[z],  (((float)parentsFitness.get(Parents[z]))/Total));
                Log.d("ProbabilityParents",Parents[z]+"-->"+" Fitness--> "+parentsFitness.get(Parents[z])+" Prob--> "+(((float)parentsFitness.get(Parents[z]))/Total)+" Total--> "+Total);
            }

            String[] Children = new String[20];
            //لحد ما كل الاباء يموتو!
            while(Children[19]==null){
                String[] winnerParents = Spin(parentsProbability);
                String[] newChildren = Produce(winnerParents);
                Differentiate(Children,newChildren);
                Children[Parent] = newChildren[0];
                Parent++;
                Children[Parent] = newChildren[1];
                Parent++;
            }

            for(int k = 0 ; k < Children.length;k++){
                Log.d("CHILDRENWILDREN",Children[k]);
            }
            Parents = Mutate(Children).clone();



        }
        for (String parent : Parents) {
            double Fitness = Double.POSITIVE_INFINITY;
            if (parentsFitness.get(parent) < Fitness) {
                Log.d("WinnerParent", parent);
            }
        }
    }
    private String[] Spin(HashMap<String,Float> parentsProbability){
        //mutation
        String[] winnerParents = new String[2];
        for(int n = 0; n < 2 ; n++) {
            float r = new Random().nextFloat();
            int k = 0;
            float fk = parentsProbability.get(parentsProbability.keySet().toArray()[k]);

            float F = fk;
            //بلف الروليت
            while (F < r) {
                k = k + 1;
                fk = parentsProbability.get(parentsProbability.keySet().toArray()[k]);
                F = F + fk;
            }
            winnerParents[n] = Parents[k];
        }
        return winnerParents;
    }
    private String[] Produce(String[] winnerParents){
        //لكل اب كسب بدل كل الحروف بعد الحرف التاني

        String[] Children = new String[2];
        char[] charsOfWinner1 = winnerParents[0].toCharArray();
        char[] charsOfWinner2 = winnerParents[1].toCharArray();

        for(int c = 2 ; c <30;c++){
            char currChar = charsOfWinner1[c];
            
            charsOfWinner1[c] = charsOfWinner2[c];
            charsOfWinner2[c] = currChar;
        }
        Children[0] = String.valueOf(charsOfWinner1);
        Children[1] = String.valueOf(charsOfWinner2);

        return Children;
    }
    private String[] Mutate(String[] Children){

        for(int i = 0 ; i < Children.length ; i++){
            for(int j = 0; j < Children[i].length();j++){
                float r = new Random().nextFloat();
                if(r<0.01){
                    char[] newChild = Children[i].toCharArray();
                    if(Children[i].charAt(j)=='0'){
                        newChild[j] = '1';

                    }else{
                        newChild[j] = '0';
                    }
                    Children[i] = String.valueOf(newChild);
                    String[] childToDiff = new String[1];
                    childToDiff[0] = Children[i];
                    Differentiate(Children,childToDiff);


                }
            }
            Log.d("RRRR","r: "+Children[i]);
        }

        return Children;
    }
    private void Differentiate(String[] Children,String[] newChildren){
        String Diff1 = "";

        while(Arrays.asList(Children).contains(newChildren[0].concat(Diff1))){
            Log.d("CONTAINERMAINER",newChildren[0].concat(Diff1));
            if(Diff1.isEmpty()){
                Diff1 = "2";
            }else{
                int diff = Integer.parseInt(Diff1);
                diff =diff+1;
                Diff1 = String.valueOf(diff);
            }
            int indx0 = Arrays.asList(Children).indexOf(newChildren[0]);
            newChildren[0]= newChildren[0].concat(Diff1);

            Children[indx0] = newChildren[0];
            Log.d("CHILDRENFTER",Children[indx0]);

        }

        String Diff2 = "";
        while(newChildren.length>1&&Arrays.asList(Children).contains(newChildren[1].concat(Diff2))){
            if(Diff2.isEmpty()){
                Diff2 = "2";
            }else{
                int diff = Integer.parseInt(Diff2);
                diff =diff+1;
                Diff2 = String.valueOf(diff);
            }
            int indx1 = Arrays.asList(Children).indexOf(newChildren[1]);
            newChildren[1]= newChildren[1].concat(Diff2);

            Children[indx1] = newChildren[1];


        }

    }
}