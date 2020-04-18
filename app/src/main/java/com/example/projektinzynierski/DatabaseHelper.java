package com.example.projektinzynierski;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "dogs.db";
    public static final String TABLE_NAME = "dogs_table";
    public static final String COL1 = "ID";
    public static final String COL2 = "NAME";
    public static final String COL3 = "WEIGHT";
    public static final String COL4 = "ACTIVITY_LEVEL";



    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " NAME TEXT, WEIGHT INTEGER, ACTIVITY_LEVEL INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public boolean addData(String name, int weight, int activityLevel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, name);
        contentValues.put(COL3, weight);
        contentValues.put(COL4, activityLevel);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public void updateDogData(String newName, int id, int newWeight, int newActivityLevel) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 + "='" + newName + "', " + COL3 + "=" + newWeight + ", "
                + COL4 + "=" + newActivityLevel + " WHERE " + COL1 + "=" + id;
        try {
            db.execSQL(query);
            Log.e("tag",query);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("tag",query);
        }

    }

    public void deleteData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = '" + id + "'";
        db.execSQL(query);
    }

    public Cursor showData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }
}
