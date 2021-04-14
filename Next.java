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

public class Next extends AppCompatActivity {

    private Collection<Integer> Cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Cities = new ArrayList<>();
        Cities.add(1);
        Cities.add(2);
        Cities.add(3);
        Cities.add(4);
        Cities.add(5);
        Cities.add(6);
        Cities.add(7);
        Cities.add(8);

        Graph graph = new Graph();
        graph.addCity(1,2,4);
        graph.addCity(1,3,10);
        graph.addCity(1,8,5);


        graph.addCity(2,3,11);
        graph.addCity(2,4,15);


        graph.addCity(3,4,13);
        graph.addCity(3,5,3);
        graph.addCity(3,8,11);

        graph.addCity(4,6,5);
        graph.addCity(4,5,6);


        graph.addCity(5,6,2);
        graph.addCity(5,7,5);

        graph.addCity(6,7,8);


        graph.addCity(7,8,7);

        Log.d("GRAPH",graph.toString());
        LinkedList<Integer> Winner = new LinkedList<>();
        int Sum = 0;
        for(int i = 0 ; i <10;i++){
            Log.d("I:I:I","I:   "+i);

            int X = 0;

            while(!graph.getRoute().containsAll(Cities)){
                X +=graph.Next(graph.getCurrCity());

            }

            if(X < Sum||Sum ==0){
                Sum = X;

                Winner.clear();
                for(int k = 0 ; k < graph.getRoute().size();k++){
                    Winner.add(k,graph.getRoute().get(k));
                }

            }
            graph.startOver();


        }

        for(int i = 0 ; i < Winner.size();i++){
            Log.d("WINNNERRRR",Winner.get(i).toString());
        }






    }
    public class Graph{
        private HashMap<Integer,List<Integer[]>> Map;

        private Integer Cursor;
        private int deletePos;
        private HashMap<Integer,List<Integer[]>>  Saved;
        private LinkedList<Integer> Route;
        private boolean dirty;

        public Graph(){
            Map = new HashMap<>();
            Cursor = 1;
            deletePos = 1;
            Saved = new HashMap<>();
            Route = new LinkedList<>();
            Route.add(0,1);
            dirty = false;


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

            Random random = new Random();


            Log.d("SOURCE",Src.toString());
            int Indx = random.nextInt(Map.get(Src).size());
            LinkedList<Integer[]> listOfDests = (LinkedList<Integer[]>) Map.get(Src);
            Integer[] mapOfDestDist = listOfDests.get(Indx);


            Route.add(listOfDests.get(Indx)[0]);

            if(!dirty){
                for(int i = 0 ; i < Map.size();i++){
                    Saved.put(Src,Map.get(Src));

                }
            }


            Cursor = listOfDests.get(Indx)[0];


            dirty = true;




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
            for(int i = 0 ; i < Saved.size();i++){
                Map.put((Integer) Saved.keySet().toArray()[i],Saved.get(Saved.keySet().toArray()[i]));
            }
        }


    }


}