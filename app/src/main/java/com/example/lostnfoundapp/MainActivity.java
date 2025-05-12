package com.example.lostnfoundapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.lostnfoundapp.activities.CreateAdvertActivity;
import com.example.lostnfoundapp.activities.ItemListActivity;

public class MainActivity extends AppCompatActivity {
    private CardView cvCreateAdvert, cvShowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the action bar completely for main activity
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_main);

        // Set status bar color to white/light
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Set status bar to white/light color
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));

            // Use dark icons (black) on light background
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        // Initialize views
        cvCreateAdvert = findViewById(R.id.cvCreateAdvert);
        cvShowItems = findViewById(R.id.cvShowItems);

        // Set click listeners
        cvCreateAdvert.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateAdvertActivity.class);
            startActivity(intent);
        });

        cvShowItems.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ItemListActivity.class);
            startActivity(intent);
        });
    }
}