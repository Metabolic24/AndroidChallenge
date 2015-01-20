package com.m2dl.helloandroid.helloandroid2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CustomDrawableView extends View{

    private ShapeDrawable mDrawable;
    int x = 10; int y = 10;
    int width = 100; int height = 150;

    private OnTouchListener listener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction())
            {
                case MotionEvent.ACTION_MOVE: {
                    x = (int)event.getX();
                    y = (int)event.getY();
                    invalidate(); // pour invalider l'image et forcer un rappel à la methode onDraw de la classe.
                }
            }
            return true;
        }
    };

    public CustomDrawableView(Context context) {
        super(context);
        mDrawable = new ShapeDrawable(new OvalShape());
        mDrawable.getPaint().setColor(0xff74AC23);
        setOnTouchListener(listener);
    }

    public CustomDrawableView(Context context, AttributeSet attr) {
        super(context, attr);
        mDrawable = new ShapeDrawable(new OvalShape()); // ici on affiche un oval...
        mDrawable.getPaint().setColor(0xff74AC23);
        setOnTouchListener(listener);
    }

    protected void onDraw(Canvas canvas) {
        mDrawable.setBounds(x, y, x + width, y + height);
        mDrawable.draw(canvas);
    }
}
