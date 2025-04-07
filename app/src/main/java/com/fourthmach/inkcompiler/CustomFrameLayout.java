package com.fourthmach.inkcompiler;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class CustomFrameLayout extends FrameLayout {

    // Define the listener interface
    public interface OnResizeListener {
        void onResized(int newWidth, int newHeight);
    }

    // A default "empty" listener that does nothing
    private static final OnResizeListener EMPTY_LISTENER = new OnResizeListener() {
        @Override
        public void onResized(int newWidth, int newHeight) {
            // Do nothing
        }
    };

    // Variable to hold the resize listener
    private OnResizeListener resizeListener = EMPTY_LISTENER;

    public CustomFrameLayout(Context context) {
        super(context);
    }

    public CustomFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // Method to set a custom listener
    public void setOnResizeListener(OnResizeListener listener) {
        this.resizeListener = listener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // If the size has changed, trigger the listener
        if (w != oldw || h != oldh) {
            resizeListener.onResized(w, h);
        }
    }
}