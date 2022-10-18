package com.example.ejercicio6;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

public class Square {
    int color;
    float x,y,side;
    public Square(float x, float y, float side){
        Random random = new Random();
        this.color = Color.rgb(random.nextInt(255),random.nextInt(255), random.nextInt(255));
        this.side = side;
        this.x = x;
        this.y = y;

    }

    public float getX() {return x;}
    public float getY() {return y;}
    public void setY(Float y) { this.y= y;}
    public void setX(Float x) { this.x= x;}

    public void draw(Canvas canvas, Paint paint,boolean isRef){
        if(isRef == true ){
            paint.setColor(Color.BLACK);
        }else{ paint.setColor(color);}

        canvas.drawRect((x - side), (y - side), ( x + side), (y + side),paint);
    }


    public void setSide(float mScaleFactor) {
        this.side = mScaleFactor;
    }

    public boolean isPointInside(float xP, float yP ){
        float x1 = getX() - side;
        float x2 = getX() + side;
        float y1 = getY() - side;
        float y2 = getY() + side;
        return (xP >= x1 && xP<= x2) && (yP >= y1 && yP <= y2);
    }
    public boolean isInside (Square s){
        return s.isPointInside(getX() - side, getY()- side) && s.isPointInside(getX() + side, getY() + side);
    }

}

