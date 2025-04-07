package com.fourthmach.inkcompiler.editingmenuclasses;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fourthmach.inkcompiler.CustomFrameLayout;
import com.fourthmach.inkcompiler.R;

public class DraggableBoxContainer {

    private final Activity currentActivity;
    private final FrameLayout parentBoxForHolder;
    private final FrameLayout containerFor_draggableBox;

    public DraggableBoxContainer(Activity currentActivity, FrameLayout parentBoxForHolder, FrameLayout containerFor_draggableBox) {
        this.currentActivity = currentActivity;
        this.parentBoxForHolder = parentBoxForHolder;
        this.containerFor_draggableBox = containerFor_draggableBox;


    }


    private int boxCount = 0;
    public void addNewBox() {
        CustomFrameLayout newBox = (CustomFrameLayout) currentActivity.getLayoutInflater().inflate(R.layout.draggable_box, containerFor_draggableBox, false);


        /*
        newBox.setOnResizeListener(new CustomFrameLayout.OnResizeListener() {
            @Override
            public void onResized(int newWidth, int newHeight) {
                expandGIfNeeded(newBox);
            }
        });
*/

        EditText childEditText = newBox.findViewById(R.id.childdraggableboxedittext);
        childEditText.setBackgroundColor(Color.RED);
        childEditText.setTextColor(Color.WHITE);
        childEditText.setTextSize(16);
        childEditText.setPadding(20, 20, 20, 20);

        childEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int after) {
                // Here, you can adjust the width and height based on text length
                //int newWidth = (int) (childEditText.getPaint().measureText(charSequence.toString()) + 50); // Extra padding
                //childEditText.setWidth(newWidth);

                //Log.i("MainActivity", Integer.toString(newWidth));
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Example formatted text
        String formattedText = "<font size='50'>Box " + (++boxCount) + "</font> testing tesgin";
        childEditText.setText(Html.fromHtml(formattedText, Html.FROM_HTML_MODE_LEGACY));


        if (SettingValues.isMovingElement) {
            bindDragListener(newBox);
        }

        // Add to G
        containerFor_draggableBox.addView(newBox);
    }



    // Generic function to bind a draggable listener
    @SuppressLint("ClickableViewAccessibility")
    public void bindDragListener(FrameLayout view) {
        View clickDetector = view.findViewById(R.id.transparentClickView);
        clickDetector.setOnTouchListener(new View.OnTouchListener() {
            float dX, dY, startX, startY;
            boolean isMoving = false;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        startX = event.getRawX();
                        startY = event.getRawY();
                        isMoving = false;
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        float newX = event.getRawX() + dX;
                        float newY = event.getRawY() + dY;
                        view.setX(newX);
                        view.setY(newY);

                        if (Math.abs(event.getRawX() - startX) > 10 || Math.abs(event.getRawY() - startY) > 10) {
                            isMoving = true;
                        }
                        return true;

                    case MotionEvent.ACTION_UP:
                        if (!isMoving) {
                            view.performClick();
                        }

                        expandGIfNeeded(view);
                        return true;
                }
                return false;
            }
        });
    }

    public void expandGIfNeeded(FrameLayout newBox) {
        ViewGroup.MarginLayoutParams GParams = (ViewGroup.MarginLayoutParams) containerFor_draggableBox.getLayoutParams();

        int GWidth = containerFor_draggableBox.getWidth();
        int GHeight = containerFor_draggableBox.getHeight();

        /*
        int GLeft = (int) containerFor_draggableBox.getX();
        int GTop = (int) containerFor_draggableBox.getY();
        int GRight = GLeft + GWidth;
        int GBottom = GTop + GHeight;
        Toast.makeText(currentActivity, String.format("GLeft = %s, GTop = %s, boxLeft = %s, boxTop = %s", GLeft, GTop, boxLeft, boxTop), Toast.LENGTH_SHORT).show();


         */
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
}
