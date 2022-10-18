package com.example.ejercicio6;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

public class Circle {
    int color;
    private float x,y,radius;

    public Circle(float x, float y, float r){
        Random random = new Random();
        this.color = Color.rgb(random.nextInt(255),random.nextInt(255), random.nextInt(255));
        this.x = x;
        this.y = y;
        radius = r;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getX() {return x;}
    public float getY() {return y;}
    public void setY(Float y) { this.y= y;}
    public void setX(Float x) { this.x= x;}
    public double getArea() {
        return Math.PI * radius * radius;
    }

    public boolean isInside (Circle  circle){
        float x1 = circle.getX();
        float y1 = circle.getY();
        float r1 =  circle.getRadius();
         int dist = (int)Math.sqrt(((x1 - x) * (x1 - x)) + ((y1 - y) * (y1 - y)));
         if(dist + radius == r1){
             return true;
         }else if(dist + radius < r1){
             return true;
         }else{
             return false;
         }
    }
    public void draw(Canvas canvas, Paint paint, Boolean isRef){
        if(isRef == true ){
            paint.setColor(Color.BLACK);
        }else{ paint.setColor(color);}
        canvas.drawCircle(x,y,radius,paint);
    }

}
