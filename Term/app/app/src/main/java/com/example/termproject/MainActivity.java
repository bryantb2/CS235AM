package com.example.termproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.lang.reflect.Field;

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
        BuildLayoutSpinners();
    }


    private void BuildLayoutSpinners() {
        // UI property constants
        final String spinnerIdDbPrefix = "db";
        final String spinnerIdBackendPrefix = "backend";
        final String spinnerIdFrontendPrefix = "frontend";
        final String spinnerIdGeneralPrefix = "general";
        final String spinnerIdMiddle = "QuestionSpinner";

        // XML string constants
        final String dbArrayPrefix = "db_";
        final String backendArrayPrefix = "backEnd_";
        final String frontendArrayPrefix = "frontEnd_";
        final String generalArrayPrefix = "general_";

        // Build adapter from XML array and set to UI element
        boolean stillDBQuestions = true;
        boolean stillBackendQuestions = true;
        boolean stillFrontendQuestions = true;
        boolean stillGeneralQuestions = true;
        while(stillGeneralQuestions == true || stillBackendQuestions == true || stillDBQuestions ==true
                || stillFrontendQuestions == true) {
            final int counterStartingPosition = 1;
            // Set general questions first
            if(stillGeneralQuestions != false) {
                GenerateAdapter(counterStartingPosition,generalArrayPrefix,spinnerIdGeneralPrefix,spinnerIdMiddle);
                stillGeneralQuestions = false;
            }
            // Set frontend questions second
            else if(stillFrontendQuestions != false) {
                GenerateAdapter(counterStartingPosition,frontendArrayPrefix,spinnerIdFrontendPrefix,spinnerIdMiddle);
                stillFrontendQuestions = false;
            }
            // Set backend questions third
            else if(stillBackendQuestions != false) {
                GenerateAdapter(counterStartingPosition,backendArrayPrefix,spinnerIdBackendPrefix,spinnerIdMiddle);
                stillBackendQuestions = false;
            }
            // Set DB questions fourth
            else {
                GenerateAdapter(counterStartingPosition,dbArrayPrefix,spinnerIdDbPrefix,spinnerIdMiddle);
                stillDBQuestions = false;
            }

        }

    }

    private void GenerateAdapter(int initialCounterValue, String arrayPrefix, String spinnerIdPrefix, String spinnerIdMiddle) {
        // check to see if first question in array XML exists
        // if not null
            // increment counter
            // set resource id
            // get UI spinner id and set adapter
        boolean areThereStillAQuestions = true;
        int counter = initialCounterValue;
        while(areThereStillAQuestions == true) {
            String arrayName = arrayPrefix + Integer.toString(counter);
            int id = getResources().getIdentifier(arrayName,"array", getPackageName());
            if(id == 0) {
                // id of zero means that the resource does not exist
                // breaks out of this function (since it has done all that it can
                return;
            }
            // Get UI spinner element
            String spinnerName = spinnerIdPrefix + spinnerIdMiddle+ Integer.toString(counter);
            Spinner UISpinner = (Spinner)findViewById(getResources().getIdentifier(spinnerName, "id", getPackageName()));

            // Fill spinner content
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    this, id, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item);
            UISpinner.setAdapter(adapter);
            UISpinner.setSelection(0);

            // increment resource counter
            counter+= 1;
        }
        return; // this is just for java, will never be returned from here
    }
}
