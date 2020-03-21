package com.github.napat.sudoku.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import com.github.napat.sudoku.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Sodoku";
    private CoordinatorLayout rootLayout;
    private FloatingActionButton btnFab;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        initBar();
        initInstance();
    }

    private void initBar() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(AboutActivity.this, drawerLayout, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        // Enable Home Button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(navMenuListener);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        // tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
        // tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
        // tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));

    }

    private void initInstance() {
        rootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);

        btnFab = (FloatingActionButton) findViewById(R.id.btnFab);
        btnFab.setOnClickListener(this);
    }

    NavigationView.OnNavigationItemSelectedListener navMenuListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            int id = menuItem.getItemId();
            switch (id) {
                case R.id.navItem1:
                    Snackbar.make(drawerLayout, "navItem1", Snackbar.LENGTH_SHORT)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.d(TAG, "navItem1: Undo button is clicked!");
                                }
                            })
                            .show();
                    break;
                case R.id.navItem2:
                    Snackbar.make(drawerLayout, "navItem2", Snackbar.LENGTH_SHORT)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.d(TAG, "navItem2: Undo button is clicked!");
                                }
                            })
                            .show();
                    break;
                case R.id.navItem3:
                    Snackbar.make(drawerLayout, "navItem3", Snackbar.LENGTH_SHORT)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.d(TAG, "navItem3: Undo button  is clicked!");
                                }
                            })
                            .show();
                    break;
                default:
                    Snackbar.make(drawerLayout, "default navItem", Snackbar.LENGTH_SHORT)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.d(TAG, "default navItem: Undo button  is clicked!");
                                }
                            })
                            .show();
            }
            return false;
        }
    };

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Enable Home Button
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Enable Home Button
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Enable Home Button
        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFab:

                Snackbar.make(rootLayout, "Hello. I am Snackbar!", Snackbar.LENGTH_SHORT)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d(TAG, "Button FAB UNDO on Snackbar is clicked!");
                            }
                        })
                        .show();

                return;
        }
    }
}
