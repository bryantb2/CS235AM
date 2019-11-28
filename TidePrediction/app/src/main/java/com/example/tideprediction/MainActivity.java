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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.Intent;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    // UI ELEMENTS
    Spinner locationDropDown;
    Button showTideButton;

    // STRING CONSTANTS
    public final String LOCATION = "LOCATION";
    public final String FLORENCE = "Florence";
    public final String NEWPORT = "Newport";
    public final String ASTORIA = "Astoria";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Get UI elements
        this.locationDropDown = findViewById(R.id.locationSelectorSpinner);
        this.showTideButton = findViewById(R.id.showTidesButton);

        // Fill spinner content
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.locationSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        locationDropDown.setAdapter(adapter);
        locationDropDown.setSelection(0);

        // Assign button click event
        assignButtonEventHandlers();
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

                // Send intent for second activity
                Intent intent = new Intent(getApplicationContext(),TideViewActivity.class);
                intent.putExtra(LOCATION, locationName);

                startActivity(intent);
            }
        });
    }

    /*@Override
    public void onClick(View v) {

        // Create intent to launch location tideItem activity
        /*Intent intent = new Intent(this, TideItemActivity.class);

        if (v.getId() == R.id.viewTideButton) {
            // Send to second activity with intent for city
            int location = locationSpinner.getSelectedItemPosition();
            switch (location) {
                case 0:
                    locationSelection = "Florence";
                    break;
                case 1:
                    locationSelection = "Cape Disappointment";
                    break;
                case 2:
                    locationSelection = "Yaquina";
                    break;
            }


            intent.putExtra("locationSelection", locationSelection);
            startActivity(intent);
        }*/


    //}

}
