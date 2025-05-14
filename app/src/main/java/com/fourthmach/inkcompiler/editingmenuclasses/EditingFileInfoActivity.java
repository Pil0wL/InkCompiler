package com.fourthmach.inkcompiler.editingmenuclasses;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.fourthmach.inkcompiler.R;
import com.fourthmach.inkcompiler.SaveFileSystem.EditingSaveFile;
import com.fourthmach.inkcompiler.SaveFileSystem.SaveFileManager;
import com.fourthmach.inkcompiler.editingmenuclasses.MainDraBoxContainer.MainDraBoxContainer;
import com.fourthmach.inkcompiler.editingmenuclasses.editorbuttons.*;
import com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.EnhancedDraggableLayout.DraggableBoxSettings;

public class EditingFileInfoActivity extends AppCompatActivity {





    private RelativeLayout overlayButtonsContainer;
    private FrameLayout parentBoxForHolder;
    private MainDraBoxContainer containerFor_draggableBox;
    private Activity selfreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editing_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        overlayButtonsContainer = findViewById(R.id.editing_main_overlay);
        parentBoxForHolder = findViewById(R.id.editing_main_draggablecontainer);
        containerFor_draggableBox = parentBoxForHolder.findViewById(R.id.maindraggableboxcontainer);
        View screenDragClickDetector = findViewById(R.id.editingmain_dragscreen);
        selfreference = this;

        EFIActivityInfo efiActivityInfo = new EFIActivityInfo();
        efiActivityInfo.activity = this;
        efiActivityInfo.overlayButtonsContainer = overlayButtonsContainer;
        efiActivityInfo.parentBoxForHolder = parentBoxForHolder;
        efiActivityInfo.containerFor_draggableBox = containerFor_draggableBox;
        efiActivityInfo.editingSaveFile = new EditingSaveFile(SaveFileManager.beholdedShallowSaveFile); SaveFileManager.beholdedShallowSaveFile = null;
        efiActivityInfo.draboxSettings = new DraggableBoxSettings(efiActivityInfo);
        efiActivityInfo.screenDragClickDetector = screenDragClickDetector;

        containerFor_draggableBox.actualStart(efiActivityInfo);


        new UpDownArrows(efiActivityInfo);
        EditMainButtons buttons = new EditMainButtons(efiActivityInfo);
        HelperForDraggableContainer helper_DraggableContainer = new HelperForDraggableContainer(efiActivityInfo);


        buttons.AddButton.setOnClickListener(v -> helper_DraggableContainer.addNewBox());
        buttons.RemoveButton.setOnClickListener(v -> helper_DraggableContainer.removeSelectedBoxes());
        buttons.SettingsButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager(); // if in Activity
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                SettingsFragment settingsFragment = new SettingsFragment();
                settingsFragment.efiActivityInfo = efiActivityInfo;

                transaction.replace(R.id.note_settings_fragment_container, settingsFragment);
                transaction.addToBackStack(null); // So back button works
                transaction.commit();

            }
        });


        // Resize
        buttons.editor_buttons[0].setOnClickListener(v -> helper_DraggableContainer.changeMode(1));

        // text Edit
        buttons.editor_buttons[1].setOnClickListener(v -> helper_DraggableContainer.changeMode(2));


        // Move Screen
        buttons.editor_buttons[2].setOnClickListener(new View.OnClickListener() {
            float lastTouchX, lastTouchY;
            boolean isDragging = false;


            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {
                helper_DraggableContainer.changeMode(-1);
                screenDragClickDetector.setOnTouchListener(new View.OnTouchListener() {
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
        buttons.editor_buttons[3].setOnClickListener(v -> helper_DraggableContainer.changeMode(0));

        // Move Element
        buttons.editor_buttons[4].setOnClickListener(v -> helper_DraggableContainer.changeMode(3));
    }







}
