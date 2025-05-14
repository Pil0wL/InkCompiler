package com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.standardfunctions;

import android.util.Log;
import android.widget.FrameLayout;

import com.fourthmach.inkcompiler.editingmenuclasses.EFIActivityInfo;
import com.fourthmach.inkcompiler.editingmenuclasses.HelperForDraggableContainer;
import com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.EnhancedDraggableLayout.EnhancedDraggableLayout;
import com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.RichTextRenderer.RichTextRenderer;

public class ResizeFunction extends functionbase {

    private EFIActivityInfo efiActivityInfo;

    @Override
    public void actionDown(EnhancedDraggableLayout.ActionArgs actionArgs, EnhancedDraggableLayout enhancedDraggableBox) {

        int[] location = new int[2]; enhancedDraggableBox.getLocationOnScreen(location);
        int absoluteX = location[0];

        float initialWidth = enhancedDraggableBox.getWidth();
        float middle = initialWidth / 2f;

        Log.d("EnhancedDraggableLayout", String.format("actionArgs.startX = %.2f | absoluteX = %d | \"middle\" = %.2f | resizing left: %s",
                actionArgs.startX, absoluteX, middle, Boolean.toString((actionArgs.startX - absoluteX) < middle)
                ));


        actionArgs.resizingLeft = (actionArgs.startX - absoluteX) < middle;
        actionArgs.initialWidth = initialWidth;
        actionArgs.initialX = enhancedDraggableBox.getX();

    }

    private static final float MIN_WIDTH = 50;
    @Override
    public void actionMove(EnhancedDraggableLayout.ActionArgs actionArgs, EnhancedDraggableLayout enhancedDraggableBox) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) enhancedDraggableBox.getLayoutParams();
        float deltaX = actionArgs.event.getRawX() - actionArgs.startX; // the distance from which they clicked

        if (actionArgs.resizingLeft) {
            int newWidth = (int) (actionArgs.initialWidth - deltaX);
            float newX = actionArgs.initialX + deltaX;

            if (newWidth > MIN_WIDTH) {
                params.width = newWidth;
                enhancedDraggableBox.setX(newX);
                enhancedDraggableBox.setLayoutParams(params);
            }
        } else {
            int newWidth = (int) (actionArgs.initialWidth + deltaX);
            if (newWidth > MIN_WIDTH) {
                params.width = newWidth;
                enhancedDraggableBox.setLayoutParams(params);
            }
        }
    }

    @Override
    public void actionUp(EnhancedDraggableLayout.ActionArgs actionArgs, EnhancedDraggableLayout enhancedDraggableBox) {
        float deltaX = actionArgs.event.getRawX() - actionArgs.startX;
        int newWidth = (int) (actionArgs.resizingLeft ? (actionArgs.initialWidth - deltaX) : (actionArgs.initialWidth + deltaX));
        enhancedDraggableBox.rnoiotsf.changeWidth(newWidth);

        efiActivityInfo.editingSaveFile.save();

        RichTextRenderer richTextRenderer = enhancedDraggableBox.richTextRenderer;
        richTextRenderer.UpdateRender();
    }

    public ResizeFunction(EFIActivityInfo efiActivityInfo) {
        this.efiActivityInfo = efiActivityInfo;
    }
}
