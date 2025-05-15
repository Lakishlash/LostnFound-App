package com.example.lostnfoundapp.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.lostnfoundapp.R;
import com.example.lostnfoundapp.database.DatabaseHelper;
import com.example.lostnfoundapp.models.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemListActivity extends AppCompatActivity {
    private ListView listViewItems;
    private TextView tvEmpty;
    private RadioGroup rgFilter;
    private List<Item> allItemsList;
    private List<Item> filteredItemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        // Set status bar color to white/light
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Set status bar to white/light color
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));

            // Use dark icons on light background
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        // Enable back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.lost_found_items));
        }

        // Initialize views
        listViewItems = findViewById(R.id.listViewItems);
        tvEmpty = findViewById(R.id.tvEmpty);
        rgFilter = findViewById(R.id.rgFilter);

        // Set up filter radio group listener
        rgFilter.setOnCheckedChangeListener((group, checkedId) -> {
            filterItems();
        });

        // Set up list item click listener
        listViewItems.setOnItemClickListener((parent, view, position, id) -> {
            Item selectedItem = filteredItemsList.get(position);
            Intent intent = new Intent(ItemListActivity.this, ItemDetailActivity.class);
            intent.putExtra("ITEM_ID", selectedItem.getId());
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadItems();
    }

    private void loadItems() {
        // Get all items from database
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        allItemsList = dbHelper.getAllItems();

        // Apply current filter
        filterItems();
    }

    private void filterItems() {
        if (allItemsList == null || allItemsList.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            listViewItems.setVisibility(View.GONE);
            return;
        }

        // Apply filter based on selected radio button
        int selectedId = rgFilter.getCheckedRadioButtonId();

        if (selectedId == R.id.rbLostFilter) {
            // Filter to show only Lost items
            filteredItemsList = allItemsList.stream()
                    .filter(item -> getString(R.string.lost).equals(item.getType()))
                    .collect(Collectors.toList());
        } else if (selectedId == R.id.rbFoundFilter) {
            // Filter to show only Found items
            filteredItemsList = allItemsList.stream()
                    .filter(item -> getString(R.string.found).equals(item.getType()))
                    .collect(Collectors.toList());
        } else {
            // Show all items
            filteredItemsList = new ArrayList<>(allItemsList);
        }

        if (filteredItemsList.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            listViewItems.setVisibility(View.GONE);
            return;
        }

        tvEmpty.setVisibility(View.GONE);
        listViewItems.setVisibility(View.VISIBLE);

        // Create data for adapter
        List<Map<String, String>> data = new ArrayList<>();
        for (Item item : filteredItemsList) {
            Map<String, String> map = new HashMap<>();
            map.put("name", item.getName() + " (" + item.getType() + ")");
            map.put("details", getString(R.string.location) + ": " + item.getLocation() + " • " + item.getDate());
            data.add(map);
        }

        // Create adapter
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                data,
                R.layout.list_item,
                new String[]{"name", "details"},
                new int[]{R.id.tvItemName, R.id.tvItemDetails}
        );

        // Set adapter
        listViewItems.setAdapter(adapter);
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