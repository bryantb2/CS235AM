package com.example.tipcalculator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsActivityFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //loads preferences from the XML files
        addPreferencesFromResource(R.xml.preferences);
    }
}
