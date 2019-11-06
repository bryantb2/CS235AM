package com.example.a99littlebugs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TheCode extends AppCompatActivity {
    // CLASS FIELDS
    private Button patchBugs;

    private int numberOfBugs;
    private int buttonNumberClicked;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // Getting references to UI widgets
        this.patchBugs = findViewById(R.id.patchItAround_Button);

        // Getting intent sent from MainActivity
        Intent intent = getIntent();

        // Getting number of bugs left
        // Getting the button clicked from first activity
        this.numberOfBugs = intent.getExtras().getInt(MainActivity.BUG_NUMBER);
        this.buttonNumberClicked = intent.getExtras().getInt(MainActivity.BUTTON_NUMBER);

        // ASSIGN EVENT LISTENERS
        CreateUIEventListeners();
    }

    // EVENT LISTENERS
    private void CreateUIEventListeners() {
        this.patchBugs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent
                // Start first activity to send a result back
                int newBugNumber = subtractBugs(buttonNumberClicked,numberOfBugs);
                firstActivityLauncher(newBugNumber);
            }
        });
    }

    // ACTIVITY LAUNCHER
    private void firstActivityLauncher(int newBugNumber) {
        // Creating intent result to be sent back to first activity
        Intent result = new Intent(this, MainActivity.class);
        result.putExtra(MainActivity.BUG_NUMBER, newBugNumber);
        result.putExtra(MainActivity.NEW_BUG_FIX_MESSAGE,String.valueOf(newBugNumber) + " little bugs in the code");
        setResult(MainActivity.MAIN_ACTIVITY_REQUEST,result);
        showUIMessage(String.valueOf(newBugNumber) + " littlebugs in the code");
        finish();
    }

    // BASIC LOGIC METHODS
    private int subtractBugs(int numberSubtracted, int numberOfBugs) {
        return (numberOfBugs - numberSubtracted);
    }

    //UI METHODS
    private void showUIMessage(String messageContent) {
        Toast.makeText(this,messageContent, Toast.LENGTH_LONG).show();
    }

}
