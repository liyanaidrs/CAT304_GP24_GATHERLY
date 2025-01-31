package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;

public class OrganiserScanActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organiser_scan);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new OrganiserQRFragment())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_scan);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_scan) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new OrganiserQRFragment())
                    .commit();
        } else if (id == R.id.nav_statistics) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new OrganiserStatisticFragment())
                    .commit();
        }  else if (id == R.id.nav_chat) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new OrganiserChatFragment())
                    .commit();
        }
        else if (id == R.id.nav_logout) {
            Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(OrganiserScanActivity.this, MainActivity.class);
            startActivity(intent);


        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}