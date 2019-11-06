package com.example.a99littlebugs;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // CLASS FIELDS
    private Button takeOneDown;
    private Button takeTwoDown;
    private TextView messageBox;

    private int numberOfBugs = 100;

    static final int MAIN_ACTIVITY_REQUEST = 1;
    static final String BUG_NUMBER = "BUG_NUMBER";
    static final String BUTTON_NUMBER = "BUTTON_NUMBER";
    static final String NEW_BUG_NUMBER = "NEW_BUG_NUMBER";

    // LIFECYCLE METHODS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Getting references to UI widgets
        this.takeOneDown = findViewById(R.id.takeOneDown_Button);
        this.takeTwoDown = findViewById(R.id.takeTwoDown_Button);
        this.messageBox = findViewById(R.id.messageTextView);

        // ASSIGN EVENT LISTENERS
        CreateUIEventListeners();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    // EVENT LISTENERS
    private void CreateUIEventListeners() {
        this.takeOneDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent
                // Start activity to receive a result back
                secondActivityLauncher(1);
            }
        });

        this.takeTwoDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent
                // Start activity to receive a result back
                secondActivityLauncher(2);
            }
        });
    }

    // ACTIVITY LAUNCHER
    private void secondActivityLauncher(int buttonNumber) {
        Intent secondActivity = new Intent(this, TheCode.class);
        secondActivity.putExtra(BUTTON_NUMBER,buttonNumber);
        secondActivity.putExtra(BUG_NUMBER,this.numberOfBugs);
        startActivityForResult(secondActivity, MAIN_ACTIVITY_REQUEST);
        if(buttonNumber == 1) {
            showUIMessage("Take one down");
        }
        else {
            showUIMessage("Take two down");
        }
    }

    //UI METHODS
    private void showUIMessage(String messageContent) {
        Toast.makeText(this,messageContent, Toast.LENGTH_LONG).show();
    }
}
