package com.fourthmach.inkcompiler.editingmenuclasses;

import static com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.helperFunctions.helperFunctions.safeStringToFloat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.fragment.app.Fragment;

import com.fourthmach.inkcompiler.R;
import com.fourthmach.inkcompiler.SaveFileSystem.EditingSaveFile;
import com.fourthmach.inkcompiler.editingmenuclasses.MainDraBoxContainer.MainDraBoxContainer;
import com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.EnhancedDraggableLayout.EnhancedDraggableLayout;

public class SettingsFragment extends Fragment {

    public SettingsFragment() {}

    public EFIActivityInfo efiActivityInfo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.default_note_settings, container, false);


        EditingSaveFile.NoteSettings noteSettings = efiActivityInfo.editingSaveFile.noteSettings;


        EditText inputTextSize = view.findViewById(R.id.noteSettingsInputTextSize);
        inputTextSize.setText(Float.toString(efiActivityInfo.editingSaveFile.noteSettings.textSize));
        inputTextSize.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String textSize = inputTextSize.getText().toString();
                    float converted = safeStringToFloat(textSize, 16f);

                    noteSettings.setTextSize(converted);
                }
            }
        });

        Switch switchBold = view.findViewById(R.id.noteSettingsSwitchBold);
        Switch switchItalic = view.findViewById(R.id.noteSettingsSwitchItalic);
        Switch switchUnderline = view.findViewById(R.id.noteSettingsSwitchUnderline);



        switchBold.setChecked(noteSettings.bold);
        switchItalic.setChecked(noteSettings.italic);
        switchUnderline.setChecked(noteSettings.underline);

        switchBold.setOnCheckedChangeListener((buttonView, isChecked) -> noteSettings.setBold(isChecked));
        switchItalic.setOnCheckedChangeListener((buttonView, isChecked) -> noteSettings.setItalic(isChecked));
        switchUnderline.setOnCheckedChangeListener((buttonView, isChecked) -> noteSettings.setUnderline(isChecked));




        ImageButton btnBack = view.findViewById(R.id.noteSettingsBtnBack);
        btnBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();

            MainDraBoxContainer containerdwayne = efiActivityInfo.containerFor_draggableBox;
            for (int i = 0; i < containerdwayne.getChildCount(); i++) {
                EnhancedDraggableLayout child = (EnhancedDraggableLayout) containerdwayne.getChildAt(i);
                child.richTextRenderer.reload();


                efiActivityInfo.containerFor_draggableBox.expandByTargetBox(child);
            }
            Log.d("IMPORTANT NOTICE", "efiActivityInfo.containerFor_draggableBox.expandByTargetBox(child); calling it on every single new box is highly inneficient please make a new function that is more optimized");
            efiActivityInfo.editingSaveFile.save();
        });

        return view;
    }

}
