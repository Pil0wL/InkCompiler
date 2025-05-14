package com.fourthmach.inkcompiler.editingmenuclasses;

import android.app.Activity;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.fourthmach.inkcompiler.SaveFileSystem.EditingSaveFile;
import com.fourthmach.inkcompiler.editingmenuclasses.MainDraBoxContainer.MainDraBoxContainer;
import com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.EnhancedDraggableLayout.DraggableBoxSettings;
import com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.EnhancedDraggableLayout.EnhancedDraggableLayout;

import java.util.ArrayList;

public class EFIActivityInfo {

    public Activity activity;
    public RelativeLayout overlayButtonsContainer;
    public FrameLayout parentBoxForHolder;
    public MainDraBoxContainer containerFor_draggableBox;
    public EditingSaveFile editingSaveFile;

    public DraggableBoxSettings draboxSettings;

    public ArrayList<EnhancedDraggableLayout> selectedDraBoxes = new ArrayList<>();

}
