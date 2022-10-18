package com.example.ejercicio2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.example.ejercicio2.Line;

import java.util.ArrayList;
import java.util.Random;

public class ViewTwo extends View {
    Random random = new Random();
    Paint paint = new Paint();
    float prevX , prevY , newX , newY;
    int color = Color.BLACK;
    ArrayList<Line> lines = new ArrayList<Line>();


    public ViewTwo(Context context, AttributeSet attrs) {
        super(context,attrs);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(6f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(color);
        canvas.drawLine(prevX,prevY,newX,newY, this.paint);
        for(int i = 0; i < lines.size(); i++){
            ArrayList<Float> aux= lines.get(i).getPoints();
            paint.setColor((int)(float)aux.get(4));
            canvas.drawLine(aux.get(0),
                    aux.get(1),
                    aux.get(2),
                    aux.get(3),
                    this.paint);
        }

        }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                prevX = event.getX();
                prevY = event.getY();
                this.color = Color.rgb(random.nextInt(255),random.nextInt(255), random.nextInt(255));
                break;
            case MotionEvent.ACTION_MOVE:
                newX = event.getX();
                newY = event.getY();
                this.invalidate();
                break;
            case MotionEvent.ACTION_UP:
                Line aux = new Line( prevX, prevY, newX, newY, color);
                lines.add(aux);

                this.invalidate();
                break;
        }
        return true;
    }
}

