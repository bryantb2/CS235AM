package com.example.piggameapp;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TitleScreenActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //show fragment class as main content
        //MUST DECLARE ACTIVITY IN MANIFEST

        /*
        adds fragment to activity by
            1. opening up a transaction between the fragment and activity
            2. replacing the content in the activity with the fragment object
            3. committing those changes to the new activity
         */
       /* getFragmentManager().beginTransaction()
                .replace(android.R.id.content,new TitleScreenFragment())
                .commit();
        }*/
        setContentView(R.layout.title_screen);
    }

    public void createPigGame(String player1Name, String player2Name, int dieSize, int numberOfDie, int maxGameScore, boolean onNewGameClicked) {
        // Checks to see if the newGame button was clicked
            // get a reference to the game fragment and pass in the game parameters/settings
        if(onNewGameClicked == true) {
            GameScreenFragment gameFragment = (GameScreenFragment)getFragmentManager()
                    .findFragmentById(R.id.game_fragment);
            gameFragment.BuildGame(player1Name,player2Name,dieSize,numberOfDie,maxGameScore);
        }
    }
}
