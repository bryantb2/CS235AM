package com.example.termproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // PUBLIC STATIC CONSTANTS
    public static final String QUIZ_LOGIC = "QUIZ_LOGIC";

    // UI property constants
    private final String spinnerIdDbPrefix = "db";
    private final String spinnerIdBackendPrefix = "backend";
    private final String spinnerIdFrontendPrefix = "frontend";
    private final String spinnerIdGeneralPrefix = "general";
    private final String spinnerIdMiddle = "QuestionSpinner";

    // XML string constants
    private final String dbArrayPrefix = "db_";
    private final String backendArrayPrefix = "backEnd_";
    private final String frontendArrayPrefix = "frontEnd_";
    private final String generalArrayPrefix = "general_";

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

        // Assign event handlers
        AssignButtonEventHandlers();
    }

    // EVENT LISTENER ASSIGNMENT
    private void AssignButtonEventHandlers() {
        this.calculateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast message = Toast.makeText(getApplication(), "Please make sure to answer all questions!", Toast.LENGTH_SHORT);
                if(AreSpinnerAnswersValid() != true)
                    message.show();
                else {
                    // open up second activity
                    LaunchResultsActivity();
                }
            }
        });

        this.resetButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                BuildLayoutSpinners(); // defacto reset
                Toast.makeText(getApplication(), "Answers have been reset!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ADAPTER GENERATOR METHODS
    private void BuildLayoutSpinners() {
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
                stillGeneralQuestions = false; // means that this particular section of questions has now been inflated with array data
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
                // code has successfully finished building adapters if it gets here
                areThereStillAQuestions = false;
            }
            else {
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
        }
    }

    // SPINNER ANSWER CHECKER METHODS
    private boolean AreSpinnerAnswersValid() {
        // Get adapters from UI and check their selected positions
        boolean stillDBQuestions = true;
        boolean stillBackendQuestions = true;
        boolean stillFrontendQuestions = true;
        boolean stillGeneralQuestions = true;

        while(stillGeneralQuestions == true || stillBackendQuestions == true || stillDBQuestions ==true
                || stillFrontendQuestions == true) {
            final int counterStartingPosition = 1;
            // Set general questions first
            if(stillGeneralQuestions != false) {
                boolean areAnswered = IsCategoryOfSpinnersValid(counterStartingPosition,spinnerIdGeneralPrefix,spinnerIdMiddle);
                if(areAnswered != true)
                    return false;
                stillGeneralQuestions = false; // means that this particular section of questions has been checked AND IS VALID
            }
            // Set frontend questions second
            else if(stillFrontendQuestions != false) {
                boolean areAnswered = IsCategoryOfSpinnersValid(counterStartingPosition,spinnerIdFrontendPrefix,spinnerIdMiddle);
                if(areAnswered != true)
                    return false;
                stillFrontendQuestions = false; // means that this particular section of questions has been checked AND IS VALID
            }
            // Set backend questions third
            else if(stillBackendQuestions != false) {
                boolean areAnswered = IsCategoryOfSpinnersValid(counterStartingPosition,spinnerIdBackendPrefix,spinnerIdMiddle);
                if(areAnswered != true)
                    return false;
                stillBackendQuestions = false; // means that this particular section of questions has been checked AND IS VALID
            }
            // Set DB questions fourth
            else {
                boolean areAnswered = IsCategoryOfSpinnersValid(counterStartingPosition,spinnerIdDbPrefix,spinnerIdMiddle);
                if(areAnswered != true)
                    return false;
                stillDBQuestions = false; // means that this particular section of questions has been checked AND IS VALID
            }
        }
        return true; // returns true if ALL answers are valid
    }

    private boolean IsCategoryOfSpinnersValid(int initialCounterValue, String spinnerIdPrefix, String spinnerIdMiddle) {
        // check UI to see if question exists
            // if not null
                // increment counter
                // get UI resource id
                // determine if the 0th position has been selected
                // return false if any question has the 0th position selected (meaning no answer)
        boolean areThereStillAQuestions = true;
        int counter = initialCounterValue;
        while(areThereStillAQuestions == true) {
            String spinnerName = spinnerIdPrefix + spinnerIdMiddle+ Integer.toString(counter);
            int id = getResources().getIdentifier(spinnerName,"id", getPackageName());
            if(id == 0) {
                // id of zero means that the resource does not exist
                // if the code gets here, it means all questions have been checked and ARE VALID!
                areThereStillAQuestions = false;
            }
            else {
                // Get UI spinner element
                Spinner UISpinner = (Spinner)findViewById(getResources().getIdentifier(spinnerName, "id", getPackageName()));

                // Test to see if UI spinner's 0th position is selected as the answer
                int selectedSpinnerPosition = UISpinner.getSelectedItemPosition();
                if(selectedSpinnerPosition == 0)
                    return false; // returns false because question has not been answered

                // increment resource counter
                counter+= 1;
            }
        }
        return true; // if loop has been broken out of, then all questions are valid
    }

    // QUIZ METHODS
    private void LaunchResultsActivity() {
        // instantiate quiz object
            // parse and pass in results
        // create intent and add the quiz object as an extra
        // start second activity
        ArrayList<ArrayList<String>> parsedQuizAnswers = ParseQuizResults();
        QuizLogic quiz = new QuizLogic();
        quiz.PassNCalcTestResults(parsedQuizAnswers);

        Intent intent = new Intent(getApplicationContext(),ResultsActivity.class);
        intent.putExtra(QUIZ_LOGIC,quiz);
        startActivity(intent);
    }

    private ArrayList<ArrayList<String>> ParseQuizResults() {
        // method will return a set of parallel ArrayLists
            // first Arraylist will be the results
            // second Arraylist will be the corresponding section tags
        // loops through each section and concatenates each response position to an ArrayList of ArrayLists
        ArrayList<String> sectionTags = new ArrayList<String>();
            sectionTags.add(QuizLogic.GENERAL_SECTION);
            sectionTags.add(QuizLogic.FRONT_END_SECTION);
            sectionTags.add(QuizLogic.BACK_END_SECTION);
            sectionTags.add(QuizLogic.DB_SECTION);
        ArrayList<ArrayList<String>> arrOfQuizAnswers = new ArrayList<ArrayList<String>>();

        final int counterStartingPosition = 1;
        for(int i = 0; i <sectionTags.size(); i++) {
            // Parse general section and add results to
            if(sectionTags.get(i) == QuizLogic.GENERAL_SECTION) {
                ArrayList<String> parsedGeneralSection = ParseAndReturnQuizSection(counterStartingPosition,spinnerIdGeneralPrefix,spinnerIdMiddle);
                arrOfQuizAnswers.add(parsedGeneralSection);
            }
            // Set frontend questions second
            else if(sectionTags.get(i) == QuizLogic.FRONT_END_SECTION) {
                ArrayList<String> parsedFrontendSection = ParseAndReturnQuizSection(counterStartingPosition,spinnerIdFrontendPrefix,spinnerIdMiddle);
                arrOfQuizAnswers.add(parsedFrontendSection);
            }
            // Set backend questions third
            else if(sectionTags.get(i) == QuizLogic.BACK_END_SECTION) {
                ArrayList<String> parsedBackendSection = ParseAndReturnQuizSection(counterStartingPosition,spinnerIdBackendPrefix,spinnerIdMiddle);
                arrOfQuizAnswers.add(parsedBackendSection);
            }
            // Set DB questions fourth
            else {
                ArrayList<String> parsedDBSection = ParseAndReturnQuizSection(counterStartingPosition,spinnerIdDbPrefix,spinnerIdMiddle);
                arrOfQuizAnswers.add(parsedDBSection);
            }
        }

        // add parsed quiz answers to final array
        // add section tag list to final array
        arrOfQuizAnswers.add(sectionTags);
        Log.d("MAIN_ACTIVITY","question parsing successful!");
        return arrOfQuizAnswers; // every ArrayList, except the last, is a series of quiz answers
    }

    private ArrayList<String> ParseAndReturnQuizSection(int initialCounterValue, String spinnerIdPrefix, String spinnerIdMiddle) {
        // check UI to see if question exists
            // if not null
            // increment counter
            // get UI resource id
            // add selected position to tempArrList
        boolean areThereStillAQuestions = true;
        int counter = initialCounterValue;
        ArrayList<String> tempArrList = new ArrayList<String>();
        while(areThereStillAQuestions == true) {
            String spinnerName = spinnerIdPrefix + spinnerIdMiddle+ Integer.toString(counter);
            int id = getResources().getIdentifier(spinnerName,"id", getPackageName());
            if(id == 0) {
                // id of zero means that the resource does not exist
                // if the code gets here, it means all questions have been parsed
                areThereStillAQuestions = false;
            }
            else {
                // Get UI spinner element
                Spinner UISpinner = (Spinner)findViewById(getResources().getIdentifier(spinnerName, "id", getPackageName()));

                // Get and set selectedPosition to ArrayList
                int selectedSpinnerPosition = UISpinner.getSelectedItemPosition();
                tempArrList.add(Integer.toString(selectedSpinnerPosition));
                Log.d("MAIN_ACTIVITY","Logging parsed integer location for question "+counter+":"+selectedSpinnerPosition);
                // increment resource counter
                counter+= 1;
            }
        }
        return tempArrList; // if loop has been broken out of, then all questions have been parsed
    }
}
