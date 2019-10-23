package com.example.tipcalculator;

import android.os.Bundle;
import android.app.Activity;

public class AboutActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content,new AboutActivityFragment())
                .commit();
    }
}
