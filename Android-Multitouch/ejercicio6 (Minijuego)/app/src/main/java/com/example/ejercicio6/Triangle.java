package com.example.ejercicio6;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.Random;

public class Triangle {
   float width,halfWidth;
    int color;
    float x,x1,x2,x3;
    float y,y1,y2,y3;
    Random random;

    public Triangle (float x, float y, float width){
        this.x = x;
        this.y = y;
        this.width = width;
        halfWidth = width / 2;
        x1 =  x - halfWidth;
        x2 = x + halfWidth;
        x3 = x;
        y1 = y + halfWidth;
        y2 = y + halfWidth;
        y3 = y - halfWidth;
        random = new Random();
        this.color = Color.rgb(random.nextInt(255),random.nextInt(255), random.nextInt(255));
    }

    public void setWidth(float width) {
        this.width = width;
        halfWidth = width / 2;
    }

    public void setX(float x){
        this.x = x;
        x1 =  x - halfWidth;
        x2 = x + halfWidth;
        x3 = x;
    }
    public void setY(float y){
        this.y = y;
        y1 = y + halfWidth;
        y2 = y + halfWidth;
        y3 = y - halfWidth;
    }
    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
    public float getX1(){
        return x1;
    }
    public float getX2(){
        return x2;
    }
    public float getX3(){
        return x3;
    }
    public float getY1(){
        return y1;
    }
    public float getY2(){
        return y2;
    }
    public float getY3(){
        return y3;
    }
    public static float getArea(float x1, float y1, float x2, float y2, float x3, float y3){
        return (float) Math.abs((x1*(y2-y3) + x2*(y3-y1)+ x3*(y1-y2))/2.0);
    }
    public void draw(Canvas canvas, Paint paint, Boolean isRef){
        if(isRef == true ){
            paint.setColor(Color.BLACK);
        }else{
            paint.setColor(color);
        }
        Path path = new Path();
        path.moveTo(x3, y3);
        path.lineTo(x1, y1);
        path.lineTo(x2, y2);
        path.close();
        canvas.drawPath(path, paint);
    }
    public boolean isPointInside(float xP, float yP ){
        float area1 = getArea(x1,y1,x2,y2,x3,y3);
        float area2 = getArea(xP, yP, x2, y2, x3, y3);
        float area3 = getArea(x1, y1, xP, yP, x3, y3);
        float area4 = getArea(x1, y1, x2, y2, xP, yP);
       return Math.abs(area1 -(area2 + area3 + area4))  < 0.1;
    }

    public boolean isInside (Triangle t){
        return t.isPointInside(getX1(), getY1()) && t.isPointInside(getX2(), getY2()) && t.isPointInside(getX3(), getY3());
    }

}
