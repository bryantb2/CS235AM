package com.example.tipcalculator;

import android.os.Bundle;
import android.app.Activity;

public class SettingsActivity extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //show fragment class as main content
        /*
        adds fragment to activity by
            1. opening up a transaction between the fragment and activity
            2. replacing the content in the activity with the fragment object
            3. commiting those changes to the new activity
         */
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content,new SettingsActivityFragment())
                .commit();
    }
}
