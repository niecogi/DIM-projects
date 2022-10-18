package com.example.ejercicio6;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ViewSix extends View {
    Paint paint = new Paint();
    private float mScaleFactor = 100.f;
    GestureDetector mGestureDetector;
    ScaleGestureDetector mScaleDetector;
    ScaleListener mScaleListener;
    List<Square> squares;
    List<Circle> circles;
    List<Triangle> triangles;
    Square squareref;
    Circle circleref;
    Triangle triangleref;
    MainActivity mainActivity = (MainActivity) getContext();

    public ViewSix(Context context, AttributeSet attrs) {
        super(context, attrs);
        squares = new ArrayList<>();
        circles = new ArrayList<>();
        triangles = new ArrayList<>();
        GestureListener gestureListener = new GestureListener();
        mGestureDetector = new GestureDetector(getContext(), gestureListener);
        mGestureDetector.setOnDoubleTapListener(gestureListener);
        mScaleListener = new ScaleListener();
        mScaleDetector = new ScaleGestureDetector(getContext(), mScaleListener);
        figureGenerator();
    }
    public int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    public void figureGenerator(){
        squareref = new Square(getRandomNumber(20,600),getRandomNumber(400,690),100);
        circleref = new Circle(getRandomNumber(20,800),getRandomNumber(700,1000), 100);
        triangleref = new Triangle(getRandomNumber(20,1000),getRandomNumber(1000,1400),200);

        for (int i = 0; i <= 2; i++) {
            Square square = new Square(getRandomNumber(10,700), getRandomNumber(0,255), getRandomNumber(100,250));
            squares.add(square);
            Circle circle = new Circle(getRandomNumber(10,700),getRandomNumber(0,255), getRandomNumber(100,250));
            circles.add(circle);
            Triangle triangle = new Triangle(getRandomNumber(100,200), getRandomNumber(10,255), getRandomNumber(100,200));
            triangles.add(triangle);
        }
        for (int i = 0; i <= 2; i++) {
            Square square = new Square(getRandomNumber(255,900), getRandomNumber(500,1500), getRandomNumber(100,250));
            squares.add(square);
            Circle circle = new Circle(getRandomNumber(255,900),getRandomNumber(550,1600), getRandomNumber(100,250));
            circles.add(circle);
            Triangle triangle = new Triangle(getRandomNumber(100,200),getRandomNumber(255,900), getRandomNumber(100,200));
            triangles.add(triangle);
        }
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
        paint.setColor(Color.BLACK);
        squareref.draw(canvas,paint,true);
        circleref.draw(canvas,paint,true);
        triangleref.draw(canvas,paint,true);
        for (Triangle t : triangles) {
            t.draw(canvas,paint,false);
        }
        for (Circle c : circles) {
            c.draw(canvas,paint,false);
        }
        for (Square s : squares) {
            s.draw(canvas,paint,false);
        }

    }

    public Square getTouchedSquare(Float x, Float y){
        for (int i = squares.size()-1; i >= 0; i--) {
            Square s = squares.get(i);
            if(s.isPointInside(x,y)){
                return s;
            }
        }
        return null;
    }

    public Circle getTouchedCircle(Float x, Float y){
        for (int i = circles.size()-1; i >= 0; i--) {
            Circle c = circles.get(i);
            float x1 = x - c.getX();
            float y1 = y - c.getY();
            float radius = c.getRadius();
            float area = (float) c.getArea();
            if ( x1*x1 + y1*y1 <= area && x1 < radius && y1 < radius   ) {
                return c;
            }
        }
        return null;
    }
    public Triangle getTouchedTriangle(Float x, Float y){
        for (int i = triangles.size()-1; i >= 0; i--) {
            Triangle t = triangles.get(i);
            if (t.isPointInside(x, y)) {
                return t;
            }
        }
        return null;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        public Square square;
        public Triangle triangle;
        public Circle  circle;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            square = getTouchedSquare(detector.getFocusX(), detector.getFocusY());
            triangle = getTouchedTriangle(detector.getFocusX(), detector.getFocusY());
            circle = getTouchedCircle(detector.getFocusX(), detector.getFocusY());
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(90f, Math.min(300f, mScaleFactor));
            if(square != null) {
                square.setSide(mScaleFactor);
                invalidate();
            }
            if(triangle != null) {
                triangle.setWidth(mScaleFactor);
                invalidate();
            }
            if(circle != null) {
                circle.setRadius(mScaleFactor);
                invalidate();
            }
            return true;
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        public Square square;
        public Circle circle;
        public Triangle triangle;
        public Float lastTouchXS;
        public Float lastTouchYS;
        public Float lastTouchXC;
        public Float lastTouchYC;
        public Float lastTouchXT;
        public Float lastTouchYT;
        @Override
        public boolean onDown(MotionEvent e) {
            lastTouchXS = e.getX();
            lastTouchYS = e.getY();
            lastTouchXC = e.getX();
            lastTouchYC = e.getY();
            lastTouchXT = e.getX();
            lastTouchYT = e.getY();
            square = null;
            circle = null;
            triangle = null;
            square = getTouchedSquare(lastTouchXS,lastTouchYS);
            if(square != null) {return true;}
            circle = getTouchedCircle(lastTouchXC, lastTouchYC);
            if(circle != null) {return true;}
            triangle = getTouchedTriangle(lastTouchXT, lastTouchYT);
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if(triangle != null){
                float deltaX = e2.getX() - lastTouchXT;
                float deltaY = e2.getY() - lastTouchYT;
                triangle.setX(triangle.getX() + deltaX);
                triangle.setY(triangle.getY() + deltaY);
                lastTouchXT = e2.getX();
                lastTouchYT = e2.getY();
                if (triangle.isInside(triangleref)) {
                    triangles.remove(triangle);
                    mainActivity.sound();
                    invalidate();
                }
            }
            if(circle != null){
                float deltaX = e2.getX() - lastTouchXC;
                float deltaY = e2.getY() - lastTouchYC;
                circle.setX(circle.getX() + deltaX);
                circle.setY(circle.getY() + deltaY);
                lastTouchXC = e2.getX();
                lastTouchYC = e2.getY();
                if (circle.isInside(circleref)) {
                    circles.remove(circle);
                    mainActivity.sound();
                    invalidate();
                }
            }
            if(square != null){
                    float deltaX = e2.getX() - lastTouchXS;
                    float deltaY = e2.getY() - lastTouchYS;
                    square.setX(square.getX() + deltaX);
                    square.setY(square.getY() + deltaY);
                    lastTouchXS = e2.getX();
                    lastTouchYS = e2.getY();
                if (square.isInside(squareref)) {
                    squares.remove(square);
                    mainActivity.sound();
                    invalidate();
                }

            }
            if(squares.isEmpty() && triangles.isEmpty() && circles.isEmpty()){
                figureGenerator();
            }
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            int random = getRandomNumber(1,4);
            float x = e.getX();
            float y = e.getY();
            switch (random){
                case 1:
                    square = new Square(x,y,getRandomNumber(100,250));
                    squares.add(square);
                    break;
                case 2:
                    circle =  new Circle(x,y,getRandomNumber(100,250));
                    circles.add(circle);
                    break;
                case 3:
                    triangle = new Triangle(x,y,getRandomNumber(100,200));
                    triangles.add(triangle);
                    break;
            }
            return true;
        }


    }
}
