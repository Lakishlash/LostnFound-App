package com.example.lostnfoundapp.activities;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.lostnfoundapp.R;
import com.example.lostnfoundapp.database.DatabaseHelper;
import com.example.lostnfoundapp.models.Item;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateAdvertActivity extends AppCompatActivity {
    private RadioGroup rgPostType;
    private RadioButton rbLost, rbFound;
    private EditText etName, etPhone, etDescription, etDate, etLocation;
    private CardView cvSave;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);

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
            getSupportActionBar().setTitle(getString(R.string.create_advert_title));
        }

        // Initialize views
        rgPostType = findViewById(R.id.rgPostType);
        rbLost = findViewById(R.id.rbLost);
        rbFound = findViewById(R.id.rbFound);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etDescription = findViewById(R.id.etDescription);
        etDate = findViewById(R.id.etDate);
        etLocation = findViewById(R.id.etLocation);
        cvSave = findViewById(R.id.cvSave);

        // Initialize calendar
        calendar = Calendar.getInstance();

        // Set up date picker
        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            updateDateLabel();
        };

        etDate.setOnClickListener(v -> {
            new DatePickerDialog(CreateAdvertActivity.this, date,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Save button click listener
        cvSave.setOnClickListener(v -> saveItem());
    }

    private void updateDateLabel() {
        String dateFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        etDate.setText(sdf.format(calendar.getTime()));
    }

    private void saveItem() {
        // Get values from form
        String type = rbLost.isChecked() ? getString(R.string.lost) : getString(R.string.found);
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String location = etLocation.getText().toString().trim();

        // Validate inputs
        if (name.isEmpty() || phone.isEmpty() || description.isEmpty() || date.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        // Create new item
        Item item = new Item(type, name, phone, description, date, location);

        // Save to database
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        long itemId = dbHelper.addItem(item);

        if (itemId != -1) {
            Toast.makeText(this, R.string.item_saved_success, Toast.LENGTH_SHORT).show();
            // Clear form
            etName.setText("");
            etPhone.setText("");
            etDescription.setText("");
            etDate.setText("");
            etLocation.setText("");
            rbLost.setChecked(true);

            // Finish activity
            finish();
        } else {
            Toast.makeText(this, R.string.error_saving_item, Toast.LENGTH_SHORT).show();
        }
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