package com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.standardfunctions;

import com.fourthmach.inkcompiler.editingmenuclasses.EFIActivityInfo;
import com.fourthmach.inkcompiler.editingmenuclasses.HelperForDraggableContainer;
import com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.EnhancedDraggableLayout.EnhancedDraggableLayout;

import java.util.ArrayList;

public class MoveFunction extends functionbase {


    private EFIActivityInfo efiActivityInfo;
    @Override
    public void actionMove(EnhancedDraggableLayout.ActionArgs actionArgs, EnhancedDraggableLayout enhancedDraggableBox) {
        float rawX = actionArgs.event.getRawX();
        float rawY = actionArgs.event.getRawY();
        {
            float newX = rawX - actionArgs.dX;
            float newY = rawY - actionArgs.dY;
            enhancedDraggableBox.setX(newX);
            enhancedDraggableBox.setY(newY);

            enhancedDraggableBox.rnoiotsf.changeXY(newX, newY);
        }

        for (EnhancedDraggableLayout.ActionArgs.initialSelectedInfo info: actionArgs.selectedDraBoxesInfo) {
            float newX = rawX - info.dX;
            float newY = rawY - info.dY;
            EnhancedDraggableLayout mainBody = info.mainBody;
            mainBody.setX(newX);
            mainBody.setY(newY);

            mainBody.rnoiotsf.changeXY(newX, newY);
        }
    }
    @Override
    public void actionUp(EnhancedDraggableLayout.ActionArgs actionArgs, EnhancedDraggableLayout enhancedDraggableBox) {
        enhancedDraggableBox.rnoiotsf.finalizeChangeXY();

        for
        (EnhancedDraggableLayout.ActionArgs.initialSelectedInfo info: actionArgs.selectedDraBoxesInfo)
            info.mainBody.rnoiotsf.finalizeChangeXY();

        efiActivityInfo.editingSaveFile.save();
    }


    public MoveFunction(EFIActivityInfo efiActivityInfo) {
        this.efiActivityInfo = efiActivityInfo;
    }
}
