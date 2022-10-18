package com.example.ejercicio3;

public class Point {
    public float x,y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float distanceTo(Point p1){
        return (float) Math.sqrt((this.x-p1.x)*(this.x-p1.x) + (this.y-p1.y)*(this.y-p1.y));
    }

}
