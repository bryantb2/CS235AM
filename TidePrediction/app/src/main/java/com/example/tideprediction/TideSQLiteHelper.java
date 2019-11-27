package com.example.tideprediction;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TideSQLiteHelper extends SQLiteOpenHelper {
    // CLASS FIELDS
    private static final String DB_NAME = "Tide.sqlite";
    private static final int DB_VERSION = 1;
    private Context context = null;

    // PUBLIC CONSTANTS
    public static final String TIDE_PREDICTIONS = "TIDE_PREDICTIONS";
    public static final String DATE = "DATE";
    public static final String DAY = "DAY";
    public static final String TIDE_TIME = "DATE";
    public static final String WAVE_HEIGHT_CM = "WAVE_HEIGHT_CM";
    public static final String GET_LOW = "GET_LOW";



    public TideSQLiteHelper(Context c) {
        super(c, DB_NAME, null, DB_VERSION);
        this.context = c;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // BUILD DATABASE STRUCTURE
        db.execSQL("CREATE TABLE " + TIDE_PREDICTIONS
                + "( _id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DATE + " TEXT,"
                + DAY + " INTEGER,"
                + TIDE_TIME + " TEXT,"
                + WAVE_HEIGHT_CM + " TEXT,"
                + GET_LOW + " TEXT" + ")" );

        // INITIALIZE DB
        // ADD IN THE VALUES FROM THE XML FILE
        Dal dal = new Dal(context);
        dal.loadDbFromXML();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}