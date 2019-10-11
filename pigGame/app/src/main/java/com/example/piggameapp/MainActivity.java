package com.example.piggameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.renderscript.ScriptGroup;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //CLASS UI FIELDS
    private PigGame pigGame;
    private EditText player1UsernameTextEntry;
    private EditText player2UsernameTextEntry;
    private TextView player1ScoreLabel;
    private TextView player2ScoreLabel;
    private ImageView dieImage;
    private TextView currentPlayerLabel;
    private TextView pointsAccumulatorLabel;
    private Button rollDieButton;
    private Button endTurnButton;
    private Button newGameButton;

    private String player1Name;
    private String player2Name;
    private boolean gameInProgress = false;

    //LIFECYCLES
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //GETTING UI ELEMENTS
        this.player1UsernameTextEntry = findViewById(R.id.player1Username_TextEntry);
        this.player1ScoreLabel = findViewById(R.id.player1Score_Label);
        this.player2ScoreLabel = findViewById(R.id.player2Score_Label);
        this.player2UsernameTextEntry = findViewById(R.id.player2Username_TextEntry);
        this.currentPlayerLabel = findViewById(R.id.currentPlayer_Label);
        this.dieImage = findViewById(R.id.dieRollResult_Label);
        this.pointsAccumulatorLabel = findViewById(R.id.runningPointsTotal_Label);
        this.rollDieButton = findViewById(R.id.rollDie_Button);
        this.endTurnButton = findViewById(R.id.endTurn_Button);
        this.newGameButton = findViewById(R.id.newGame_Button);

        //Methods calls
        CreateUIEventListeners();
    }

    //Event Listener Assigner
    private void CreateUIEventListeners() {
        this.GenerateButtonListeners();
    }

    //Event Listener Assignment Methods
    private void GenerateButtonListeners() {
        this.newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewGameHandler();
            }
        });
        this.endTurnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EndTurnHandler();
            }
        });
        this.rollDieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RollHandler();
            }
        });
    }

    //EVENT HANDLERS
    public void RollHandler() {

    }

    public void EndTurnHandler() {
        //lock roll button
        //add runningtotal to current player score
        //calc winner
            //if current player is winner, display name in turn label
                //give toast message saying click newgame to start again
            //else switch current player to next player
                //unlock roll button
        this.DisableRollButton();
        this.pigGame.
    }

    public void NewGameHandler() {
        //execute this block if the app was already running
        //delete existing usernames in fields
        //set game bool to not active
        //reset score labels to zero
        //reset runningtotal label to zero
        //reset current player label
        if(gameInProgress==true) {
            this.ResetUsernameFields();
            this.ResetScoreLabels();
            this.ResetRunningTotalLabel();
            this.ResetCurrrentPlayerLabel();
            this.gameInProgress = false;
            Toast.makeText(getApplicationContext(),"Please enter valid usernames to begin",Toast.LENGTH_LONG).show();
        }
        //execute this block if the app was not running
        //get the text from both edit text fields
        //if data in fields is valid, create PigGame object
        //pass in usernames and die size
        //display current player's turn
        if(AreUsernamesValid()==true) {
            this.DisableUsernameEntryFields();
            this.player1Name = this.player1UsernameTextEntry.getText().toString();
            this.player2Name = this.player2UsernameTextEntry.getText().toString();
            this.pigGame = new PigGame(player1Name,player2Name,8);
            this.gameInProgress = true;
            Toast.makeText(getApplicationContext(),"New game started, good luck!",Toast.LENGTH_LONG).show();
            Log.d("pigGameUILayer","inside newGameButtonClick method, usernames valid");
        }
        Log.d("pigGameUILayer","inside newGameButtonClick method, usernames NOT valid");
    }

    private void EnableRollButton() {
        this.rollDieButton.setEnabled(true);
    }

    private void DisableRollButton() {
        this.rollDieButton.setEnabled(false);
    }

    private void EnableUsernameEntryFields() {
        this.player1UsernameTextEntry.setEnabled(true);
        this.player2UsernameTextEntry.setEnabled(true);
    }

    private void DisableUsernameEntryFields() {
        this.player1UsernameTextEntry.setEnabled(false);
        this.player2UsernameTextEntry.setEnabled(false);
    }

    //METHODS
    private void UpdateDieImage(int rolledNumber) {

    }

    private void UpdatePlayerScore(int playerNumber, int score) {
        if(playerNumber==1) {
            this.player1ScoreLabel.setText(String.valueOf(score));
        }
        else {
            this.player2ScoreLabel.setText(String.valueOf(score));
        }
    }

    private void ResetScoreLabels() {
        this.player1ScoreLabel.setText("0 total points");
        this.player2ScoreLabel.setText("0 total points");
    }

    private void UpdatePointsRunningTotal(int points) {
        this.pointsAccumulatorLabel.setText(String.valueOf(points));
    }

    private void ResetRunningTotalLabel() {
        this.pointsAccumulatorLabel.setText("0 Points");
    }

    private void ResetUsernameFields() {
        final String defaultPlaceHolderText = "Enter username";
        this.player1UsernameTextEntry.setText(defaultPlaceHolderText);
        this.player2UsernameTextEntry.setText(defaultPlaceHolderText);
    }

    private void UpdateCurrentPlayer(String currentPlayerUsername) {
        this.currentPlayerLabel.setText(currentPlayerUsername);
    }

    private void ResetCurrrentPlayerLabel() {
        this.currentPlayerLabel.setText("Nobody's turn");
    }



    private boolean AreUsernamesValid() {
        String player1Username = this.player1UsernameTextEntry.getText().toString();
        String player2Username = this.player2UsernameTextEntry.getText().toString();
        boolean isValid = true;
        //validation passes if both players have original names that are not the same
        if(player1Username.length() == 0 || player1Username.equals("Enter username")) {
            Toast.makeText(this,"Player 1 needs a valid username!",Toast.LENGTH_LONG).show();
            isValid = false;
        }
        if(player2Username.length() == 0 || player2Username.equals("Enter username")) {
            Toast.makeText(this,"Player 2 needs a valid username!",Toast.LENGTH_LONG).show();
            isValid = false;
        }
        if(player1Username.equals(player2Username)) {
            Toast.makeText(this,"Players cannot have the same username!",Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }



}
