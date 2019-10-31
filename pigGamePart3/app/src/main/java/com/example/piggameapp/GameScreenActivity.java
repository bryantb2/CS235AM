package com.example.piggameapp;

import android.app.Activity;
import android.os.Bundle;

public class GameScreenActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //show fragment class as main content
        //MUST DECLARE ACTIVITY IN MANIFEST

        /*
        adds fragment to activity by
            1. opening up a transaction between the fragment and activity
            2. replacing the content in the activity with the fragment object
            3. committing those changes to the new activity
         */
        setContentView(R.layout.game_screen);
    }
}
