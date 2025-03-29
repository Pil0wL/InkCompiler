package com.fourthmach.inkcompiler;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import com.fourthmach.inkcompiler.databinding.ActivityMainBinding;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private FrameLayout boxContainer;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private List<SaveFile> itemList;
    private static final String FILE_NAME = "saved_notes.json"; // Storage File

    private static final int REQUEST_ADD_NOTE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // activity_main | sets R to that context

        RecyclerView recyclerView = findViewById(R.id.save_file_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemList = loadNotes();
        if (itemList == null) {
            itemList = new ArrayList<>();
        }


        RecyclerViewAdapter adapter = new RecyclerViewAdapter(itemList, save_file -> {
            Intent intent = new Intent(MainActivity.this, SaveFileInfoActivity.class);
            intent.putExtra("save_file", save_file);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);


        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Exit the app when the back button is pressed in Activity A
                finishAffinity(); // Close all activities in the task
            }
        };

        // Add the callback to the OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, callback);

    }

    // Function to open AddNoteActivity
    private void openAddNoteScreen() {
        Intent intent = new Intent(this, AddNoteActivity.class);
        startActivityForResult(intent, REQUEST_ADD_NOTE);
    }

    // Handle the result from AddNoteActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_NOTE && resultCode == RESULT_OK) {
            String title = data.getStringExtra("title");
            String description = data.getStringExtra("description");
            addNewNote(title, description);
        }

        boxContainer = findViewById(R.id.boxContainer);
    }


    // Save notes to storage
    private void saveNotes() {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(itemList);
            FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(json.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load notes from storage
    private List<SaveFile> loadNotes() {
        try {
            FileInputStream fis = openFileInput(FILE_NAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            reader.close();
            fis.close();
            Gson gson = new Gson();
            Type type = new TypeToken<List<SaveFile>>() {
            }.getType();
            return gson.fromJson(jsonBuilder.toString(), type);
        } catch (IOException e) {
            return new ArrayList<>(); // Return empty list if file not found
        }
    }

    // Add a new note
    public void addNewNote(String title, String description) {
        SaveFile newNote = new SaveFile(title, description);
        itemList.add(newNote);
        saveNotes(); // Save updated list
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Note Saved!", Toast.LENGTH_SHORT).show();
    }

    public void openAddNoteScreen(View view) {
        Intent intent = new Intent(this, AddNoteActivity.class);
        startActivity(intent);
    }




        /*
        setSupportActionBar(binding.appBarMain.toolbar);
        if (binding.appBarMain.fab != null) {
            binding.appBarMain.fab.setOnClickListener(view -> {
                Snackbar.make(view, "replaced :money_mount:", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).setAnchorView(R.id.fab).show();

                addDraggableBox();

            });
        }
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();

        NavigationView navigationView = binding.navView;
        if (navigationView != null) {
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_transform, R.id.nav_reflow, R.id.nav_slideshow, R.id.nav_settings)
                    .setOpenableLayout(binding.drawerLayout)
                    .build();
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);
        }

        BottomNavigationView bottomNavigationView = binding.appBarMain.contentMain.bottomNavView;
        if (bottomNavigationView != null) {
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_transform, R.id.nav_reflow, R.id.nav_slideshow)
                    .build();
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
        }

        boxContainer = findViewById(R.id.the_things_of_where_i_put);
        if (boxContainer == null) {
            Log.e("MainActivity", "boxContainer is null! Check your included layout file.");
        }
         */

    private void addDraggableBox() {
        // Inflate the draggable box layout
        View box = getLayoutInflater().inflate(R.layout.draggable_box, boxContainer, false);


        // Set up touch listener to make the box draggable
        box.setOnTouchListener(new View.OnTouchListener() {
            private float dX, dY;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Record the initial touch position
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // Move the view with the touch
                        view.animate()
                                .x(event.getRawX() + dX)
                                .y(event.getRawY() + dY)
                                .setDuration(0)
                                .start();
                        break;
                    case MotionEvent.ACTION_UP:
                        // Call performClick() when a click is detected
                        view.performClick();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        // Add the box to the container
        boxContainer.addView(box);

    }
}
