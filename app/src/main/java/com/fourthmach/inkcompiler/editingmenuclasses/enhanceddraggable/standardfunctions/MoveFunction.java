package com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.standardfunctions;

import com.fourthmach.inkcompiler.SaveFileSystem.EditingSaveFile;
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

        enhancedDraggableBox.setX(rawX - actionArgs.dX);
        enhancedDraggableBox.setY(rawY - actionArgs.dY);

        for (EnhancedDraggableLayout.ActionArgs.initialSelectedInfo info: actionArgs.selectedDraBoxesInfo) {
            EnhancedDraggableLayout mainBody = info.mainBody;
            mainBody.setX(rawX - info.dX);
            mainBody.setY(rawY - info.dY);
        }
    }
    @Override
    public void actionUp(EnhancedDraggableLayout.ActionArgs actionArgs, EnhancedDraggableLayout enhancedDraggableBox) {

        // difference from start
        float dsX = actionArgs.event.getRawX() - actionArgs.startX;
        float dsY = actionArgs.event.getRawY() - actionArgs.startY;
        {
            EditingSaveFile.Note rnoiotsf = enhancedDraggableBox.rnoiotsf;
            rnoiotsf.changeXY(
                    rnoiotsf.x + dsX,
                    rnoiotsf.y + dsY
            );
        }

        for (EnhancedDraggableLayout.ActionArgs.initialSelectedInfo info: actionArgs.selectedDraBoxesInfo){
            EditingSaveFile.Note rnoiotsf = info.mainBody.rnoiotsf;

            rnoiotsf.changeXY(
                    rnoiotsf.x + dsX,
                    rnoiotsf.y + dsY
            );
        }

        efiActivityInfo.editingSaveFile.save();
    }


    public MoveFunction(EFIActivityInfo efiActivityInfo) {
        this.efiActivityInfo = efiActivityInfo;
    }
}
