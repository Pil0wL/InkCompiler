package com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.standardfunctions;

import android.view.View;

import com.fourthmach.inkcompiler.editingmenuclasses.EFIActivityInfo;
import com.fourthmach.inkcompiler.editingmenuclasses.HelperForDraggableContainer;
import com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.EnhancedDraggableLayout.EnhancedDraggableLayout;

import java.util.ArrayList;

public class MultiSelectFunction extends functionbase {


    private EFIActivityInfo efiActivityInfo;
    @Override
    public void actionUp(EnhancedDraggableLayout.ActionArgs actionArgs, EnhancedDraggableLayout enhancedDraggableBox) {
        ArrayList<EnhancedDraggableLayout> selectedDraBoxes = efiActivityInfo.selectedDraBoxes;

        if (selectedDraBoxes.contains(enhancedDraggableBox)) {
            enhancedDraggableBox.selectionBorder.setVisibility(View.INVISIBLE);
            selectedDraBoxes.remove(enhancedDraggableBox);
        }
        else {
            enhancedDraggableBox.selectionBorder.setVisibility(View.VISIBLE);
            selectedDraBoxes.add(enhancedDraggableBox);
        }
    }


    public MultiSelectFunction(EFIActivityInfo efiActivityInfo) {
        this.efiActivityInfo = efiActivityInfo;
    }
}
