package com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.EnhancedEditText;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.fourthmach.inkcompiler.editingmenuclasses.EFIActivityInfo;
import com.fourthmach.inkcompiler.editingmenuclasses.HelperForDraggableContainer;
import com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.EnhancedDraggableLayout.EnhancedDraggableLayout;
import com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.RichTextRenderer.RichTextRenderer;

public class EnhancedEditText extends androidx.appcompat.widget.AppCompatEditText {

    public EnhancedEditText(Context context) {
        super(context);
        initial();
    }

    public EnhancedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initial();
    }

    public EnhancedEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initial();
    }

    private void initial() {
        Log.d("TextBox", "Created!");
    }

    private EFIActivityInfo efiActivityInfo;
    private EnhancedDraggableLayout enhancedDraggableBox;
    public void actualStart(EFIActivityInfo efiActivityInfo, EnhancedDraggableLayout enhancedDraggableBox) {
        this.efiActivityInfo = efiActivityInfo;
        this.enhancedDraggableBox = enhancedDraggableBox;

        EditText selfReference = this;
        selfReference.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    manualUnFocus();
                    return true; // consume event and prevent newline
                }
                return false;
            }
        });
    }



    private boolean isManuallyFocused = false;
    public void manualUnFocus() {
        if (!isManuallyFocused) return;
        isManuallyFocused = false;
        clearFocus();

        setOnFocusChangeListener(null);

        setCursorVisible(false);
        setFocusable(false);
        setFocusableInTouchMode(false);

        //setKeyListener(null);    // disables typing (important)

        // Hide keyboard
        InputMethodManager imm = (InputMethodManager) efiActivityInfo.activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindowToken(), 0);
        }


        { // bring to back
            EnhancedDraggableLayout parent = enhancedDraggableBox;
            parent.removeView(this);
            parent.addView(this, 0);
        }
        setVisibility(View.INVISIBLE);

        RichTextRenderer richTextRenderer = enhancedDraggableBox.richTextRenderer;
        richTextRenderer.setVisibility(View.VISIBLE);

        String inputted = getText().toString();
        enhancedDraggableBox.rnoiotsf.changeRawText(inputted);
        efiActivityInfo.editingSaveFile.save();

        richTextRenderer.renderText(inputted);
    }

    public void manualFocus() {
        if (isManuallyFocused) return;
        isManuallyFocused = true;

        EnhancedEditText selfReference = this;

        selfReference.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) manualUnFocus();

                //helper_DraggableContainer.expandGIfNeeded(child);
            }
        });

        selfReference.setCursorVisible(true);
        selfReference.setFocusable(true);
        selfReference.setFocusableInTouchMode(true);
        boolean success = selfReference.requestFocus();
        bringToFront();
        Log.d("TextBox", "is focus success : " + success);



        enhancedDraggableBox.richTextRenderer.setVisibility(View.INVISIBLE);
        selfReference.setVisibility(View.VISIBLE);
    }
}
