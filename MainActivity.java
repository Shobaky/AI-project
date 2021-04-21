package com.example.annealing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        double T0 = 20;
        int k = 0;
        double fT = T0 -(0.2*k);

        ArrayList<Integer> cities = new ArrayList<>();
        cities.add(1);
        cities.add(2);
        cities.add(3);
        cities.add(4);
        cities.add(5);
        cities.add(6);
        cities.add(7);
        cities.add(8);

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

        LinkedList<Integer> X0 = new LinkedList<>();
        int dist0 = 0;
        while(!graph.getRoute().containsAll(cities)){
            X0.push(graph.getCurrCity());
            dist0 += graph.Next(graph.getCurrCity());

        }
        graph.startOver();
        for(int i = 0 ;  i <100; i++){
            LinkedList<Integer> X1 = new LinkedList<>();
            int dist1 = 0;
            while(!graph.getRoute().containsAll(cities)){
                X1.push(graph.getCurrCity());
                dist1+=graph.Next(graph.getCurrCity());
            }
            graph.startOver();
            if(dist0>dist1){
                X0.clear();
                for(int j = 0 ; j < X1.size() ; j++){
                    X0.push(X1.get(j));
                }
            }else{
                double r = Math.random();
                if(r<Math.exp((dist0-dist1)/T0)){
                    X0.clear();
                    for(int j = 0 ; j < X1.size() ; j++){
                        X0.push(X1.get(j));
                    }
                }
            }
            T0 = fT;
            k++;
            fT = T0 -(0.2*k);
        }
        for(int l = 0 ; l < X0.size();l++){
            Log.d("ROUTEEE",X0.get(l).toString());
        }
    }
    public class Graph{
        private HashMap<Integer, List<Integer[]>> Map;

        private Integer Cursor;
        private int deletePos;

        private LinkedList<Integer> Route;
        private boolean dirty;
        private LinkedList<Integer> comingFrom;


        public Graph(){
            Map = new HashMap<>();
            Cursor = 1;
            deletePos = 1;
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


            for(int i = 0 ; i < listOfDests.size();i++){

                if(comingFrom.contains(listOfDests.get(i)[0])&& !comingFrom.containsAll(listOfDests)){
                    continue;
                }
                Indx = i;
            }

            Integer[] mapOfDestDist = listOfDests.get(Indx);


            Route.add(listOfDests.get(Indx)[0]);




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

        }


    }
}