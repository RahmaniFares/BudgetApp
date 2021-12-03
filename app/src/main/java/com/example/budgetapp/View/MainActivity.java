package com.example.budgetapp.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.budgetapp.R;
import com.example.budgetapp.View.fragment_MainActivity.DashbordFragment;
import com.example.budgetapp.View.fragment_MainActivity.HistoricFragment;
import com.example.budgetapp.View.fragment_MainActivity.ProfilFragment;
import com.example.budgetapp.View.fragment_MainActivity.StatisticsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {



    // eazeGraph
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DashbordFragment())
                .commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch(item.getItemId()){
                        case R.id.nav_home :
                            selectedFragment = new DashbordFragment();break;
                        case R.id.nav_hist :
                            selectedFragment = new HistoricFragment();break;
                        case R.id.nav_stats :
                            selectedFragment = new StatisticsFragment();break;
                        case R.id.nav_profil :
                            selectedFragment = new ProfilFragment();break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                    return true;
                }
            };


}