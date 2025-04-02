package com.fourthmach.inkcompiler.editingmenuclasses;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class DraggableFrameLayout extends FrameLayout {

    public DraggableFrameLayout(Context context) {
        super(context);
    }

    public DraggableFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DraggableFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false; // Force G to always intercept touch events
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            performClick(); // Ensures accessibility support
        }
        return true; // Always consume touch events
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }
}