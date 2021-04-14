package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Collection<Integer> Cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        float Prob = new Random().nextFloat();
        double X = (new Random().nextDouble()*10.24)-5.12;
        double fX = 10+(Math.pow(X,2)-(10*Math.cos(2*180*X)));
        double X1 = X;
        for(int i = 0 ; i < 1000 ; i++){

            for(int j = 2 ; j < 10 ; j++){
                float R = new Random().nextFloat();
                if(R<Prob){
                    X1 = (new Random().nextDouble()*10.24)-5.12;
                }
            }
            double fX1 = 10+(Math.pow(X1,2)-(10*Math.cos(2*180*X1)));
            if(fX1 > fX){
                X = X1;
            }
        }
        Log.d("PEACE",""+X);

    }
    public class Graph{
        private HashMap<Integer,List<Integer[]>> Map;

        private Integer Cursor;
        private int deletePos;
        private HashMap<Integer,List<Integer[]>>  Saved;
        private LinkedList<Integer> Route;
        private boolean dirty;
        private LinkedList<Integer> comingFrom;


        public Graph(){
            Map = new HashMap<>();
            Cursor = 1;
            deletePos = 1;
            Saved = new HashMap<>();
            Route = new LinkedList<>();
            Route.add(0,1);
            dirty = false;
            comingFrom = new LinkedList<>();

        }

        public void addCity(Integer Src,Integer Dest,Integer Dist){
            if(!Map.containsKey(Src)){
                Map.put(Src,new LinkedList<>());
            }
            if(!Map.containsKey(Dest)){
                Map.put(Dest,new LinkedList<>());
            }

            Integer[]  DestDist = new Integer[2];
            DestDist[0] = Dest;
            DestDist[1] = Dist;

            Integer[] SrcDist = new Integer[2];

            SrcDist[0] = Src;
            SrcDist[1] = Dist;

            Map.get(Src).add(DestDist);
            Map.get(Dest).add(SrcDist);
        }

        public Integer Next(Integer Src){




            Log.d("SOURCE",Src.toString());

            LinkedList<Integer[]> listOfDests = (LinkedList<Integer[]>) Map.get(Src);
            int Indx = 0;
            int Steepest = 0;

            for(int i = 0 ; i < listOfDests.size();i++){

                if(comingFrom.contains(listOfDests.get(i)[0])&& !comingFrom.containsAll(listOfDests)){
                    continue;
                }
                if(Steepest==0 || Steepest > listOfDests.get(i)[1]){
                    Log.d("MOVING","FROM:"+Src+" TO:" +listOfDests.get(i)[0]+" Dist: "+listOfDests.get(i)[1]);
                    Steepest = listOfDests.get(i)[1];
                    Indx = i;
                }
            }

            Integer[] mapOfDestDist = listOfDests.get(Indx);


            Route.add(listOfDests.get(Indx)[0]);

            if(!dirty){
                for(int i = 0 ; i < Map.size();i++){
                    Saved.put(Src,Map.get(Src));

                }
            }


            Cursor = listOfDests.get(Indx)[0];


            dirty = true;

            comingFrom.add(Src);
            return mapOfDestDist[1];
        }
        public Integer getCurrCity(){
            return Cursor;
        }

        @Override
        public String toString() {
            StringBuilder Builder = new StringBuilder();
            for(int i = 0 ; i <Map.keySet().size();i++){
                Integer Src = (Integer) Map.keySet().toArray()[i];
                for(int j = 0 ; j < Map.get(Src).size();j++) {
                    Builder.append("From: " + Map.keySet().toArray()[i] + " To: " + Map.get(Src).get(j)[0]+" Dist: "+Map.get(Src).get(j)[1]);

                }
            }
            return Builder.toString();
        }
        @NonNull
        @Override
        protected Object clone() throws CloneNotSupportedException {

            return Map.clone();
        }

        public LinkedList<Integer> getRoute() {
            return Route;
        }
        public void startOver(){
            Log.d("MOUTETOUTE","\n");
            Route.clear();
            Cursor = 1;
            dirty = false;
            comingFrom.clear();
            for(int i = 0 ; i < Saved.size();i++){
                Map.put((Integer) Saved.keySet().toArray()[i],Saved.get(Saved.keySet().toArray()[i]));
            }
        }


    }


}
