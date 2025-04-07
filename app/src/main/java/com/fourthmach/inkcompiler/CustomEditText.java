package com.fourthmach.inkcompiler;

import android.content.Context;
import android.util.AttributeSet;

public class CustomEditText extends androidx.appcompat.widget.AppCompatEditText {

    public interface OnResizeListener {
        void onResized(int newWidth, int newHeight);
    }
    private static final OnResizeListener EMPTY_LISTENER = new OnResizeListener() {
        @Override
        public void onResized(int newWidth, int newHeight) {
            // Do nothing
        }
    };
    private OnResizeListener resizeListener = EMPTY_LISTENER;

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnResizeListener(OnResizeListener listener) {
        this.resizeListener = listener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (w != oldw || h != oldh) {
            resizeListener.onResized(w, h);
        }
    }
}