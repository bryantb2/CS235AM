package com.example.piggameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.renderscript.Sampler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //CLASS FIELDS
    private PigGame pigGame;
    private EditText player1Username;
    private EditText player2Username;
    private TextView player1ScoreLabel;
    private TextView player2ScoreLabel;
    private ImageView dieImage;
    private TextView currentPlayerLabel;
    private TextView pointsAccumulatorLabel;
    private Button rollDieButton;
    private Button endTurnButton;
    private Button newGameButton;

    //LIFECYCLES
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //GETTING UI ELEMENTS
        this.player1Username = findViewById(R.id.player1Username_TextEntry);
        this.player1ScoreLabel = findViewById(R.id.player1Score_Label);
        this.player2ScoreLabel = findViewById(R.id.player2Score_Label);
        this.player2Username = findViewById(R.id.player2Username_TextEntry);
        this.currentPlayerLabel = findViewById(R.id.currentPlayer_Label);
        this.dieImage = findViewById(R.id.dieRollResult_Label);
        this.pointsAccumulatorLabel = findViewById(R.id.runningPointsTotal_Label);
        this.rollDieButton = findViewById(R.id.rollDie_Button);
        this.endTurnButton = findViewById(R.id.endTurn_Button);
        this.newGameButton = findViewById(R.id.newGame_Button);
    }

    //EVENT HANDLERS
    public void RollButtonClick() {

    }

    public void EndTurnButtonClick() {

    }

    public void NewGameButtonClick() {
        //get the text from both edit text fields
        //if data in fields is valid, create PigGame object

    }

    //METHODS
    public void UpdateDieImage(int rolledNumber) {

    }

    private void UpdatePlayerScore(int playerNumber, int score) {
        if(AreUsernamesValid() != false) {
            if(playerNumber==1) {
                this.player1ScoreLabel.setText(String.valueOf(score));
            }
            else {
                this.player2ScoreLabel.setText(String.valueOf(score));
            }
        }
    }

    private void UpdateCurrentPlayer(String currentPlayerUsername) {
        this.currentPlayerLabel.setText(currentPlayerUsername);
    }

    private void UpdatePointsRunningTotal(int points) {
        this.pointsAccumulatorLabel.setText(String.valueOf(points));
    }

    private boolean AreUsernamesValid() {
        String player1Username = this.player1Username.getText().toString();
        String player2Username = this.player2Username.getText().toString();
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
