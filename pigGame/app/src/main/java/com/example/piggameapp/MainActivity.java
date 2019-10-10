package com.example.piggameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    //CLASS FIELDS
    private PigGame pigGame = new PigGame();

    //LIFECYCLES
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //EVENT HANDLERS
    public void rollClick() {

    }


    //METHODS




}
