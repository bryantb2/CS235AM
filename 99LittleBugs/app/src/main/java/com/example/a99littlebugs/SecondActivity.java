package com.example.a99littlebugs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Getting intent sent from MainActivity
        Intent intent = getIntent();

        // Creating intent result to be sent back to first activity
        Intent result = new Intent(this, MainActivity.class);
        setResult(Activity.RESULT_OK, );
    }
}
