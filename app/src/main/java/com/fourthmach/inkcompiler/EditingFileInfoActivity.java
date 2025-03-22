package com.fourthmach.inkcompiler;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class EditingFileInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editing_main);

        android.content.Intent current_intent = getIntent();


        Button menuButton = findViewById(R.id.menuButton);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a PopupMenu
                PopupMenu popupMenu = new PopupMenu(EditingFileInfoActivity.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.editbuttons_main, popupMenu.getMenu());

                // Handle menu item clicks
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();

                        if (itemId == R.id.resize) {
                            // Handle Resize
                            Toast.makeText(EditingFileInfoActivity.this, "Resize clicked", Toast.LENGTH_SHORT).show();
                            return true;
                        } else if (itemId == R.id.textEdit) {
                            // Handle Text Edit
                            Toast.makeText(EditingFileInfoActivity.this, "Text Edit clicked", Toast.LENGTH_SHORT).show();
                            return true;
                        } else if (itemId == R.id.saveScreen) {
                            // Handle Save Screen
                            Toast.makeText(EditingFileInfoActivity.this, "Save Screen clicked", Toast.LENGTH_SHORT).show();
                            return true;
                        } else if (itemId == R.id.moveElement) {
                            // Handle Move Element
                            Toast.makeText(EditingFileInfoActivity.this, "Move Element clicked", Toast.LENGTH_SHORT).show();
                            return true;
                        } else if (itemId == R.id.multiSelect) {
                            // Handle Multi-Select
                            Toast.makeText(EditingFileInfoActivity.this, "Multi-Select clicked", Toast.LENGTH_SHORT).show();
                            return true;
                        } else {
                            return false;
                        }
                    }
                });

                // Show the PopupMenu
                popupMenu.show();
            }
        });
    }

}
