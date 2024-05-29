package com.example.sholatki;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.sholatki.fragment.HomeFragment;
import com.example.sholatki.fragment.JadwalFragment;
import com.example.sholatki.fragment.LocationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    JadwalFragment jadwalFragment = new JadwalFragment();
    LocationFragment settingsFragment = new LocationFragment();
    Toolbar toolbar;
    Menu mainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.homeFragment) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, homeFragment).commit();
                    getSupportActionBar().setTitle("Home");
                    showBookmarkMenu(true);
                    return true;
                } else if(menuItem.getItemId() == R.id.jadwalFragment) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, jadwalFragment).commit();
                    getSupportActionBar().setTitle("Jadwal Sholat");
                    showBookmarkMenu(false);
                    return true;
                } else if(menuItem.getItemId() == R.id.locationFragment){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, settingsFragment).commit();
                    getSupportActionBar().setTitle("Lokasi");
                    showBookmarkMenu(false);
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        mainMenu = menu;
        return true;
    }

    private void showBookmarkMenu(boolean show) {
        if (mainMenu != null) {
            MenuItem bookmarkItem = mainMenu.findItem(R.id.action_bookmark);
            if (bookmarkItem != null) {
                bookmarkItem.setVisible(show);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_bookmark) {
            startActivity(new Intent(this, HadithActivity.class));
        }
        return false;
    }

}