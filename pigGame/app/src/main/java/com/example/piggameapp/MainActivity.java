package com.example.piggameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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

import java.io.InputStream;

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
        //disable buttons until a new game has been created
        this.DisableRollButton();
        this.DisableEndTurnButton();
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
                NewGame();
            }
        });
        this.endTurnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EndTurn();
            }
        });
        this.rollDieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Roll();
            }
        });
    }

    //EVENT HANDLERS
    public void Roll() {
        //THIS METHOD ADDS TO PLAYER RUNNING TOTAL AND CALCULATES SCORE -------------------------------------
        //lock roll button
        //roll pigGame die
        //if rolled number is not 8
            //set the die image
            //update running total label
        //else reset currentPlayerUI elements
            //execute endturn methods

        this.DisableRollButton();
        int rollResult = this.pigGame.RollAndCalc();
        this.UpdateDieImage(rollResult);
        if(rollResult != 8) {
            this.UpdatePointsRunningTotal(this.pigGame.getPointsForCurrentTurn());
        }
        else {
            this.UpdatePlayerScore(this.pigGame.getCurrentPlayerNumber(),0);
            this.ResetRunningTotalLabel();
            Toast.makeText(this,("Ouch, "+this.pigGame.getCurrentPlayerName()+" has rolled an 8!"),Toast.LENGTH_LONG);
            this.EndTurn();
        }
        this.EnableRollButton();
    }

    public void EndTurn() {
        //THIS METHOD ADDS TO SCORE AND DETERMINES IF THERE IS A WINNER -------------------------------------
        //lock roll button
        //move running total to total points label
        //calc winner
            //if current player is winner, display name in turn label
                //give toast message saying click newgame to start again
            //else switch current player to next player
                //unlock roll button
        this.DisableRollButton();
        this.DisableEndTurnButton();
        int playerThatJustWent = this.pigGame.getCurrentPlayerNumber();
        int winnerNumber = this.pigGame.EndTurn();
        this.UpdatePlayerScore(playerThatJustWent,this.pigGame.getPlayerScore(playerThatJustWent));
        this.UpdatePointsRunningTotal(0);
        if(winnerNumber != 0) {
            //display winner on UI
            this.DisplayWinner(winnerNumber);
            //disable game buttons
            this.DisableEndTurnButton();
            this.DisableRollButton();
            //display toast message
            Toast.makeText(getApplicationContext(), (this.pigGame.getPlayerName(winnerNumber) + " has won!"),Toast.LENGTH_LONG);
        }
        else {
            //end turn auto switches the player turn if there is no winner, so all we have to do here reflect the change in the UI
            //re-enable the endturn button
            //re-enable the roll button
            this.UpdateCurrentPlayer();
            this.EnableEndTurnButton();
            this.EnableRollButton();
        }
    }

    public void NewGame() {
        //execute this block if the app was already running
        //delete existing usernames in fields
        //set game bool to not active
        //reset score labels to zero
        //reset runningtotal label to zero
        //reset current player label
        if(gameInProgress==true) {
            this.ResetUsernameFields(); //this is what prevents the second block in this handler from firing
            this.ResetScoreLabels();
            this.ResetRunningTotalLabel();
            this.ResetCurrrentPlayerLabel();
            this.gameInProgress = false;
            this.EnableUsernameEntryFields();
            Toast.makeText(getApplicationContext(),"Please enter valid usernames, press new game",Toast.LENGTH_LONG).show();
        }
        //execute this block if the app was not running
        //get the text from both edit text fields
        //if data in fields is valid, reset PigGame object
        //pass in usernames and die size
        //display current player's turn
        //unlock the roll and endturn buttons
        if(AreUsernamesValid() == true) {
            this.DisableUsernameEntryFields();
            this.player1Name = this.player1UsernameTextEntry.getText().toString();
            this.player2Name = this.player2UsernameTextEntry.getText().toString();
            if(this.pigGame != null) {
                //checking if the object was instantiated
                this.pigGame.RestartGame(player1Name,player2Name);
            }
            else {
                this.pigGame = new PigGame(player1Name,player2Name,8);
            }
            this.gameInProgress = true;
            this.UpdateCurrentPlayer();
            this.EnableRollButton();
            this.EnableEndTurnButton();
            Toast.makeText(getApplicationContext(),"New game started, good luck!",Toast.LENGTH_LONG).show();
            Log.d("pigGameUILayer","inside newGameButtonClick method, usernames valid");
        }
        Log.d("pigGameUILayer","inside newGameButtonClick method, usernames NOT valid");
    }

    private void DisplayWinner(int winnerNumber) {
        //set the display points label to the name of the winner
        this.pointsAccumulatorLabel.setText(this.pigGame.getPlayerName(winnerNumber) + " has won!");
    }

    private void EnableEndTurnButton() {
        this.endTurnButton.setEnabled(true);
    }

    private void DisableEndTurnButton() {
        this.endTurnButton.setEnabled(false);
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
        int referenceID = 0;
        switch(rolledNumber) {
            case 1: {
                referenceID = R.drawable.die8side1;
                break;
            }
            case 2: {
                referenceID = R.drawable.die8side2;
                break;
            }
            case 3: {
                referenceID = R.drawable.die8side3;
                break;
            }
            case 4: {
                referenceID = R.drawable.die8side4;
                break;
            }
            case 5: {
                referenceID = R.drawable.die8side5;
                break;
            }
            case 6: {
                referenceID = R.drawable.die8side6;
                break;
            }
            case 7: {
                referenceID = R.drawable.die8side7;
                break;
            }
            case 8: {
                referenceID = R.drawable.die8side8;
                break;
            }
            default: {
                referenceID = -1;
                break;
            }
        }
        Drawable drawableImage = getResources().getDrawable(referenceID);
        this.dieImage.setImageDrawable(drawableImage);
    }

    private void UpdatePlayerScore(int playerNumber, int score) {
        if(playerNumber==1) {
            this.player1ScoreLabel.setText(String.valueOf(score + " Points"));
        }
        else {
            this.player2ScoreLabel.setText(String.valueOf(score + " Points"));
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

    private void UpdateCurrentPlayer() {
        this.currentPlayerLabel.setText(this.pigGame.getCurrentPlayerName()+"'s turn");
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
