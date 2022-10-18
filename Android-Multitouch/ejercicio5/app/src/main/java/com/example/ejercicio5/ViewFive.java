package com.example.ejercicio5;

import android.app.Activity;
import android.content.Context;
import android.gesture.Gesture;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ViewFive extends View {
    private float mScaleFactor = 100.f;
    Paint paint = new Paint();
    float side = 250;
    GestureDetector mGestureDetector;
    ScaleGestureDetector mScaleDetector;
    ScaleListener mScaleListener;
    List<Square> squares;


    public ViewFive(Context context,AttributeSet attrs) {
        super(context, attrs);
        squares = new ArrayList<>();
        GestureListener gestureListener = new GestureListener();
        mGestureDetector = new GestureDetector(getContext(), gestureListener);
        mGestureDetector.setOnDoubleTapListener(gestureListener);
        mScaleListener = new ScaleListener();
        mScaleDetector = new ScaleGestureDetector(getContext(), mScaleListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        mScaleDetector.onTouchEvent(event);
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Square s : squares) {
            s.draw(canvas,paint);
        }
    }
    public Square getTouchedSquare(Float x, Float y){
        for (int i = squares.size()-1; i >= 0; i--) {
            Square s = squares.get(i);
            float x1 = s.getX() - side;
            float x2 = s.getX() + side;
            float y1 = s.getY() - side;
            float y2 = s.getY() + side;
            if ( (x >= x1 && x<= x2) && (y >= y1 && y <= y2) ){
                return s;
            }
        }
        return null;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        public Square square;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            square = getTouchedSquare(detector.getFocusX(), detector.getFocusY());
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(85f, Math.min(250f, mScaleFactor));
            if(square != null) {
                square.setSide(mScaleFactor);
            }
            invalidate();
            return true;
        }

    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        public Square square;
        public Float lastTouchX;
        public Float lastTouchY;

        @Override
        public boolean onDown(MotionEvent e) {
            lastTouchX= e.getX();
            lastTouchY = e.getY();
            square = getTouchedSquare(lastTouchX,lastTouchY);
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if(square != null){
                float deltaX = e2.getX() - lastTouchX;
                float deltaY = e2.getY() - lastTouchY;
                square.setX(square.getX() + deltaX);
                square.setY(square.getY() + deltaY);
                lastTouchX = e2.getX();
                lastTouchY = e2.getY();
            }
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            float x = e.getX();
            float y = e.getY();
            square = new Square(x,y,side);
            squares.add(square);
            return true;

        }


    }

}
