package com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.EnhancedDraggableLayout;

import com.fourthmach.inkcompiler.editingmenuclasses.EFIActivityInfo;
import com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.standardfunctions.*;

public class DraggableBoxSettings {
    public functionbase[] createdFunctions;
    public int selectedMode = 0;

    public DraggableBoxSettings(EFIActivityInfo efiActivityInfo) {
        createdFunctions = new functionbase[] {
                new MoveFunction(efiActivityInfo),
                new ResizeFunction(efiActivityInfo),
                new EditTextFunction(efiActivityInfo),
                new MultiSelectFunction(efiActivityInfo)
        };
    }

}
