package com.example.githubtrendingclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ContentActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private NavController fragmentMenuBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fragmentMenuBottom = Navigation.findNavController(this, R.id.fragmentMenuBottom);
        NavigationUI.setupWithNavController(bottomNavigationView, fragmentMenuBottom);
    }
}