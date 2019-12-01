package com.example.termproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class ResultsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_stack_results);

        // Capture intent sent from first activity
        Intent intent = getIntent();
        QuizLogic quiz = (QuizLogic)intent.getSerializableExtra(MainActivity.QUIZ_LOGIC); // get obj using serializable interface (cast to proper type)
    }
}
