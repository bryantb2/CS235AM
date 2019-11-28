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
    public static final String TIDE_PREDICTIONS = "TIDE_PREDICTIONS";
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
        db.execSQL("CREATE TABLE " + TIDE_PREDICTIONS
                + "( _id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DATE + " TEXT,"
                + DAY + " INTEGER,"
                + TIDE_TIME + " TEXT,"
                + WAVE_HEIGHT_CM + " TEXT,"
                + GET_LOW + " TEXT" + ")" );

        // INITIALIZE DB
        // ADD IN THE VALUES FROM THE XML FILE
        loadDbFromXML(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Parse the XML files and put the data in the db
    public void loadDbFromXML(SQLiteDatabase db) {
        // PARSE XML FILE (LIKE LAST LAB)
        Dal dal = new Dal(context);
        ArrayList<TideItem> items = dal.parseXmlFile("tide_predictions.xml");

        // ALLOWS FOR THE STORAGE OF DB ROW ITEMS
        ContentValues cv = new ContentValues();

        for(TideItem item : items)
        {
            // GET THE ITEMS
            // SETS THEM TO THEIR APPROPRIATE COLUMNS
            cv.put(TideSQLiteHelper.DATE, item.getDate());
            cv.put(TideSQLiteHelper.DAY, item.getDay());
            cv.put(TideSQLiteHelper.TIDE_TIME, item.getTime());
            cv.put(TideSQLiteHelper.WAVE_HEIGHT_CM, item.getPredInCm());
            cv.put(TideSQLiteHelper.GET_LOW, item.getHighlow());

            // BUILDS A ROW IN THE DB
            db.insert(TideSQLiteHelper.TIDE_PREDICTIONS, null, cv);
        }
    }
}
