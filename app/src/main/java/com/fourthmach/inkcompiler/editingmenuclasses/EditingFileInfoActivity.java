package com.fourthmach.inkcompiler.editingmenuclasses;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fourthmach.inkcompiler.R;
import com.fourthmach.inkcompiler.editingmenuclasses.editorbuttons.*;

public class EditingFileInfoActivity extends AppCompatActivity {


    private static int MIN_WIDTH = 50;

    private RelativeLayout overlay;
    private FrameLayout parentBoxForHolder;
    private FrameLayout containerFor_draggableBox;

    private Activity selfreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editing_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        android.content.Intent current_intent = getIntent();

        overlay = findViewById(R.id.editing_main_overlay);
        parentBoxForHolder = findViewById(R.id.editing_main_draggablecontainer);
        containerFor_draggableBox = parentBoxForHolder.findViewById(R.id.maindraggableboxcontainer);
        selfreference = this;


        new UpDownArrows(overlay);
        EditMainButtons buttons = new EditMainButtons(this, overlay);
        DraggableBoxContainer draggableBoxes_controller = new DraggableBoxContainer(this, parentBoxForHolder, containerFor_draggableBox);


        buttons.AddButton.setOnClickListener(v -> draggableBoxes_controller.addNewBox());


        // Resize
        buttons.editor_buttons[0].setOnClickListener(new View.OnClickListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {
                unbindAll();
                for (int i = 0; i < containerFor_draggableBox.getChildCount(); i++) {
                    FrameLayout child = (FrameLayout) containerFor_draggableBox.getChildAt(i);
                    View clickDetector = child.findViewById(R.id.transparentClickView);
                    clickDetector.setOnTouchListener(new View.OnTouchListener() {
                        float initialX;
                        float initialRawX;
                        int initialWidth;
                        boolean resizingLeft = false;
                        boolean resizingRight = false;
                        final int edgePadding = 40; // Sensitivity to edges (in pixels)

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {

                            switch (event.getActionMasked()) {
                                case MotionEvent.ACTION_DOWN:
                                    initialX = child.getX();
                                    initialRawX = event.getRawX();
                                    initialWidth = child.getWidth();

                                    float touchX = event.getX();

                                    if (touchX < edgePadding) {
                                        resizingLeft = true;
                                        return true;
                                    } else if (touchX > child.getWidth() - edgePadding) {
                                        resizingRight = true;
                                        return true;
                                    }
                                    return false;

                                case MotionEvent.ACTION_MOVE:
                                    Log.d("TouchEvent", "Action: inside jaja");
                                    float deltaX = event.getRawX() - initialRawX;

                                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) child.getLayoutParams();

                                    if (resizingRight) {
                                        int newWidth = (int) (initialWidth + deltaX);
                                        if (newWidth > MIN_WIDTH) {
                                            params.width = newWidth;
                                            child.setLayoutParams(params);
                                        }
                                    } else if (resizingLeft) {
                                        int newWidth = (int) (initialWidth - deltaX);
                                        float newX = initialX + deltaX;

                                        if (newWidth > MIN_WIDTH) {
                                            params.width = newWidth;
                                            child.setX(newX);
                                            child.setLayoutParams(params);
                                        }
                                    }
                                    return true;

                                case MotionEvent.ACTION_UP:

                                    draggableBoxes_controller.expandGIfNeeded(child);
                                case MotionEvent.ACTION_CANCEL:
                                    resizingLeft = false;
                                    resizingRight = false;
                                    return false;
                            }
                            return false;
                        }
                    });
                }


            }

        });

        // text Edit
        buttons.editor_buttons[1].setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {
                unbindAll();

                SettingValues.isMovingElement = true;
                for (int i = 0; i < containerFor_draggableBox.getChildCount(); i++) {
                    FrameLayout child = (FrameLayout) containerFor_draggableBox.getChildAt(i);
                    EditText childEditText = child.findViewById(R.id.childdraggableboxedittext);
                    childEditText.bringToFront();

                    childEditText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("testing", "bwahg");
                            childEditText.setCursorVisible(true);
                            childEditText.setFocusable(true);
                            childEditText.setFocusableInTouchMode(true);
                            childEditText.requestFocus();
                            childEditText.setKeyListener(new EditText(selfreference).getKeyListener());

                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            if (imm != null) {
                                imm.showSoftInput(childEditText, InputMethodManager.SHOW_IMPLICIT);
                            }
                        }
                    });
                    childEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                childEditText.setCursorVisible(false);
                                childEditText.setFocusable(false);
                                childEditText.setFocusableInTouchMode(false);

                                childEditText.setLongClickable(false); // disables selection
                                childEditText.setKeyListener(null);    // disables typing (important)
                            }

                            draggableBoxes_controller.expandGIfNeeded(child);
                        }
                    });
                }
            }
        });

        // Move Screen
        buttons.editor_buttons[2].setOnClickListener(new View.OnClickListener() {
            float lastTouchX, lastTouchY;
            boolean isDragging = false;


            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {
                unbindAll();
                SettingValues.isMovingElement = false;
                parentBoxForHolder.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                lastTouchX = event.getRawX();
                                lastTouchY = event.getRawY();
                                isDragging = true;
                                return true; // Start tracking touch

                            case MotionEvent.ACTION_MOVE:
                                if (isDragging) {
                                    float dx = event.getRawX() - lastTouchX;
                                    float dy = event.getRawY() - lastTouchY;

                                    containerFor_draggableBox.setX(containerFor_draggableBox.getX() + dx);
                                    containerFor_draggableBox.setY(containerFor_draggableBox.getY() + dy);

                                    lastTouchX = event.getRawX();
                                    lastTouchY = event.getRawY();
                                }
                                return true;

                            case MotionEvent.ACTION_UP:
                                isDragging = false; // Stop dragging
                                return true;
                        }
                        return false;
                    }
                });

            }
        });

        // Move Element
        buttons.editor_buttons[3].setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {
                unbindAll();

                SettingValues.isMovingElement = true;
                for (int i = 0; i < containerFor_draggableBox.getChildCount(); i++) {
                    FrameLayout child = (FrameLayout) containerFor_draggableBox.getChildAt(i);
                    draggableBoxes_controller.bindDragListener(child);
                }
            }
        });
    }


    @SuppressLint("ClickableViewAccessibility")
    private void unbindAll() {
        parentBoxForHolder.setOnTouchListener(null);

        for (int i = 0; i < containerFor_draggableBox.getChildCount(); i++) {
            FrameLayout child = (FrameLayout) containerFor_draggableBox.getChildAt(i);

            View clickDetector = child.findViewById(R.id.transparentClickView);
            clickDetector.setOnTouchListener(null);
            clickDetector.setOnClickListener(null);
            clickDetector.setOnFocusChangeListener(null);
            clickDetector.bringToFront();

            EditText childEditText = child.findViewById(R.id.childdraggableboxedittext);
            childEditText.setCursorVisible(false);
            childEditText.setFocusable(false);
            childEditText.setFocusableInTouchMode(false);
            childEditText.setLongClickable(false); // disables selection
            childEditText.setKeyListener(null);    // disables typing (important)
        }
    }






}
