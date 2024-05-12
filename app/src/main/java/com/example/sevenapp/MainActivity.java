package com.example.sevenapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.Manifest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sevenapp.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private String userKey;
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FloatingActionButton fab = findViewById(R.id.fabadd);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Створюємо Intent, щоб відкрити нову активність
                Intent intent = new Intent(MainActivity.this, NewEvent.class);

                startActivity(intent);  // Запускає активність
            }
        });
        if (savedInstanceState == null) { // Переконайтеся, що це тільки при першому завантаженні
            replaceFragment(new HomeFragment());
        }

        binding.bottomNavigation.setBackground(null);
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.search) {
                replaceFragment(new SearchFragment());
            } else if (item.getItemId() == R.id.calendar) {
                replaceFragment(new CalendarFragment());
            } else if (item.getItemId() == R.id.profile) {
                ProfileFragment profileFragment = new ProfileFragment();

                replaceFragment(profileFragment);

            }

            return true;
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

}