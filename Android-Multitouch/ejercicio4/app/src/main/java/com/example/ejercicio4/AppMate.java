package com.example.ejercicio4;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import java.util.HashMap;

public class AppMate {
    HashMap<Integer,Point> points =  new HashMap<>();
    public AppMate(){}
    public void addPoint(Integer id, Point point){
        if(points.size() >= 3) {return;}
        points.put(id,point);
    }
    public void removePoint(Integer id){
        points.remove(id);
    }
    public void updatePoint(Integer id, Point point){
        Point orig = points.get(id);
        if (orig == null) {return;}
        orig.x = point.x;
        orig.y = point.y;
    }
    public void Draw(Canvas canvas, Paint paint){
        if (points.size() != 3){
            return;
        }
        Point p1,p2,p3;

        Point[] pArray =  points.values().toArray( new Point[points.size()]);

        float d12= pArray[0].distanceTo(pArray[1]);
        float d23= pArray[1].distanceTo(pArray[2]);
        float d31= pArray[2].distanceTo(pArray[0]);
        float mindist = Math.min(d12,Math.min(d23,d31));

        if(mindist == d12){
            p1 = pArray[0];
            p2 = pArray[1];
            p3 = pArray[2];
        }else if(mindist == d23){
            p1 = pArray[1];
            p2 = pArray[2];
            p3 = pArray[0];
        } else {
            p1 = pArray[2];
            p2 = pArray[0];
            p3 = pArray[1];
        }
        Point pm = p1.getMiddlePoint(p2);
        double alpha = Math.atan2(p3.y-pm.y,p3.x-pm.x);
        paint.setColor(Color.GREEN);
        canvas.drawLine(p1.x,p1.y,p2.x,p2.y,paint);
        canvas.drawLine(p2.x,p2.y,p3.x,p3.y,paint);
        canvas.drawLine(p3.x,p3.y,p1.x,p1.y,paint);
        paint.setColor(Color.BLUE);
        canvas.drawLine(pm.x,pm.y,250*(float)Math.cos(alpha) + p3.x, 250*(float)Math.sin(alpha)+ p3.y,paint);
        paint.setColor(Color.MAGENTA);
        canvas.drawCircle(p1.x,p1.y,30,paint);
        canvas.drawCircle(p2.x,p2.y,30,paint);
        canvas.drawCircle(p3.x,p3.y,30,paint);


    }
}
