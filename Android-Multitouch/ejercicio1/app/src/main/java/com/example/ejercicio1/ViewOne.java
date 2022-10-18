package com.example.ejercicio1;

import android.view.View;
import java.util.Random;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ViewOne extends View {
    Random random = new Random();
    Paint paint = new Paint();
    float prevX , prevY , newX , newY;
    int color = Color.BLACK;
    public ViewOne(Context context, AttributeSet attrs) {
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
        paint.setColor(this.color);
        canvas.drawLine(this.prevX,this.prevY,this.newX,this.newY,this.paint);
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
                prevX = -1;
                prevY = -1;
                newX = -1;
                newY = -1;
                this.invalidate();
                break;
        }
        return true;
    }
}

