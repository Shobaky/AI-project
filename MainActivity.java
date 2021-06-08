package com.example.myapplication;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Graph Cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Cities = new Graph();
        ArrayList<Double> Dists = new ArrayList<>();
        Dists.add(50.0);
        Dists.add(60.0);
        Dists.add(10.0);
        Dists.add(90.0);
        Dists.add(100.0);
        for(double i = 1 ; i <= 5 ; i++){
            for(double j = i+1 ; j <= 5 ; j++){

                Cities.addCity(i,j,Dists.get((int) (i-1)));
            }
        }
        Map<Integer,Double[]> Ants = new HashMap<>();
        int Ant = 0;
        for(Double city : Cities.getCities()){
            Double[] Visited = new Double[5];
            Visited[0] = city;
            Ants.put(Ant,Visited);
            Cities.addAntPath(Ant,city);
            Ant++;
        }
        for(int l = 0 ; l < 20;l++) {
            for (int City = 0; City < Cities.getCities().size() - 1; City++) {
                for (int ant = 0; ant < Ants.keySet().size(); ant++) {
                    ArrayList<Double> antCities = Cities.getCities();
                    antCities.removeAll(Arrays.asList(Ants.get(ant)));

                    Double[] antVisited = Ants.get(ant);
                    int numberOfVisited = 0;
                    for (Double visited : antVisited) {
                        if (visited != null) {
                            numberOfVisited++;
                        }
                    }
                    double probSum = 0.0;

                    for (Double city : antCities) {

                        double[] destInfo = Cities.getDestInfo(antVisited[numberOfVisited - 1], city);

                        probSum += (destInfo[1] / Math.pow(destInfo[0], 0.1));

                    }
                    Map<Double, Double> Probs = new HashMap<>();

                    for (double tobeVisited : antCities) {

                        double[] destInfo = Cities.getDestInfo(antVisited[numberOfVisited - 1], tobeVisited);

                        double P = (destInfo[1] / Math.pow(destInfo[0], 0.1)) / probSum;

                        Probs.put(tobeVisited, P);


                    }

                    float r = new Random().nextFloat();
                    Double[] visitedAlready = Ants.get(ant);

                    double spinnerSlice = Probs.get(Probs.keySet().toArray()[0]);
                    int Spin = 1;
                    for (double city : Probs.keySet()) {


                        if (r <= spinnerSlice) {
                            visitedAlready[numberOfVisited] = city;

                            Ants.put(ant, visitedAlready);

                            Cities.addAntPath(ant, city);
                            numberOfVisited++;
                            break;

                        } else {
                            spinnerSlice += Probs.get(Probs.keySet().toArray()[Spin]);

                        }
                        Spin++;
                    }


                    //اعتقد ملهاش لزمة اني الف على كل المدن طب مانا الف على المدن الي انا متاكد ان النملة مشت فيها وخلاص

                }


            }
            HashMap<Integer, Double> antsLengths = Cities.getAntLengths();
            double L = 0.0;
            for (int Anto : antsLengths.keySet()) {
                if (L < antsLengths.get(Anto)) {
                    L = antsLengths.get(Anto);
                }
            }
            double Q = 20.0;
            for (double cityI = 1; cityI <= 5; cityI++) {
                for (double cityJ = 1; cityJ <= 5; cityJ++) {
                    double deltaT = 0.0;
                    for (int Anto : Ants.keySet()) {

                        Double[] cityPair = new Double[2];
                        cityPair[0] = cityI;
                        cityPair[1] = cityJ;
                        if (Cities.getAntPath(Anto).indexOf(cityJ) - Cities.getAntPath(Anto).indexOf(cityI) == 1) {
                            deltaT += Q / antsLengths.get(Anto);
                        }
                    }
                    Cities.updateTao(cityI, cityJ, deltaT);

                }
            }

            for (double cityI = 1; cityI <= 5; cityI++) {
                for (double cityJ = 1; cityJ <= 5; cityJ++) {
                    double[] cityPair = Cities.getDestInfo(cityI, cityJ);
                    Log.d("TAOPAIR", "SRc: " + cityI + " Dest: " + cityJ + " Tao: " + cityPair[1]);

                }
            }


        }
    }
    public class Graph{
        private HashMap<Double,List<Double[]>> Map;
        private HashMap<Integer,LinkedList<Double>> antRoute;

        private HashMap<Integer,Double> antDist;





        public Graph(){
            Map = new HashMap<>();


            antRoute = new HashMap<>();
            antDist = new HashMap<>();


        }

        public void addCity(Double Src,Double Dest,Double Dist){
            if(!Map.containsKey(Src)){
                Map.put(Src,new LinkedList<>());
            }
            if(!Map.containsKey(Dest)){
                Map.put(Dest,new LinkedList<>());
            }
            double Tao= (double) new Random().nextFloat();
            Double[]  DestDistTao = new Double[3];
            DestDistTao[0] = Dest;
            DestDistTao[1] = Dist;
            DestDistTao[2] = Tao;

            Double[] SrcDistTao = new Double[3];

            SrcDistTao[0] = Src;
            SrcDistTao[1] = Dist;
            SrcDistTao[2] = Tao;

            Map.get(Src).add(DestDistTao);
            Map.get(Dest).add(SrcDistTao);

        }
        public double[] getDestInfo(Double Src,Double Dest){
            double[] Inf = new double[2];
            for(Double[] destInfo : Map.get(Src)){
                if(destInfo[0].equals(Dest)){
                    Inf[0] = destInfo[1];
                    Inf[1] = destInfo[2];
                }
            }
            return Inf;
        }
        public void addAntPath(int Ant, Double City){
            if(antRoute.get(Ant)==null){
                antRoute.put(Ant,new LinkedList<>());
            }
            if(antRoute.get(Ant).size()>0) {
                if (antDist.get(Ant) == null) {
                    antDist.put(Ant, getDestInfo(antRoute.get(Ant).getLast(), City)[0]);
                } else {
                    antDist.put(Ant, getDestInfo(antRoute.get(Ant).getLast(), City)[0] + antDist.get(Ant));
                }
            }
            antRoute.get(Ant).push(City);
        }
        public LinkedList<Double> getAntPath(int Ant){
            return antRoute.get(Ant);
        }
        public HashMap<Integer,Double> getAntLengths(){
            return antDist;
        }


        public void updateTao(Double Src,Double Dest,Double deltaTao){
            for(Double[] DestInfo : Map.get(Src)){
                if(DestInfo[0].equals(Dest)){
                    DestInfo[2] = 0.1*DestInfo[2]+deltaTao;
                }
            }
        }

        /*public Integer Next(Integer Src){


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
        }*/


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


       /* public void startOver(){
            Log.d("MOUTETOUTE","\n");
            Route.clear();
            Cursor = 1;
            dirty = false;
            comingFrom.clear();
            for(int i = 0 ; i < Saved.size();i++){
                Map.put((Integer) Saved.keySet().toArray()[i],Saved.get(Saved.keySet().toArray()[i]));
            }
        }*/
        public ArrayList<Double> getCities(){
            ArrayList<Double> CitiesArray = new ArrayList<>();
            for(int i = 0 ; i < Map.keySet().size();i++){
                CitiesArray.add((Double) Map.keySet().toArray()[i]);
            }
            return CitiesArray;
        }


    }


}
