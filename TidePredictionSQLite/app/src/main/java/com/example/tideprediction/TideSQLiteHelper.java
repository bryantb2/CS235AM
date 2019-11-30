package com.example.tideprediction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

public class TideSQLiteHelper extends SQLiteOpenHelper {
    // Initialize database
    //TideSQLiteHelper helper;
    SQLiteDatabase db;

    // Get reference to Dal
    private Dal dal;
    private int testVar;

    // CLASS FIELDS
    private static final String DB_NAME = "Tide.SQlite";
    private static final int DB_VERSION = 4;
    private Context context;

    // PUBLIC TABLE ID CONSTANTS
    public static final String TIDE_PREDICTIONS_FLORENCE = "TIDE_PREDICTIONS_FLORENCE";
    public static final String TIDE_PREDICTIONS_NEWPORT = "TIDE_PREDICTIONS_NEWPORT";
    public static final String TIDE_PREDICTIONS_ASTORIA = "TIDE_PREDICTIONS_ASTORIA";

    // PUBLIC TABLE ROW IDs
    public static final String DATE = "DATE";
    public static final String DAY = "DAY";
    public static final String TIDE_TIME = "TIDE_TIME";
    public static final String WAVE_HEIGHT_CM = "WAVE_HEIGHT_CM";
    public static final String GET_LOW = "GET_LOW";



    public TideSQLiteHelper(Context c) {
        super(c, DB_NAME, null, DB_VERSION);
        this.context = c;
        // Initialize database
        //TideSQLiteHelper helper = new TideSQLiteHelper(c);
        this.dal = new Dal(c);
        this.db = getReadableDatabase();

        this.testVar = 1;
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

    // Parse the XML files and put the data in the db
    public void loadDbFromXML() {
        // PARSE XML FILE (LIKE LAST LAB)
        for(int i = 0; i < MainActivity.NUMBER_OF_LOCATIONS; i++) {
            final String xmlFileStub = "_tide_predictions.xml";
            // Set tide items variable
            // Set tide table string identifier
            ArrayList<TideItem> items;
            String dbTableId;
            if(i == 0) {
                items = dal.parseXmlFile(MainActivity.FLORENCE + xmlFileStub);
                dbTableId = TideSQLiteHelper.TIDE_PREDICTIONS_FLORENCE;
            }
            else if(i == 1) {
                items = dal.parseXmlFile(MainActivity.NEWPORT + xmlFileStub);
                dbTableId = TideSQLiteHelper.TIDE_PREDICTIONS_NEWPORT;
            }
            else if(i == 2){
                items = dal.parseXmlFile(MainActivity.ASTORIA + xmlFileStub);
                dbTableId = TideSQLiteHelper.TIDE_PREDICTIONS_ASTORIA;
            }
            else {
                items = null;
                dbTableId = "";
            }

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

                // TEMP
                Log.d("DATE LOGGER","Logging date: " + item.getDate());

                // BUILDS A ROW IN THE DB (using id)
                db.insert(dbTableId, null, cv);
            }
        }
    }

    public Cursor getTidePredictionsByDate(String date, String location) {
        // get readable DB in this method because this is where data is retrieved
        //this.db = this.getReadableDatabase();
        // TODO: RETRIEVE VALUES FROM DB
        // Get table id based off location string
        String dbTableId;
        if(location == MainActivity.ASTORIA)
            dbTableId = TideSQLiteHelper.TIDE_PREDICTIONS_ASTORIA;
        else if(location == MainActivity.FLORENCE)
            dbTableId = TideSQLiteHelper.TIDE_PREDICTIONS_FLORENCE;
        else
            dbTableId = TideSQLiteHelper.TIDE_PREDICTIONS_NEWPORT;

        // Query DB tide items
        // String query = "SELECT * FROM "+ dbTableId + "WHERE DATE = " + date.toString();
        String query = "SELECT * FROM "+ dbTableId + "WHERE DATE = " + date;
        Cursor tempCursorObj = db.rawQuery(query, null);
        return tempCursorObj;
    }

    public Boolean isDBEmpty() {
        // Get florence tide table (since all three are loaded together, if one is not present, all of them aren't)
        Cursor mCursor = db.rawQuery("SELECT * FROM " + TideSQLiteHelper.TIDE_PREDICTIONS_FLORENCE, null);
        Boolean rowExists;

        if (mCursor.moveToFirst())
        {
            // DO SOMETHING WITH CURSOR
            rowExists = true;

        } else
        {
            // I AM EMPTY
            rowExists = false;
        }

        return rowExists;
    }


    /*
    PREVIOUSLY WORKING DB CLASS
    public class TideSQLiteHelper extends SQLiteOpenHelper {
    // Initialize database
    //TideSQLiteHelper helper;
    //SQLiteDatabase db;

    // Get reference to Dal
    private Dal dal;
    private int testVar;

    // CLASS FIELDS
    private static final String DB_NAME = "Tide.SQlite";
    private static final int DB_VERSION = 4;
    private Context context;

    // PUBLIC TABLE ID CONSTANTS
    public static final String TIDE_PREDICTIONS_FLORENCE = "TIDE_PREDICTIONS_FLORENCE";
    public static final String TIDE_PREDICTIONS_NEWPORT = "TIDE_PREDICTIONS_NEWPORT";
    public static final String TIDE_PREDICTIONS_ASTORIA = "TIDE_PREDICTIONS_ASTORIA";

    // PUBLIC TABLE ROW IDs
    public static final String DATE = "DATE";
    public static final String DAY = "DAY";
    public static final String TIDE_TIME = "TIDE_TIME";
    public static final String WAVE_HEIGHT_CM = "WAVE_HEIGHT_CM";
    public static final String GET_LOW = "GET_LOW";



    public TideSQLiteHelper(Context c) {
        super(c, DB_NAME, null, DB_VERSION);
        this.context = c;
        // Initialize database
        //TideSQLiteHelper helper = new TideSQLiteHelper(c);
        this.dal = new Dal(c);

        this.testVar = 1;
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

        // Fill DB with data
        //loadDbFromXML();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Parse the XML files and put the data in the db
    public void loadDbFromXML(SQLiteDatabase db) {
        // PARSE XML FILE (LIKE LAST LAB)
        for(int i = 0; i < MainActivity.NUMBER_OF_LOCATIONS; i++) {
            final String xmlFileStub = "_tide_predictions.xml";
            // Set tide items variable
            // Set tide table string identifier
            ArrayList<TideItem> items;
            String dbTableId;
            if(i == 0) {
                items = dal.parseXmlFile(MainActivity.FLORENCE + xmlFileStub);
                dbTableId = TideSQLiteHelper.TIDE_PREDICTIONS_FLORENCE;
            }
            else if(i == 1) {
                items = dal.parseXmlFile(MainActivity.NEWPORT + xmlFileStub);
                dbTableId = TideSQLiteHelper.TIDE_PREDICTIONS_NEWPORT;
            }
            else if(i == 2){
                items = dal.parseXmlFile(MainActivity.ASTORIA + xmlFileStub);
                dbTableId = TideSQLiteHelper.TIDE_PREDICTIONS_ASTORIA;
            }
            else {
                items = null;
                dbTableId = "";
            }

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

                // BUILDS A ROW IN THE DB (using id)
                db.insert(dbTableId, null, cv);
            }
        }
        // Close
        db.close();
    }

    public Cursor getTideTableByDate(SQLiteDatabase db, Date date, String location) {
        // get readable DB in this method because this is where data is retrieved
        db = this.getReadableDatabase();
        // TODO: RETRIEVE VALUES FROM DB
        // Get table id based off location string
        String dbTableId;
        if(location == MainActivity.ASTORIA)
            dbTableId = TideSQLiteHelper.TIDE_PREDICTIONS_ASTORIA;
        else if(location == MainActivity.FLORENCE)
            dbTableId = TideSQLiteHelper.TIDE_PREDICTIONS_FLORENCE;
        else
            dbTableId = TideSQLiteHelper.TIDE_PREDICTIONS_NEWPORT;

        // Query DB tide items
        // String query = "SELECT * FROM dbTableId WHERE Zip = ? ORDER BY Date ASC";
        String query = "SELECT * FROM "+ dbTableId;
        Cursor tempCursorObj = db.rawQuery(query, null);
        return tempCursorObj;
    }
     */


}
