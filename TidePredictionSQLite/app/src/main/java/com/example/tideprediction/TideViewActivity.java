package com.example.tideprediction;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TideViewActivity extends ListActivity {

    // UI ELEMENTS
    private int dayAbbrTextView;
    private int dateTimeTextView;
    private int tideStatusTextView;
    private int tideTimeTextView;

    // CLASS FIELDS
    Cursor cursor;
    TideSQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get references to UI objects
        dayAbbrTextView = R.id.dayAbbrv;
        dateTimeTextView = R.id.dateTime;
        tideStatusTextView = R.id.tideStatus;
        tideTimeTextView = R.id.tideTime;

        // Get intent sent from main activity
        Intent intent = getIntent();
        int dayOfMonth = intent.getExtras().getInt(MainActivity.DAY);
        int monthOfYear = intent.getExtras().getInt(MainActivity.MONTH);
        int year = intent.getExtras().getInt(MainActivity.YEAR);
        String location = intent.getExtras().getString(MainActivity.LOCATION);

        // Set DB helper object
        // Load table data for select location
        this.sqLiteHelper = new TideSQLiteHelper(this);
        if(sqLiteHelper.isDBEmpty() == true)
            sqLiteHelper.loadDbFromXML();

        // Build date string that will get passed into getTideItems method
        String dateString = String.valueOf(year) + "/" + String.valueOf(monthOfYear) + "/" + String.valueOf(dayOfMonth);

        cursor = this.sqLiteHelper.getTidePredictionsByDate(dateString, location);

        // TEMP LOGGING
        if(cursor != null && cursor.getCount() >0) {
            Log.d("APPLICATION", "the cursor is not empty!");
        }


        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.list_layout,
                cursor,
                new String[]{TideSQLiteHelper.DAY,
                        TideSQLiteHelper.DATE,
                        TideSQLiteHelper.GET_LOW,
                        TideSQLiteHelper.TIDE_TIME,
                },
                new int[]{
                        dayAbbrTextView,
                        dateTimeTextView,
                        tideStatusTextView,
                        tideTimeTextView
                },
                0 );	// no flags
        setListAdapter(adapter);
        getListView().setFastScrollEnabled(true);
    }

/*
PREVIOUSLY WORKING DB
public class TideViewActivity extends ListActivity {

    // UI ELEMENTS
    private int dayAbbrTextView;
    private int dateTimeTextView;
    private int tideStatusTextView;
    private int tideTimeTextView;

    // CLASS FIELDS
    Cursor cursor;
    TideSQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get references to UI objects
        dayAbbrTextView = R.id.dayAbbrv;
        dateTimeTextView = R.id.dateTime;
        tideStatusTextView = R.id.tideStatus;
        tideTimeTextView = R.id.tideTime;

        // Get intent sent from main activity
        Intent intent = getIntent();
        int dayOfMonth = intent.getExtras().getInt(MainActivity.DAY);
        int monthOfYear = intent.getExtras().getInt(MainActivity.MONTH);
        String location = intent.getExtras().getString(MainActivity.LOCATION);

        // Set TideSQLiteHelper object
        // Build id object
        // Load table data for select location
        this.sqLiteHelper = new TideSQLiteHelper(this);
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        sqLiteHelper.loadDbFromXML(db);
        Date selectedDate = new Date(Calendar.YEAR, monthOfYear, dayOfMonth);
        cursor = this.sqLiteHelper.getTideTableByDate(db, selectedDate, location);

        // TEMP LOGGING
        if(cursor != null && cursor.getCount() >0) {
            Log.d("APPLICATION", "the cursor is not empty!");
        }


        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.list_layout,
                cursor,
                new String[]{TideSQLiteHelper.DAY,
                        TideSQLiteHelper.DATE,
                        TideSQLiteHelper.GET_LOW,
                        TideSQLiteHelper.TIDE_TIME,
                },
                new int[]{
                        dayAbbrTextView,
                        dateTimeTextView,
                        tideStatusTextView,
                        tideTimeTextView
                },
                0 );	// no flags
        setListAdapter(adapter);
    }
 */




   /* @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // Toast.makeText(this, "Tide will have a peak height of " + cursor.get get(i).get(TIDE_HEIGHT_CM) + "cm", Toast.LENGTH_SHORT).show();
    }*/

   /*
   DISPLAYING ENTIRE TIDE LIST
   cursor = db.rawQuery("SELECT * FROM " + TideSQLiteHelper.TIDE_PREDICTIONS, null);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.list_layout,
                cursor,
                new String[]{TideSQLiteHelper.DAY,
                        TideSQLiteHelper.DATE,
                        TideSQLiteHelper.GET_LOW,
                        TideSQLiteHelper.TIDE_TIME,
                },
                new int[]{
                        dayAbbrTextView,
                        dateTimeTextView,
                        tideStatusTextView,
                        tideTimeTextView
                },
                0 );	// no flags
        setListAdapter(adapter);
    */
}
