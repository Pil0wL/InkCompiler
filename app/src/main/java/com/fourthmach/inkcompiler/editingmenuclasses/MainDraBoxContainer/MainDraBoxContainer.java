package com.fourthmach.inkcompiler.editingmenuclasses.MainDraBoxContainer;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.fourthmach.inkcompiler.editingmenuclasses.EFIActivityInfo;

public class MainDraBoxContainer extends FrameLayout {

    public MainDraBoxContainer(Context context) {
        super(context);
    }

    public MainDraBoxContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainDraBoxContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    private EFIActivityInfo efiActivityInfo;
    public void actualStart(EFIActivityInfo efiActivityInfo) {
        this.efiActivityInfo = efiActivityInfo;
    }

    public void expandByTargetBox(FrameLayout newBox) {
        ViewGroup.MarginLayoutParams GParams = (ViewGroup.MarginLayoutParams) getLayoutParams();

        int GWidth = getWidth();
        int GHeight = getHeight();

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(newBox.getWidth(), View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        newBox.measure(widthMeasureSpec, heightMeasureSpec);
        int measuredHeight = newBox.getMeasuredHeight();

        int boxLeft = (int) newBox.getX();
        int boxTop = (int) newBox.getY();
        int boxWidth = newBox.getWidth();
        int boxHeight = measuredHeight;
        int boxRight = boxLeft + boxWidth;
        int boxBottom = boxTop + boxHeight;

        Log.d("info", String.format("boxLeft = %d, boxTop = %d, boxWidth = %d, boxHeight = %d\nGWidth = %d, GHeight = %d",
                boxLeft,
                boxTop,
                boxWidth,
                boxHeight,
                GWidth,
                GHeight
        ));

        int moveX = 0;
        int moveY = 0;

        int boxMoveX = 0;
        int boxMoveY = 0;
        boolean resized = false;
        if (boxLeft < 0) {
            moveX = boxLeft;
            int negativeBoxLeft = -boxLeft;
            boxMoveX = negativeBoxLeft;
            GParams.width += negativeBoxLeft;
            resized = true;
        } else if (GWidth < boxRight) {
            int shiftX = boxRight - GWidth;
            moveX = shiftX;
            GParams.width += shiftX;
            resized = true;
        }
        if (boxTop < 0) {
            moveY = boxTop;
            int negativeBoxTop = -boxTop;
            boxMoveY = negativeBoxTop;
            GParams.height += negativeBoxTop;
            resized = true;
        } else if (GHeight < boxBottom) {
            int shiftY = boxBottom - GHeight;
            moveY = shiftY;
            GParams.height += shiftY;
            resized = true;
        }

        if (resized) {
            setX(getX() + (int) ((double) moveX * 0.5));
            setY(getY() + (int) ((double) moveY * 0.5));

            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.setX(child.getX() + boxMoveX);
                child.setY(child.getY() + boxMoveY);
            }
            setLayoutParams(GParams);
        }

    }

}
