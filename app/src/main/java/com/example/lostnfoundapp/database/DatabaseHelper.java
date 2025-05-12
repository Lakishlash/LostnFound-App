package com.example.lostnfoundapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.lostnfoundapp.models.Item;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "LostAndFoundDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Name
    private static final String TABLE_ITEMS = "items";

    // Table Columns
    private static final String KEY_ID = "id";
    private static final String KEY_TYPE = "type";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DATE = "date";
    private static final String KEY_LOCATION = "location";

    // Singleton instance
    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create items table
        String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS +
                "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_TYPE + " TEXT," +
                KEY_NAME + " TEXT," +
                KEY_PHONE + " TEXT," +
                KEY_DESCRIPTION + " TEXT," +
                KEY_DATE + " TEXT," +
                KEY_LOCATION + " TEXT" +
                ")";

        db.execSQL(CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);

        // Create table again
        onCreate(db);
    }

    // Add a new item
    public long addItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TYPE, item.getType());
        values.put(KEY_NAME, item.getName());
        values.put(KEY_PHONE, item.getPhone());
        values.put(KEY_DESCRIPTION, item.getDescription());
        values.put(KEY_DATE, item.getDate());
        values.put(KEY_LOCATION, item.getLocation());

        // Insert row
        long itemId = db.insert(TABLE_ITEMS, null, values);
        db.close();

        return itemId;
    }

    // Get all items
    public List<Item> getAllItems() {
        List<Item> itemList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_ITEMS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
                item.setType(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TYPE)));
                item.setName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)));
                item.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHONE)));
                item.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)));
                item.setDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)));
                item.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(KEY_LOCATION)));

                itemList.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return itemList;
    }

    // Get item by ID
    public Item getItemById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_ITEMS,
                new String[]{KEY_ID, KEY_TYPE, KEY_NAME, KEY_PHONE, KEY_DESCRIPTION, KEY_DATE, KEY_LOCATION},
                KEY_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Item item = new Item();
        item.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
        item.setType(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TYPE)));
        item.setName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)));
        item.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHONE)));
        item.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)));
        item.setDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)));
        item.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(KEY_LOCATION)));

        cursor.close();
        db.close();

        return item;
    }

    // Delete item by ID
    public void deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, KEY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }
}