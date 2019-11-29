package com.example.tideprediction;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class TideSQLiteHelper extends SQLiteOpenHelper {
    // CLASS FIELDS
    private static final String DB_NAME = "Tide.sqlite";
    private static final int DB_VERSION = 1;
    private Context context = null;

    // PUBLIC CONSTANTS
    public static final String TIDE_PREDICTIONS_FLORENCE = "TIDE_PREDICTIONS_FLORENCE";
    public static final String TIDE_PREDICTIONS_NEWPORT = "TIDE_PREDICTIONS_NEWPORT";
    public static final String TIDE_PREDICTIONS_ASTORIA = "TIDE_PREDICTIONS_ASTORIA";
    public static final String DATE = "DATE";
    public static final String DAY = "DAY";
    public static final String TIDE_TIME = "TIDE_TIME";
    public static final String WAVE_HEIGHT_CM = "WAVE_HEIGHT_CM";
    public static final String GET_LOW = "GET_LOW";



    public TideSQLiteHelper(Context c) {
        super(c, DB_NAME, null, DB_VERSION);
        this.context = c;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // BUILD DATABASE STRUCTURE
        // DB structure for FLORENCE
        db.execSQL("CREATE TABLE " + TIDE_PREDICTIONS_FLORENCE
                + "( _id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DATE + " TEXT,"
                + DAY + " INTEGER,"
                + TIDE_TIME + " TEXT,"
                + WAVE_HEIGHT_CM + " TEXT,"
                + GET_LOW + " TEXT" + ")" );

        // DB structure for NEWPORT
        db.execSQL("CREATE TABLE " + TIDE_PREDICTIONS_NEWPORT
                + "( _id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DATE + " TEXT,"
                + DAY + " INTEGER,"
                + TIDE_TIME + " TEXT,"
                + WAVE_HEIGHT_CM + " TEXT,"
                + GET_LOW + " TEXT" + ")" );

        // DB structure for ASTORIA
        db.execSQL("CREATE TABLE " + TIDE_PREDICTIONS_ASTORIA
                + "( _id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DATE + " TEXT,"
                + DAY + " INTEGER,"
                + TIDE_TIME + " TEXT,"
                + WAVE_HEIGHT_CM + " TEXT,"
                + GET_LOW + " TEXT" + ")" );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
