package com.example.termproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    // WIDGET REFERENCES
    Button calculateButton;
    Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calculateButton = findViewById(R.id.calculateButton);
        resetButton = findViewById(R.id.resetButton);

        // Dynamically build out array adapters
    }


    private void BuildLayoutSpinners() {
        // UI property constants
        final String spinnerIdDbPrefix = "db";
        final String spinnerIdBackendPrefix = "backend";
        final String spinnerIdFrontendPrefix = "frontend";
        final String spinnerIdMiddle = "backendQuestion";

        // Build adapter from XML array and set to UI element
        boolean stillDBQuestions = true;
        boolean stillBackendQuestions = true;
        boolean stillFrontendQuestions = true;
        boolean stillGeneralQuestions = true;
        while(stillGeneralQuestions == true || stillBackendQuestions == true || stillDBQuestions ==true
                || stillFrontendQuestions == true) {
            // Set general questions first
            if(stillGeneralQuestions != false) {

            }
            // Set frontend questions second
            else if(stillFrontendQuestions != false) {

            }
            // Set backend questions third
            else if(stillBackendQuestions != false) {

            }
            // Set DB questions fourth
            else {

            }

        }

    }
}
