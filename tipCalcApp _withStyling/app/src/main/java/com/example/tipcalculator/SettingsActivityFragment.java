package com.example.tipcalculator;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingsActivityFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

    private SharedPreferences prefs;
    private boolean rememberPercent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //loads preferences from the XML files
        addPreferencesFromResource(R.xml.preferences);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public void onResume() {
        //calls super
        //gets the rememberPercent preference value
        //resets default percent if nothing was assigned?
        //create an event listener for the preferences
        super.onResume();
        rememberPercent = prefs.getBoolean("pref_remember_percent",true);
        //this.setDefaultPercentPreference(rememberPercent);
        prefs.registerOnSharedPreferenceChangeListener(this);
    }
/*
    private void setDefaultPercentPreference(boolean rememberPercent) {
        //get the preference
        Preference defaultPercent = findPreference("pref_default_percent");
        if(rememberPercent) {
            defaultPercent.setEnabled(false);
        }
        else {
            defaultPercent.setEnabled(true);
        }
    }*/

    @Override
    public void onPause() {
        prefs.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if(key.equals("pref_remember_percent")) {
            rememberPercent = prefs.getBoolean(key,true);
        }
        //this.setDefaultPercentPreference(rememberPercent);
    }
}
