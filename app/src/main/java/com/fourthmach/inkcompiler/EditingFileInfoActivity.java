package com.fourthmach.inkcompiler;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditingFileInfoActivity extends AppCompatActivity {



    private ImageView editor_arrowButton;
    private HorizontalScrollView editor_buttonScrollView;

    private ImageView editmenu_arrowButton;
    private HorizontalScrollView editmenu_buttonScrollView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editing_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        android.content.Intent current_intent = getIntent();

        RelativeLayout overlay = findViewById(R.id.editing_main_overlay);

        // create the bottom buttons
        bindEditorButtons(overlay);

        // create the top buttons
        bindEditMenuButtons(overlay);










        editor_arrowButton = overlay.findViewById(R.id.editors_arrowButton);
        editor_buttonScrollView = overlay.findViewById(R.id.editors_buttonScrollView);

        isEditorButtonsExpanded = true;
        toggleEditorButtons();
        editor_arrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleEditorButtons();
            }
        });


        editmenu_arrowButton = overlay.findViewById(R.id.editmenu_arrowButton);
        editmenu_buttonScrollView = overlay.findViewById(R.id.editmenu_buttonScrollView);

        isEditMenuButtonsExpanded = true;
        toggleEditMenuButtons();
        editmenu_arrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleEditMenuButtons();
            }
        });




    }





    private static final String[] editButtonLabels = {"Resize", "Text Edit", "Move Screen", "Move Element", "Multi-Select"};
    private void bindEditorButtons(RelativeLayout overlay) {
        Button[] edit_buttons = new Button[editButtonLabels.length]; // List to store button references

        LinearLayout editor_buttonContainer = overlay.findViewById(R.id.editors_buttonContainer);
        LayoutInflater inflater = LayoutInflater.from(this);
        {
            int i = 0;
            for (String label : editButtonLabels) {
                Button button = (Button) inflater.inflate(R.layout.editingbutton_item, editor_buttonContainer, false);
                button.setText(label);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(EditingFileInfoActivity.this, ((Button) v).getText() + " clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                edit_buttons[i] = button;
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
        edit_buttons[0].setText("Updated Button 1");

        edit_buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditingFileInfoActivity.this, ((Button) v).getText() + " jaja", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void bindEditMenuButtons(RelativeLayout overlay) {
        overlay.findViewById(R.id.editmenu_home_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditingFileInfoActivity.this, MainActivity.class);

                // Clear the back stack so Activity B and C are removed
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                // Start Activity A
                startActivity(intent);
                finish();
            }
        });
    }





    boolean isEditorButtonsExpanded = false;
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
