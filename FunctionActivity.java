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
/*-------------------QUESTION 2: Sphere func-------------------------*/
public class FunctionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        double T0 = 20;
        int k = 0;
        double fT = T0 -(0.2*k);
        double fX = 0;
        double[] X0 = new double[10];
        for(int n = 0 ; n < 10 ; n ++){
            X0[n]= Math.random()*n;
            fX+=Math.pow(Math.random()*n,2);
        }
        for(int i = 0 ; i < 1000 ; i++){
            double[] X1 = new double[10];
            double fX1 =0;
            for(int n = 0 ; n < 10 ; n++){
                if(2*X0[n]<0){
                    X1[n] = X0[n]+(Math.random()*(n+1));
                }else{
                    X1[n] = X0[n]-(Math.random()*(n+1));
                }
                fX1+=Math.pow(X1[n],2);
            }
            if(fX1<fX){
                for(int n = 0 ; n < 10 ; n++){
                    X0[n] = X1[n];
                }
                fX = fX1;
            }else{
                double r = Math.random();
                if(r<Math.exp((fX-fX1)/T0)){
                    for(int n = 0 ; n < 10 ; n++){
                        X0[n] = X1[n];
                    }
                    fX =fX1;
                }
            }
            T0 = fT;
            k++;
            fT = T0 -(0.2*k);
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