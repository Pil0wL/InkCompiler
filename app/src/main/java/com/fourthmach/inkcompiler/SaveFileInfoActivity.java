package com.fourthmach.inkcompiler;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SaveFileInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_file_contents_menu);

        TextView titleTextView = findViewById(R.id.item_title);
        TextView descriptionTextView = findViewById(R.id.item_description);


        android.content.Intent current_intent = getIntent();
        SaveFile save_file = current_intent.getParcelableExtra("save_file");

        // Get data from the intent
        String title = save_file.getTitle();
        String description = save_file.getDescription();

        // Set the data to the views
        titleTextView.setText(title);
        descriptionTextView.setText(description);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }



        Button editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do something when the button is clicked

                Intent intent = new Intent(SaveFileInfoActivity.this, EditingFileInfoActivity.class);
                intent.putExtra("save_file", save_file);


                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) { // Handle the back button click
            // Use the new OnBackPressedDispatcher API
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
