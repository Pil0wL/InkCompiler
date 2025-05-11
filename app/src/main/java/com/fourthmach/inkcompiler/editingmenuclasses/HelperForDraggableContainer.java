package com.fourthmach.inkcompiler.editingmenuclasses;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;

import com.fourthmach.inkcompiler.R;
import com.fourthmach.inkcompiler.SaveFileSystem.EditingSaveFile;
import com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.EnhancedDraggableLayout.DraggableBoxSettings;
import com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.EnhancedDraggableLayout.EnhancedDraggableLayout;
import com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.EnhancedEditText.EnhancedEditText;
import com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.RichTextRenderer.RichTextRenderer;

public class HelperForDraggableContainer {

    private final EFIActivityInfo efiActivityInfo;
    private final Activity currentActivity;
    private final FrameLayout parentBoxForHolder;
    private final FrameLayout containerFor_draggableBox;
    private final EditingSaveFile editingSaveFile;

    public HelperForDraggableContainer(EFIActivityInfo efiActivityInfo) {
        this.efiActivityInfo = efiActivityInfo;
        this.currentActivity = efiActivityInfo.activity;
        this.parentBoxForHolder = efiActivityInfo.parentBoxForHolder;
        this.containerFor_draggableBox = efiActivityInfo.containerFor_draggableBox;
        this.editingSaveFile = efiActivityInfo.editingSaveFile;


        for (EditingSaveFile.Note note: editingSaveFile.NoteArrayList) createNewBox(note);
    }


    private void createNewBox(EditingSaveFile.Note rnoiotsf) {
        EnhancedDraggableLayout enhancedDraggableBox = (EnhancedDraggableLayout) currentActivity.getLayoutInflater().inflate(R.layout.draggable_box, containerFor_draggableBox, false);
        enhancedDraggableBox.rnoiotsf = rnoiotsf;

        enhancedDraggableBox.actualStart(efiActivityInfo);


        enhancedDraggableBox.setX(rnoiotsf.x);
        enhancedDraggableBox.setY(rnoiotsf.y);






        String text = rnoiotsf.rawText;//"this is <u><size;10> TESTING </size><size;12>AMONGUS SO SUSSY</size> you would</u> put the rich text right over....<image;jonny;0.2;0.2>";
        EnhancedEditText editText = enhancedDraggableBox.editText;
        editText.actualStart(efiActivityInfo, enhancedDraggableBox);
        editText.setVisibility(View.INVISIBLE);
        editText.setText(text);

        containerFor_draggableBox.addView(enhancedDraggableBox);


        RichTextRenderer richTextRenderer = enhancedDraggableBox.richTextRenderer;
        richTextRenderer.actualStart(efiActivityInfo, enhancedDraggableBox);
        richTextRenderer.post(() -> { // this is where you call renderText as it expects it to be fully loaded ; also for testing
            richTextRenderer.renderText(text);
            efiActivityInfo.containerFor_draggableBox.expandByTargetBox(enhancedDraggableBox);

        });


    }
    public void addNewBox() {
        float x = 0;
        float y = 0;
        String rawText = "";

        EditingSaveFile.Note rnoiotsf = editingSaveFile.addBox(x, y, rawText);
        editingSaveFile.save();

        createNewBox(rnoiotsf);
    }


    public void changeMode(int newMode) {
        for (int i = 0; i < containerFor_draggableBox.getChildCount(); i++) {
            FrameLayout child = (FrameLayout) containerFor_draggableBox.getChildAt(i);

            View clickDetector = child.findViewById(R.id.dragboxStandardClickDetector);
            clickDetector.bringToFront();


            EnhancedEditText mainEditText = child.findViewById(R.id.dragboxMainEditTextView);
            mainEditText.manualUnFocus();
        }
        efiActivityInfo.draboxSettings.selectedMode = newMode;

    }

}
