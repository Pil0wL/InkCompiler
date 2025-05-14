package com.fourthmach.inkcompiler.editingmenuclasses.editorbuttons;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fourthmach.inkcompiler.MainActivity;
import com.fourthmach.inkcompiler.R;
import com.fourthmach.inkcompiler.editingmenuclasses.EFIActivityInfo;

public class EditMainButtons {


    private static final String[] editButtonLabels = {"Resize", "Text Edit", "Move Screen", "Move Element", "Multi-Select"};

    private final Activity currentActivity;
    private final RelativeLayout overlayButtonsContainer;

    public final Button AddButton;
    public final Button RemoveButton;
    public final Button SettingsButton;
    public EditMainButtons(EFIActivityInfo efiActivityInfo) {
        this.currentActivity = efiActivityInfo.activity;
        this.overlayButtonsContainer = efiActivityInfo.overlayButtonsContainer;

        CreateEditorButtons();
        bindEditMenuButtons();

        AddButton = overlayButtonsContainer.findViewById(R.id.editmenu_add_button);
        RemoveButton = overlayButtonsContainer.findViewById(R.id.editmenu_remove_button);
        SettingsButton = overlayButtonsContainer.findViewById(R.id.editmenu_settings_button);
    }

    public Button[] editor_buttons;
    private void CreateEditorButtons() {
        editor_buttons = new Button[editButtonLabels.length]; // List to store button references

        LinearLayout editor_buttonContainer = overlayButtonsContainer.findViewById(R.id.editors_buttonContainer);
        LayoutInflater inflater = LayoutInflater.from(currentActivity);
        {
            int i = 0;
            for (String label : editButtonLabels) {
                Button button = (Button) inflater.inflate(R.layout.editingbutton_item, editor_buttonContainer, false);
                button.setText(label);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(currentActivity, ((Button) v).getText() + " clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                editor_buttons[i] = button;
                i++;


                // Add margin between buttons (optional)
                if (editor_buttonContainer.getChildCount() > 1) {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) button.getLayoutParams();
                    params.setMargins(0, 0, 8, 0); // 8dp margin to the right
                    button.setLayoutParams(params);
                }


                // Add the button to the container
                editor_buttonContainer.addView(button);
            }
        }
    }



    private void bindEditMenuButtons() {
        overlayButtonsContainer.findViewById(R.id.editmenu_home_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(currentActivity, MainActivity.class);

                // Clear the back stack so Activity B and C are removed
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                // Start Activity A
                currentActivity.startActivity(intent);
                currentActivity.finish();
            }
        });
    }


}
