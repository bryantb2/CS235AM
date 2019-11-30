package com.example.tideprediction;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements DatePicker.OnDateChangedListener {

    // UI ELEMENTS
    Spinner locationDropDown;
    Button showTideButton;
    DatePicker dateSelector;

    // STRING INTENT CONSTANTS
    public static final String LOCATION = "LOCATION";
    public static final String YEAR = "YEAR";
    public static final String MONTH = "MONTH";
    public static final String DAY = "DAY";

    // STRING LOCATION CONSTANTS
    public static final int NUMBER_OF_LOCATIONS= 3;
    public static final String FLORENCE = "florence";
    public static final String NEWPORT = "newport";
    public static final String ASTORIA = "astoria";

    // DATE FIELDS
    private int monthSelected;
    private int daySelected;
    private int yearSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Get UI elements
        this.locationDropDown = findViewById(R.id.locationSelectorSpinner);
        this.showTideButton = findViewById(R.id.showTidesButton);
        this.dateSelector = findViewById(R.id.datePicker);

        // Fill spinner content
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.locationSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        locationDropDown.setAdapter(adapter);
        locationDropDown.setSelection(0);

        // Set date picker UI and event listener
        // then initialize the class-level date-selected fields
        Calendar c = Calendar.getInstance();
        int calendarYear = c.get(Calendar.YEAR);
        int calendarMonth = c.get(Calendar.MONTH);
        int calendarDay = c.get(Calendar.DAY_OF_MONTH);
        dateSelector.init(calendarYear,
                calendarMonth,
                calendarDay, this);
        this.monthSelected = calendarMonth;
        this.daySelected = calendarDay;
        this.yearSelected = calendarYear;

        // Assign button click event
        assignButtonEventHandlers();
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Toast.makeText(getApplicationContext(),"Whoa, date changed!",Toast.LENGTH_LONG).show();
        this.monthSelected = monthOfYear;
        this.daySelected = dayOfMonth;
        this.yearSelected = year;
    }

    private void assignButtonEventHandlers() {
        this.showTideButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Get the selected spinner value
                int selectedLocationPosition = locationDropDown.getSelectedItemPosition();

                // Derive the selection string
                String locationName = "";
                if(selectedLocationPosition == 0)
                    locationName = FLORENCE;
                else if(selectedLocationPosition == 1)
                    locationName = NEWPORT;
                else
                    locationName = ASTORIA;

                // Show toast for selected item
                Toast.makeText(getApplicationContext(), "You selected " + locationName, Toast.LENGTH_SHORT).show();

                Log.d("TIDE_LOG",locationName);

                // Send intent for second activity
                Intent intent = new Intent(getApplicationContext(),TideViewActivity.class);
                intent.putExtra(LOCATION, locationName);
                intent.putExtra(YEAR, yearSelected);
                intent.putExtra(MONTH, monthSelected);
                intent.putExtra(DAY, daySelected);

                startActivity(intent);
            }
        });
    }

}
