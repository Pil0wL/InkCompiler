package com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.standardfunctions;

import com.fourthmach.inkcompiler.editingmenuclasses.EFIActivityInfo;
import com.fourthmach.inkcompiler.editingmenuclasses.HelperForDraggableContainer;
import com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.EnhancedDraggableLayout.EnhancedDraggableLayout;
import com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.EnhancedEditText.EnhancedEditText;

public class EditTextFunction extends functionbase {


    private EFIActivityInfo efiActivityInfo;
    @Override
    public void actionDown(EnhancedDraggableLayout.ActionArgs actionArgs, EnhancedDraggableLayout enhancedDraggableBox) {
        EnhancedEditText editText = enhancedDraggableBox.editText;
        editText.manualFocus();

    }

    /*

    public void expandGIfNeeded(FrameLayout newBox) {
        ViewGroup.MarginLayoutParams GParams = (ViewGroup.MarginLayoutParams) containerFor_draggableBox.getLayoutParams();

        int GWidth = containerFor_draggableBox.getWidth();
        int GHeight = containerFor_draggableBox.getHeight();

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
        containerFor_draggableBox.setX(containerFor_draggableBox.getX() + (int) ((double) moveX * 0.5));
        containerFor_draggableBox.setY(containerFor_draggableBox.getY() + (int) ((double) moveY * 0.5));

        for (int i = 0; i < containerFor_draggableBox.getChildCount(); i++) {
            View child = containerFor_draggableBox.getChildAt(i);
            child.setX(child.getX() + boxMoveX);
            child.setY(child.getY() + boxMoveY);
        }
        containerFor_draggableBox.setLayoutParams(GParams);
    }

}
     */

    public EditTextFunction(EFIActivityInfo efiActivityInfo) {
        this.efiActivityInfo = efiActivityInfo;
    }
}
