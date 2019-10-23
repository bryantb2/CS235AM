package com.example.tipcalculator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;

public class SettingsActivity extends Activity {

    private final int ROUND_NONE = 0;
    private SharedPreferences prefs;
    private boolean remeberTipPercent = true;
    private int rounding = ROUND_NONE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //show fragment class as main content
        //MUST DECLARE ACTIVITY IN MANIFEST
        /*
        adds fragment to activity by
            1. opening up a transaction between the fragment and activity
            2. replacing the content in the activity with the fragment object
            3. commiting those changes to the new activity
         */
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content,new SettingsActivityFragment())
                .commit();

        //setting default values
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false); //this only runs when the app is first started on a device
        prefs = PreferenceManager.getDefaultSharedPreferences(this); //sets the preferences class object to an instance of sharedPreferences
    }

    @Override
    public void onResume() {
        super.onResume();
        remeberTipPercent = prefs.getBoolean("pref_remember_percent", true);
        rounding = Integer.parseInt(prefs.getString("pref_rounding","0"));
    }
}
