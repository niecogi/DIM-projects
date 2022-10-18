package com.example.ejercicio3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ViewThree extends View {
    Paint paint = new Paint();
    static final float tolerance = 5;
    HashMap<Integer,Path> paths = new HashMap<>();

    public ViewThree(Context context, AttributeSet attrs) {
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
        for (Path path : paths.values()) {
            path.Draw(canvas,paint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int index = event.getActionIndex();
        int id =  event.getPointerId(index);
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                Path path = new Path(tolerance);
                path.AddPoint(new Point (event.getX(index), event.getY(index)));
                paths.put(id,path);
                break;

            case MotionEvent.ACTION_MOVE:
                int pointerCount = event.getPointerCount();
                for(int i = 0; i < pointerCount; ++i) {
                    index = i;
                    id = event.getPointerId(index);
                    paths.get(id).AddPoint(new Point (event.getX(index), event.getY(index)));
                }
                this.invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                paths.remove(id);
                this.invalidate();
                break;
                
        }
        return true;
    }
}
