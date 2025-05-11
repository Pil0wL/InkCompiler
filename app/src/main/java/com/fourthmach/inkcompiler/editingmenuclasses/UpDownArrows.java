package com.fourthmach.inkcompiler.editingmenuclasses;

import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fourthmach.inkcompiler.R;

public class UpDownArrows {


    private ImageView editor_arrowButton;
    private HorizontalScrollView editor_buttonScrollView;

    private ImageView editmenu_arrowButton;
    private HorizontalScrollView editmenu_buttonScrollView;


    boolean isEditorButtonsExpanded = false;
    public UpDownArrows(EFIActivityInfo efiActivityInfo) {
        RelativeLayout overlayButtonsContainer = efiActivityInfo.overlayButtonsContainer;

        editor_arrowButton = overlayButtonsContainer.findViewById(R.id.editors_arrowButton);
        editor_buttonScrollView = overlayButtonsContainer.findViewById(R.id.editors_buttonScrollView);

        isEditorButtonsExpanded = true;
        toggleEditorButtons();
        editor_arrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleEditorButtons();
            }
        });


        editmenu_arrowButton = overlayButtonsContainer.findViewById(R.id.editmenu_arrowButton);
        editmenu_buttonScrollView = overlayButtonsContainer.findViewById(R.id.editmenu_buttonScrollView);

        isEditMenuButtonsExpanded = true;
        toggleEditMenuButtons();
        editmenu_arrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleEditMenuButtons();
            }
        });
    }
    private void toggleEditorButtons() {
        RelativeLayout.LayoutParams relative_params = (RelativeLayout.LayoutParams) editor_arrowButton.getLayoutParams();
        isEditorButtonsExpanded = !isEditorButtonsExpanded; // Toggle state
        if (isEditorButtonsExpanded) {
            editor_buttonScrollView.setVisibility(View.VISIBLE);

            relative_params.addRule(RelativeLayout.ABOVE, R.id.editors_buttonScrollView);
            relative_params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            editor_arrowButton.setImageResource(R.drawable.editing_buttons_down);
        } else {
            editor_buttonScrollView.setVisibility(View.INVISIBLE);


            relative_params.removeRule(RelativeLayout.ABOVE);
            relative_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            editor_arrowButton.setImageResource(R.drawable.editing_buttons_up);
        }
        editor_arrowButton.setLayoutParams(relative_params);
    }



    boolean isEditMenuButtonsExpanded = false;
    private void toggleEditMenuButtons() {
        RelativeLayout.LayoutParams relative_params = (RelativeLayout.LayoutParams) editmenu_arrowButton.getLayoutParams();
        isEditMenuButtonsExpanded = !isEditMenuButtonsExpanded; // Toggle state
        if (isEditMenuButtonsExpanded) {
            editmenu_buttonScrollView.setVisibility(View.VISIBLE);

            relative_params.addRule(RelativeLayout.BELOW, R.id.editmenu_buttonScrollView);
            relative_params.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
            editmenu_arrowButton.setImageResource(R.drawable.editing_buttons_up);
        } else {
            editmenu_buttonScrollView.setVisibility(View.INVISIBLE);


            relative_params.removeRule(RelativeLayout.BELOW);
            relative_params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            editmenu_arrowButton.setImageResource(R.drawable.editing_buttons_down);
        }
        editmenu_arrowButton.setLayoutParams(relative_params);

    }


}
