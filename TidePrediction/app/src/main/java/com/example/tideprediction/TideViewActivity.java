package com.example.tideprediction;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class TideViewActivity extends ListActivity {

    // UI ELEMENTS
    private int dayAbbrTextView;
    private int dateTimeTextView;
    private int tideStatusTextView;
    private int tideTimeTextView;

    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get references to UI objects
        dayAbbrTextView = R.id.dayAbbrv;
        dateTimeTextView = R.id.dateTime;
        tideStatusTextView = R.id.tideStatus;
        tideTimeTextView = R.id.tideTime;

        // Initialize database
        TideSQLiteHelper helper = new TideSQLiteHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

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
    }

   /* @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // Toast.makeText(this, "Tide will have a peak height of " + cursor.get get(i).get(TIDE_HEIGHT_CM) + "cm", Toast.LENGTH_SHORT).show();
    }*/
}
