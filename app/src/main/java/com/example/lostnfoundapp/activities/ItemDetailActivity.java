package com.example.lostnfoundapp.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.lostnfoundapp.R;
import com.example.lostnfoundapp.database.DatabaseHelper;
import com.example.lostnfoundapp.models.Item;

public class ItemDetailActivity extends AppCompatActivity {
    private TextView tvItemName, tvItemType, tvItemDate, tvItemLocation, tvItemDescription, tvItemContact;
    private CardView cvRemove;
    private int itemId;
    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        // Set status bar color to white/light
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Set status bar to white/light color
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));

            // Use dark icons (black) on light background
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        // Enable back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.item_details));
        }

        // Initialize views
        tvItemName = findViewById(R.id.tvItemName);
        tvItemType = findViewById(R.id.tvItemType);
        tvItemDate = findViewById(R.id.tvItemDate);
        tvItemLocation = findViewById(R.id.tvItemLocation);
        tvItemDescription = findViewById(R.id.tvItemDescription);
        tvItemContact = findViewById(R.id.tvItemContact);
        cvRemove = findViewById(R.id.cvRemove);

        // Get item ID from intent
        itemId = getIntent().getIntExtra("ITEM_ID", -1);

        if (itemId == -1) {
            Toast.makeText(this, R.string.error_item_not_found, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load item details
        loadItemDetails();

        // Set remove button click listener
        cvRemove.setOnClickListener(v -> confirmRemove());
    }

    private void loadItemDetails() {
        // Get item from database
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        item = dbHelper.getItemById(itemId);

        // Display item details
        tvItemName.setText(item.getName());
        tvItemType.setText(item.getType());
        tvItemDate.setText(item.getDate());
        tvItemLocation.setText(item.getLocation());
        tvItemDescription.setText(item.getDescription());
        tvItemContact.setText(item.getPhone());
    }

    private void confirmRemove() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.remove_title)
                .setMessage(R.string.remove_confirmation)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    // Remove item from database
                    DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
                    dbHelper.deleteItem(itemId);
                    Toast.makeText(this, R.string.remove_success, Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}