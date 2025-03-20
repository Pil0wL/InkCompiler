package com.fourthmach.inkcompiler;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fourthmach.inkcompiler.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private FrameLayout boxContainer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // activity_main | sets R to that context

        RecyclerView recyclerView = findViewById(R.id.save_file_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<SaveFile> itemList = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            itemList.add(new SaveFile("Item " + i, "Description " + i));
        }

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(itemList, item -> {
            // Handle item click
            Intent intent = new Intent(MainActivity.this, SaveFileInfoActivity.class);
            intent.putExtra("title", item.getTitle());
            intent.putExtra("description", item.getDescription());
            intent.putExtra("actionBarTitle", item.getTitle());

            startActivity(intent);
        });


        recyclerView.setAdapter(adapter);

        if (true) {return;}
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        // Using findViewById because NavigationView exists in different layout files
        // between w600dp and w1240dp
        NavigationView navView = findViewById(R.id.nav_view);
        if (navView == null) {
            // The navigation drawer already has the items including the items in the overflow menu
            // We only inflate the overflow menu if the navigation drawer isn't visible
            getMenuInflater().inflate(R.menu.overflow, menu);
        }
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_settings) {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_settings);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }



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