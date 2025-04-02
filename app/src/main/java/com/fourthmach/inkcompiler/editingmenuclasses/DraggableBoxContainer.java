package com.fourthmach.inkcompiler.editingmenuclasses;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DraggableBoxContainer {

    private final Activity currentActivity;
    private final FrameLayout draggableboxcontainer;

    public DraggableBoxContainer(Activity currentActivity, FrameLayout draggableboxcontainer) {
        this.currentActivity = currentActivity;
        this.draggableboxcontainer = draggableboxcontainer;

    }


    private int boxCount = 0;
    public void addNewBox() {
        TextView newBox = new TextView(currentActivity);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(150, 150);
        newBox.setLayoutParams(params);
        newBox.setBackgroundColor(Color.RED);
        newBox.setTextColor(Color.WHITE);
        newBox.setTextSize(16);
        newBox.setPadding(20, 20, 20, 20);

        // Example formatted text
        String formattedText = "<font size='50'>Box " + (++boxCount) + "</font> testing tesgin";
        newBox.setText(Html.fromHtml(formattedText, Html.FROM_HTML_MODE_LEGACY));

        // Set a unique ID
        newBox.setId(View.generateViewId());

        if (SettingValues.isMovingElement) {
            bindDragListener(draggableboxcontainer, newBox);
        }

        // Add to G
        draggableboxcontainer.addView(newBox);
    }



    // Generic function to bind a draggable listener
    @SuppressLint("ClickableViewAccessibility")
    public static void bindDragListener(FrameLayout draggableboxcontainer, TextView view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            float dX, dY, startX, startY;
            boolean isMoving = false;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = v.getX() - event.getRawX();
                        dY = v.getY() - event.getRawY();
                        startX = event.getRawX();
                        startY = event.getRawY();
                        isMoving = false;
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        float newX = event.getRawX() + dX;
                        float newY = event.getRawY() + dY;
                        v.setX(newX);
                        v.setY(newY);

                        if (Math.abs(event.getRawX() - startX) > 10 || Math.abs(event.getRawY() - startY) > 10) {
                            isMoving = true;
                        }
                        return true;

                    case MotionEvent.ACTION_UP:
                        if (!isMoving) {
                            v.performClick();
                        }

                        expandGIfNeeded(draggableboxcontainer, view);
                        return true;
                }
                return false;
            }
        });
    }

    private static void expandGIfNeeded(FrameLayout G, TextView newBox) {
        ViewGroup.MarginLayoutParams GParams = (ViewGroup.MarginLayoutParams) G.getLayoutParams();

        int GWidth = G.getWidth();
        int GHeight = G.getHeight();

        /*
        int GLeft = (int) G.getX();
        int GTop = (int) G.getY();
        int GRight = GLeft + GWidth;
        int GBottom = GTop + GHeight;
        Toast.makeText(currentActivity, String.format("GLeft = %s, GTop = %s, boxLeft = %s, boxTop = %s", GLeft, GTop, boxLeft, boxTop), Toast.LENGTH_SHORT).show();


         */

        int boxLeft = (int) newBox.getX();
        int boxTop = (int) newBox.getY();
        int boxWidth = newBox.getWidth();
        int boxHeight = newBox.getHeight();
        int boxRight = boxLeft + boxWidth;
        int boxBottom = boxTop + boxHeight;


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
            G.setX(G.getX() + (int) ((double) moveX * 0.5));
            G.setY(G.getY() + (int) ((double) moveY * 0.5));

            for (int i = 0; i < G.getChildCount(); i++) {
                View child = G.getChildAt(i);
                child.setX(child.getX() + boxMoveX);
                child.setY(child.getY() + boxMoveY);
            }
            G.setLayoutParams(GParams);
        }

    }
}
