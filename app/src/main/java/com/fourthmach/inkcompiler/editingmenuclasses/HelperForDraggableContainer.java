package com.fourthmach.inkcompiler.editingmenuclasses;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

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


    public void removeSelectedBoxes() {

        for (EnhancedDraggableLayout selectedDraBox: efiActivityInfo.selectedDraBoxes) selectedDraBox.Destroy();
        editingSaveFile.save();
    }

    private void createNewBox(EditingSaveFile.Note rnoiotsf) {
        EnhancedDraggableLayout enhancedDraggableBox = (EnhancedDraggableLayout) currentActivity.getLayoutInflater().inflate(R.layout.draggable_box, containerFor_draggableBox, false);
        enhancedDraggableBox.rnoiotsf = rnoiotsf;

        enhancedDraggableBox.actualStart(efiActivityInfo);


        enhancedDraggableBox.setX(rnoiotsf.x);
        enhancedDraggableBox.setY(rnoiotsf.y);
        {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    rnoiotsf.width,
                    10 // will be changed
            );
            enhancedDraggableBox.setLayoutParams(params);
        }






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
        String rawText = "to start off use \"\\<\" to initiate a tag and > to close it, tags:\nb is to bold\ni is to italicize\nu is to underline\n\nthere are some special tags with parameters that are needed you can do tihs via \\<tagname;argument1;argument2;...>\nsize = set the remaining text size with the first argument as the size";

        EditingSaveFile.Note rnoiotsf = editingSaveFile.addBox(x, y, rawText);
        editingSaveFile.save();

        createNewBox(rnoiotsf);
    }


    public void changeMode(int newMode) {
        efiActivityInfo.screenDragClickDetector.setOnTouchListener(null);

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
