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
        /*boolean stillDBQuestions = true;
        boolean stillBackendQuestions = true;
        boolean stillFrontendQuestions = true;*/
        boolean stillGeneralQuestions = true;
        while(stillGeneralQuestions == true /*|| stillBackendQuestions == true || stillDBQuestions ==true
                || stillFrontendQuestions == true*/) {
            int counter = 1;
            // Set general questions first
            if(stillGeneralQuestions != false) {
                // check to see if first question in array XML exists
                    // if not null
                        // increment counter
                        // set resource id
                        // get UI spinner id and set adapter
                // continue this process until null, then set stillGeneralQuestions to false
                while(stillGeneralQuestions == true) {
                    String arrayName = generalArrayPrefix + Integer.toString(counter);
                    int id = getResources().getIdentifier(arrayName,"array", getPackageName());
                    if(id == 0) {
                        // id of zero means that the resource does not exist
                        // sets the generalQuestions bool to false
                        // breaks out of this inner loop
                        stillGeneralQuestions = false;
                        break;
                    }
                    // Get UI spinner element
                    String spinnerName = spinnerIdGeneralPrefix + spinnerIdMiddle+ Integer.toString(counter);
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
            }/*
            // Set frontend questions second
            else if(stillFrontendQuestions != false) {

            }
            // Set backend questions third
            else if(stillBackendQuestions != false) {

            }
            // Set DB questions fourth
            else {

            }*/

        }

    }
}
