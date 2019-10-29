package com.example.tipcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //LIFECYCLE METHODS

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //mounting the XML to the main activity
        //will convert the XML items to java objects that can be displayed in the activity
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            //determines which menu item was selected based off element IDs
            case R.id.about:
                Toast.makeText(this,"About", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.settings:
                Toast.makeText(this,"Settings", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                return true;
            default:
                //allows for default OptionsItemSelected behavior from the super class
                return super.onOptionsItemSelected((item));
        }
    }
}

