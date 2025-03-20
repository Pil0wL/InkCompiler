package com.fourthmach.inkcompiler;

import android.os.Bundle;
import android.view.MenuItem;
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

        // Get data from the intent
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");

        String actionBarTitle = getIntent().getStringExtra("actionBarTitle");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(actionBarTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Set the data to the views
        titleTextView.setText(title);
        descriptionTextView.setText(description);
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
