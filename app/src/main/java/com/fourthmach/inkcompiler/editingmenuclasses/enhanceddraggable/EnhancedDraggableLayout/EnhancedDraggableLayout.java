package com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.EnhancedDraggableLayout;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.fourthmach.inkcompiler.R;
import com.fourthmach.inkcompiler.SaveFileSystem.EditingSaveFile;
import com.fourthmach.inkcompiler.editingmenuclasses.EFIActivityInfo;
import com.fourthmach.inkcompiler.editingmenuclasses.HelperForDraggableContainer;
import com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.EnhancedEditText.EnhancedEditText;
import com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.RichTextRenderer.RichTextRenderer;
import com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.standardfunctions.functionbase;

import java.util.ArrayList;

public class EnhancedDraggableLayout extends FrameLayout {

    public EnhancedDraggableLayout(Context context) {
        super(context);
    }

    public EnhancedDraggableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EnhancedDraggableLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }



    public class ActionArgs {
        public float dX, dY, startX, startY;
        public MotionEvent event;


        // set and only used by the resize function
        public float initialWidth;
        public float initialX;
        public boolean resizingLeft;

        // only used by the move function
        public ArrayList<initialSelectedInfo> selectedDraBoxesInfo;
        public class initialSelectedInfo {
            public float dX;
            public float dY;

            public EnhancedDraggableLayout mainBody;
        }
    }


    public RichTextRenderer richTextRenderer;
    public EnhancedEditText editText;
    public View clickDetector;
    public View selectionBorder;

    public EditingSaveFile.Note rnoiotsf;

    private void onCreate() {
        editText = findViewById(R.id.dragboxMainEditTextView);
        editText.setTextColor(Color.WHITE);
        editText.setTextSize(16);
        editText.setPadding(20, 20, 20, 20);

        richTextRenderer = findViewById(R.id.dragboxRichTextRenderer);
        clickDetector = findViewById(R.id.dragboxStandardClickDetector);

        selectionBorder = findViewById(R.id.dragboxSelectedBorder);
        selectionBorder.setVisibility(View.INVISIBLE);
    }

    EFIActivityInfo efiActivityInfo;
    public void actualStart(EFIActivityInfo efiActivityInfo) {
        this.efiActivityInfo = efiActivityInfo;

        onCreate();

        DraggableBoxSettings draboxSettings = efiActivityInfo.draboxSettings;
        EnhancedDraggableLayout mainBody = this;


        clickDetector.setOnTouchListener(new View.OnTouchListener() {
            ActionArgs actionArgs;
            functionbase selectedFunction;
            boolean isMoving = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (draboxSettings.selectedMode == -1) return false;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        actionArgs = new ActionArgs();

                        float startX = event.getRawX()              ;
                        float startY = event.getRawY()              ;

                        actionArgs.startX = startX                  ;
                        actionArgs.startY = startY                  ;
                        actionArgs.dX = startX - mainBody.getX()    ;
                        actionArgs.dY = startY - mainBody.getY()    ;
                        actionArgs.event = event                    ;
                        {
                            ArrayList<ActionArgs.initialSelectedInfo> newArray = new ArrayList<>();
                            for (EnhancedDraggableLayout selectedDraBox: efiActivityInfo.selectedDraBoxes) {
                                ActionArgs.initialSelectedInfo info = actionArgs.new initialSelectedInfo();
                                info.dX = startX - selectedDraBox.getX();
                                info.dY = startY - selectedDraBox.getY();
                                info.mainBody = selectedDraBox;

                                newArray.add(info);
                            }
                            actionArgs.selectedDraBoxesInfo = newArray;
                        }
                        selectedFunction = draboxSettings.createdFunctions[draboxSettings.selectedMode];
                        selectedFunction.actionDown(actionArgs, mainBody);

                        isMoving = false;
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        if (Math.abs(event.getRawX() - actionArgs.startX) > 10 || Math.abs(event.getRawY() - actionArgs.startY) > 10) {
                            isMoving = true;
                        }

                        selectedFunction.actionMove(actionArgs, mainBody);
                        return true;

                    case MotionEvent.ACTION_UP:
                        if (!isMoving) { // the user did not drag outside a 20x20 box from where they started
                            mainBody.performClick();
                        }

                        selectedFunction.actionUp(actionArgs, mainBody);

                        actionArgs = null;
                        selectedFunction = null;
                        efiActivityInfo.containerFor_draggableBox.expandByTargetBox(mainBody);
                        return true;
                }
                return false;
            }
        });
    }
    public void updateHeight(int newHeight) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
        params.height = newHeight;
        setLayoutParams(params);
    }


}