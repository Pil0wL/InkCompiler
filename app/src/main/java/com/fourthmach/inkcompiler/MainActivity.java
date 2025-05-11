package com.fourthmach.inkcompiler;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fourthmach.inkcompiler.SaveFileSystem.SaveFileManager;
import com.fourthmach.inkcompiler.SaveFileSystem.ShallowSaveFile;
import com.fourthmach.inkcompiler.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private FrameLayout boxContainer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SaveFileManager.applicationContext = getApplicationContext();

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // activity_main | sets R to that context

        RecyclerView recyclerView = findViewById(R.id.save_file_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        HashMap<String, ShallowSaveFile> loadedSaveFiles = SaveFileManager.loadAllFiles();
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(loadedSaveFiles, save_file -> {
            Intent intent = new Intent(MainActivity.this, SaveFileInfoActivity.class);

            SaveFileManager.beholdedShallowSaveFile = save_file;

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

        findViewById(R.id.addsavefilebuttonindamenu).setOnClickListener(v -> {
            ShallowSaveFile created = SaveFileManager.officiateNewSaveFile();
            loadedSaveFiles.put(created.FileName, created);

            adapter.updateData(loadedSaveFiles);
        });

        Log.d("MainActivity", "Loaded MainActivity!");
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
    }


}