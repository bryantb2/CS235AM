package com.example.a99littlebugs;

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

    static final int FIRST_REQUEST = 1;
    //private final int SECOND_REQUEST = 2;

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



    // EVENT LISTENERS
    private void CreateUIEventListeners() {
        this.takeOneDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent
                // Start activity to receive a result back
                Intent secondActivity = new Intent(getApplicationContext(), SecondActivity.class);
                startActivityForResult(secondActivity, FIRST_REQUEST);
                showUIMessage("Take one down");
            }
        });

        this.takeTwoDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUIMessage("Take two down");
            }
        });
    }

    //UI METHODS
    private void showUIMessage(String messageContent) {
        Toast.makeText(this,messageContent, Toast.LENGTH_LONG).show();
    }

}
