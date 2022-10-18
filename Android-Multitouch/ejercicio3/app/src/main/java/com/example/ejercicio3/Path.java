package com.example.ejercicio3;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Random;

public class Path {
    private ArrayList<Point> points = new ArrayList<>();
    int color;
    float tolerance;


    public Path(float tolerance){
        Random random = new Random();
        this.color = Color.rgb(random.nextInt(255),random.nextInt(255), random.nextInt(255));
        this.tolerance = tolerance;
    }
    public void Draw(Canvas canvas, Paint paint){
        paint.setColor(color);
        for(int i = 0; i < points.size()-1; i++){
            canvas.drawLine(points.get(i).x,points.get(i).y,points.get(i+1).x,points.get(i+1).y,paint);
        }
    }

    public void AddPoint(Point point){
        if(points.size() == 0 || point.distanceTo(points.get(points.size()-1)) > tolerance ){
            points.add(point);
        }
    }
}
