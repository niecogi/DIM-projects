package com.example.ejercicio4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class ViewFour extends View {
    AppMate appMate = new AppMate();
    Paint paint = new Paint();

    public ViewFour(Context context, AttributeSet attrs) {
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
        appMate.Draw(canvas,paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int index = event.getActionIndex();
        int id =  event.getPointerId(index);
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                appMate.addPoint(id,new Point (event.getX(index), event.getY(index)));
                break;
            case MotionEvent.ACTION_MOVE:
                int pointerCount = event.getPointerCount();
                for(int i = 0; i < pointerCount; ++i) {
                    index = i;
                    id = event.getPointerId(index);
                    appMate.updatePoint(id,new Point (event.getX(index), event.getY(index)));
                }
                this.invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                appMate.removePoint(id);
                this.invalidate();
                break;
        }
        return true;
    }
}
