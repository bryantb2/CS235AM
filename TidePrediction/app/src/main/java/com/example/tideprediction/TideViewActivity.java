package com.example.tideprediction;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;

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

        // Determine file name based off location
        String filename = "";
        if(location == MainActivity.FLORENCE)
            filename = "FLORENCE";
        else if(location == MainActivity.ASTORIA)
            filename = "ASTORIA";
        else
            filename = "NEWPORT";

        // Get access to DB items via Dal
        // Load DB with values
        // Retrieve values
        Dal dal = new Dal(this);
        // TODO: UNCOMMENBT THIS: dal.loadDbFromXML(filename);
        // TODO: RETRIEVE VALUES FROM DB

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
