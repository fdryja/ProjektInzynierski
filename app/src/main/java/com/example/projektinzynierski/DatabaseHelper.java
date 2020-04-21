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
    public static final String TABLE_DATES = "dates_table";
    public static final String TABLE_ALARM = "alarm_table";

    public static final String COL1 = "ID";
    public static final String COL2 = "NAME";
    public static final String COL3 = "WEIGHT";
    public static final String COL4 = "ACTIVITY_LEVEL";
    public static final String COL5 = "EATING_COUNT";
    public static final String COL6 = "EATING";

    public static final String COL1D = "ID";
    public static final String COL2D = "SZCZEPIENIE";
    public static final String COL3D = "ODROBACZANIE";
    public static final String COL4D = "DOG_ID";

    public static final String COL1A = "ID";
    public static final String COL2A = "ALARM_NUMBER";
    public static final String COL3A = "ALARM";
    public static final String COL4A = "DOG_ID";



    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " NAME TEXT UNIQUE, WEIGHT INTEGER, ACTIVITY_LEVEL INTEGER, EATING_COUNT INTEGER DEFAULT 0, EATING INTEGER DEFAULT 0)";
        db.execSQL(createTable);

        createTable = "CREATE TABLE "+TABLE_DATES+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, SZCZEPIENIE TEXT DEFAULT 0, " +
                "ODROBACZANIE TEXT DEFAULT 0, DOG_ID INTEGER, FOREIGN KEY(DOG_ID) REFERENCES "+TABLE_NAME+"(ID))";
        db.execSQL(createTable);

        createTable = "CREATE TABLE "+TABLE_ALARM+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, ALARM_NUMBER INTEGER, ALARM TEXT DEFAULT 'Brak', DOG_ID INTEGER, FOREIGN KEY(DOG_ID) REFERENCES "+TABLE_NAME+"(ID))";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_DATES);
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



    public boolean addAlarm(int dogId, int number){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2A,number);
        contentValues.put(COL4A, dogId);

        long result = db.insert(TABLE_ALARM, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void updateAlarm(String alarm, int dogId, int al){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE "+ TABLE_ALARM + " SET " + COL3A + "='" + alarm + "' WHERE "+COL4A+"="+dogId+ " AND "+COL2A+"="+al;
        try {
            db.execSQL(query);
            Log.e("tag",query);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("tag",query);
        }
    }
    public void deleteAlarm(int dogId){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "DELETE FROM "+TABLE_ALARM+" WHERE DOG_ID="+dogId;
        try {
            db.execSQL(query);
            Log.e("tag",query);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("tag",query);
        }
    }

    public boolean addDataDates(int dogId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL4D, dogId);

        long result = db.insert(TABLE_DATES, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public void addDataSzczepienie(String szczepienie, int dogId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2D, szczepienie);
        contentValues.put(COL4D, dogId);

        String query = "UPDATE "+ TABLE_DATES + " SET " + COL2D + "='" + szczepienie + "' WHERE "+COL4D+"="+dogId;
        try {
            db.execSQL(query);
            Log.e("tag",query);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("tag",query);
        }

    }

    public void addDataOdrobaczanie(String odrobaczanie, int dogId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL3D, odrobaczanie);
        contentValues.put(COL4D, dogId);
        String query = "UPDATE "+ TABLE_DATES + " SET " + COL3D + "='" + odrobaczanie + "' WHERE "+COL4D+"="+dogId;
        try {
            db.execSQL(query);
            Log.e("tag",query);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("tag",query);
        }
    }

    public Cursor showDates(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_DATES+" WHERE "+COL4D+"="+id, null);
        return data;
    }

    public Cursor getLastId(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT last_insert_rowid()", null);
        return data;

    }

    public void updateEating(int id, int eatingCount, int eating){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE "+ TABLE_NAME + " SET " + COL5 + "=" + eatingCount + ", " + COL6+ "="+ eating
                +" WHERE "+COL1+"="+id;
        try {
            db.execSQL(query);
            Log.e("tag",query);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("tag",query);
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

    public Cursor showName(int dogId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT NAME FROM " + TABLE_NAME+ " WHERE ID="+dogId, null);
        return data;
    }

    public Cursor showCount(int dogId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT EATING_COUNT FROM " + TABLE_NAME+ " WHERE ID="+dogId, null);
        return data;
    }

    public Cursor showAlarm(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_ALARM+" WHERE DOG_ID="+id, null);
        return data;
    }
    public Cursor showAlarm(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_ALARM, null);
        return data;
    }
    public Cursor alarmCount(int dogId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_ALARM+" WHERE DOG_ID="+dogId, null);
        return data;
    }
}
