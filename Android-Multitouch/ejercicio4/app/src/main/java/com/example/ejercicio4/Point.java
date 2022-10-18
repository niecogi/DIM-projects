package com.example.ejercicio4;

public class Point {
    public float x,y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float distanceTo(Point p1){
        return (float) Math.sqrt((this.x-p1.x)*(this.x-p1.x) + (this.y-p1.y)*(this.y-p1.y));
    }

    public Point getMiddlePoint(Point p1){
        return new Point((p1.x + this.x) / 2, (p1.y + this.y) / 2) ;
    }

}