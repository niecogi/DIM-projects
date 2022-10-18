package com.example.ejercicio2;

import android.graphics.Color;

import java.util.ArrayList;


public class Line {
    ArrayList<Float> points =  new ArrayList<Float>();

    public Line(Float x, Float y, Float x1, Float y1, Integer color){
        points.add(x);
        points.add(y);
        points.add(x1);
        points.add(y1);
        points.add(Float.valueOf(color));

    }

    public ArrayList getPoints(){
        return  points;
    }


}
