package com.example.a99littlebugs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // CLASS FIELDS
    private Button takeOneDown;
    private Button takeTwoDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Getting references to UI widgets
        this.takeOneDown = findViewById(R.id.takeOneDown_Button);
        this.takeTwoDown = findViewById(R.id.takeTwoDown_Button);

        // ASSIGN EVENT LISTENERS
        CreateUIEventListeners();
    }



    // EVENT LISTENERS
    private void CreateUIEventListeners() {
        this.takeOneDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
