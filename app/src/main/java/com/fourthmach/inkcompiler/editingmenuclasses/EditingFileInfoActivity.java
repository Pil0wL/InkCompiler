package com.fourthmach.inkcompiler.editingmenuclasses;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fourthmach.inkcompiler.R;

import org.w3c.dom.Text;

public class EditingFileInfoActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editing_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        android.content.Intent current_intent = getIntent();

        RelativeLayout overlay = findViewById(R.id.editing_main_overlay);
        FrameLayout draggableboxcontainer = findViewById(R.id.editing_main_draggablecontainer);
        FrameLayout maindraggableboxcontainer = draggableboxcontainer.findViewById(R.id.maindraggableboxcontainer);


        new UpDownArrows(overlay);
        EditMainButtons buttons = new EditMainButtons(this, overlay);
        DraggableBoxContainer dbc_functions = new DraggableBoxContainer(this, maindraggableboxcontainer);

        buttons.AddButton.setOnClickListener(v -> dbc_functions.addNewBox());
        buttons.editor_buttons[2].setOnClickListener(new View.OnClickListener() {
            float lastTouchX, lastTouchY;
            boolean isDragging = false;


            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {
                SettingValues.isMovingElement = false;
                for (int i = 0; i < maindraggableboxcontainer.getChildCount(); i++) {
                    View child = maindraggableboxcontainer.getChildAt(i);
                    child.setOnTouchListener(null);
                }
                draggableboxcontainer.setOnTouchListener(new View.OnTouchListener() {
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

                                    maindraggableboxcontainer.setX(maindraggableboxcontainer.getX() + dx);
                                    maindraggableboxcontainer.setY(maindraggableboxcontainer.getY() + dy);

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
        buttons.editor_buttons[3].setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {
                SettingValues.isMovingElement = true;
                for (int i = 0; i < maindraggableboxcontainer.getChildCount(); i++) {
                    TextView child = (TextView) maindraggableboxcontainer.getChildAt(i);
                    DraggableBoxContainer.bindDragListener(maindraggableboxcontainer, child);
                }
                draggableboxcontainer.setOnTouchListener(null);
            }
        });
    }







}
