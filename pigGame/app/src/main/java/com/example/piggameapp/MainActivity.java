package com.example.piggameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.text.Editable;
import android.text.TextWatcher;
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
    }

    //Event Listener Assignment
    private void GenerateUsernameLabelListeners() {
        this.player1UsernameTextEntry.addTextChangedListener(new TextWatcher() {
            //listener for entry field 1
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(gameInProgress==true) {
                    //forces username back to original state
                    player1UsernameTextEntry.setText(player1Name);
                    //creates message warning user
                    Toast.makeText(getApplicationContext(),"Cannot input username when game is running",Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //listener for entry field 2
        this.player2UsernameTextEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //forces username back to original state
                player2UsernameTextEntry.setText(player2Name);
                //creates message warning user
                Toast.makeText(getApplicationContext(),"Cannot input username when game is running",Toast.LENGTH_LONG);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //EVENT HANDLERS
    public void RollButtonClick() {

    }

    public void EndTurnButtonClick() {

    }

    public void NewGameButtonClick() {
        //delete existing usernames in fields
        //set game bool to not active
        //reset score labels to zero
        //reset runningtotal label to zero
        //reset current player label
        this.ResetUsernameFields();
        this.gameInProgress = false;
        this.ResetScoreLabels();
        this.ResetRunningTotalLabel();
        this.ResetCurrrentPlayerLabel();
        //get the text from both edit text fields
        //if data in fields is valid, create PigGame object
        //pass in usernames and die size
        //display current player's turn
        if(AreUsernamesValid()==true) {
            this.player1Name = this.player1UsernameTextEntry.getText().toString();
            this.player2Name = this.player2UsernameTextEntry.getText().toString();
            this.pigGame = new PigGame(player1Name,player2Name,8);
            this.gameInProgress = true;
            Toast.makeText(this,"New game started, good luck!",Toast.LENGTH_LONG);
        }
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
        this.player1UsernameTextEntry.setText("");
        this.player2UsernameTextEntry.setText("");
    }

    private void UpdateCurrentPlayer(String currentPlayerUsername) {
        this.currentPlayerLabel.setText(currentPlayerUsername);
    }

    private void ResetCurrrentPlayerLabel() {
        this.currentPlayerLabel.setText(this.player1UsernameTextEntry + "'s turn");
    }



    private boolean AreUsernamesValid() {
        String player1Username = this.player1UsernameTextEntry.getText().toString();
        String player2Username = this.player2UsernameTextEntry.getText().toString();
        //if empty
        if(!(player1Username.length() != 0)) {
            Toast.makeText(this,"Player 1 username cannot be empty!",Toast.LENGTH_LONG);
            return false;
        }
        if(!(player2Username.length() != 0)) {
            Toast.makeText(this,"Player 2 username cannot be empty!",Toast.LENGTH_LONG);
            return false;
        }
        return true;
    }



}
