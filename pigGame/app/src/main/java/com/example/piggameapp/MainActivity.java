package com.example.piggameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import java.util.Timer;
import java.util.concurrent.TimeUnit;

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

    //these string vars are used to store the desired usernames of players that will be passed into the PigGame object
    //these will not be used after the PigGame object is created
    private String player1Name;
    private String player2Name;
    //tracks if the game is running
    private boolean gameInProgress = false;

    //Class-level sharedPreferences object
    private SharedPreferences savedValues;

    //SAVE STATE KEYS
    private String RUNNING_POINTS_TOTAL = "RUNNING_POINTS_TOTAL";
    private String PLAYER1_USERNAME_KEY = "USERNAME";
    private String PLAYER2_USERNAME_KEY = "USERNAME";
    private String PLAYER1_SCORE_KEY = "PLAYER1_SCORE_KEY";
    private String PLAYER2_SCORE_KEY = "PLAYER2_SCORE_KEY";
    private String CURRENT_PLAYER_KEY = "CURRENT_PLAYER_KEY";
    private String IS_GAME_RUNNING = "IS_GAME_RUNNING";

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

        boolean stateHasBeenRecovered = false;

        if(savedInstanceState != null) {
            Log.d("PigGame","inside onSaveInstanceState");
            if(savedInstanceState.getBoolean(IS_GAME_RUNNING,false) != false) {
                Log.d("PigGame","inside onSaveInstanceState: reinitializing class variables");
                //getting variables from sharedPrefs
                String player1Username = savedInstanceState.getString(PLAYER1_USERNAME_KEY, "");
                String player2Username = savedInstanceState.getString(PLAYER2_USERNAME_KEY, "");
                int player1Score = savedInstanceState.getInt(PLAYER1_SCORE_KEY,-1);
                int player2Score = savedInstanceState.getInt(PLAYER2_SCORE_KEY,-1);
                boolean isGameRunning = savedInstanceState.getBoolean(IS_GAME_RUNNING,false);
                int currentPlayerTurn = savedInstanceState.getInt(CURRENT_PLAYER_KEY,-1);
                int runningPointsTotal = savedInstanceState.getInt(RUNNING_POINTS_TOTAL,-1);
                //rebuild game objects and settings
                this.pigGame = new PigGame(player1Username,player2Username,8);
                this.gameInProgress = isGameRunning;
                this.pigGame.setPlayerScore(1,player1Score);
                this.pigGame.setPlayerScore(2,player2Score);;
                this.pigGame.setCurrentPlayerTurn(currentPlayerTurn);
                this.pigGame.setPointsForCurrentTurn(runningPointsTotal);

                this.DisableUsernameEntryFields();
                this.SetUsernameFields(this.player1Name,this.player2Name);
                this.UpdatePlayerScore(1,this.pigGame.getPlayerScore(1));
                this.UpdatePlayerScore(2,this.pigGame.getPlayerScore(2));
                this.UpdatePointsRunningTotal(this.pigGame.getPointsForCurrentTurn());
                this.UpdateCurrentPlayer();
                stateHasBeenRecovered = true;
            }
        }

        //GETS REFERENCE TO SharedPrefs OBJECT
        savedValues = getSharedPreferences("savedValues", MODE_PRIVATE);

        //Methods calls
        CreateUIEventListeners();
        //disable buttons until a new game has been created
        if (stateHasBeenRecovered != true) {
            //ASSEUMPTION:
                //if the state has been recovered, a game was already running, therefore not requiring the roll and disable buttons to be disabled
            //these will only be disabled if there was no previously saved state data, because a new game will need to be created first
            this.DisableRollButton();
            this.DisableEndTurnButton();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d("PigGame","inside onSaveInstanceState");
        savedInstanceState.putString(this.PLAYER1_USERNAME_KEY,this.pigGame.getPlayerName(1));
        savedInstanceState.putString(this.PLAYER2_USERNAME_KEY,this.pigGame.getPlayerName(2));
        savedInstanceState.putInt(this.PLAYER1_SCORE_KEY,this.pigGame.getPlayerScore(1));
        savedInstanceState.putInt(this.PLAYER2_SCORE_KEY,this.pigGame.getPlayerScore(2));
        savedInstanceState.putInt(this.CURRENT_PLAYER_KEY,this.pigGame.getCurrentPlayerNumber());
        savedInstanceState.putInt(this.RUNNING_POINTS_TOTAL,this.pigGame.getPointsForCurrentTurn());
        savedInstanceState.putBoolean(this.IS_GAME_RUNNING,this.gameInProgress);
        super.onSaveInstanceState((savedInstanceState));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("PigGame", "inisde onPause method");
        //will save the necessary data for recreating the app onResume
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putString(this.PLAYER1_USERNAME_KEY,this.pigGame.getPlayerName(1));
        editor.putString(this.PLAYER2_USERNAME_KEY,this.pigGame.getPlayerName(2));
        editor.putInt(this.PLAYER1_SCORE_KEY,this.pigGame.getPlayerScore(1));
        editor.putInt(this.PLAYER2_SCORE_KEY,this.pigGame.getPlayerScore(2));
        editor.putInt(this.CURRENT_PLAYER_KEY,this.pigGame.getCurrentPlayerNumber());
        editor.putInt(this.RUNNING_POINTS_TOTAL,this.pigGame.getPointsForCurrentTurn());
        editor.putBoolean(this.IS_GAME_RUNNING,this.gameInProgress);
    }

    @Override
    protected void onResume() {
        Log.d("PigGame", "inisde onResume method");
        //checks if the game was running before the state change
        //pointless to update and recover state if it was never running in the first place
        if(!(savedValues.getBoolean(IS_GAME_RUNNING,false)==false)) {
            //getting variables from sharedPrefs
            String player1Username = savedValues.getString(PLAYER1_USERNAME_KEY, "");
            String player2Username = savedValues.getString(PLAYER2_USERNAME_KEY, "");
            int player1Score = savedValues.getInt(PLAYER1_SCORE_KEY,-1);
            int player2Score = savedValues.getInt(PLAYER2_SCORE_KEY,-1);
            boolean isGameRunning = savedValues.getBoolean(IS_GAME_RUNNING,false);
            int currentPlayerTurn = savedValues.getInt(CURRENT_PLAYER_KEY,-1);
            int runningPointsTotal = savedValues.getInt(RUNNING_POINTS_TOTAL,-1);
            //rebuild game objects and settings
            this.pigGame = new PigGame(player1Username,player2Username,8);
            this.gameInProgress = isGameRunning;
            this.pigGame.setPlayerScore(1,player1Score);
            this.pigGame.setPlayerScore(2,player2Score);
            this.pigGame.setCurrentPlayerTurn(currentPlayerTurn);
            this.pigGame.setPointsForCurrentTurn(runningPointsTotal);

            //SETTING UI ELEMENTS AND DISABLING THE NECESSARY UI
            this.DisableUsernameEntryFields();
            this.SetUsernameFields(player1Username,player2Username);
            this.UpdatePlayerScore(1,player1Score);
            this.UpdatePlayerScore(2,player2Score);
            this.UpdatePointsRunningTotal(runningPointsTotal);
            this.UpdateCurrentPlayer();
        }
        super.onResume();
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
            this.EndTurn();
            Toast.makeText(this,("Ouch, "+this.pigGame.getCurrentPlayerName()+" has rolled an 8!"),Toast.LENGTH_SHORT);
        }
        try {
            //this process is done to slow down the user and prevent spamming
            Thread.sleep(800);
        }
        catch (InterruptedException e) {
            Log.d("PigGame","sleep function for roll button was interrupted");
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
            Toast.makeText(getApplicationContext(), (this.pigGame.getPlayerName(winnerNumber) + " has won!"),Toast.LENGTH_SHORT);
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
            Toast.makeText(getApplicationContext(),"Please enter valid usernames, press new game",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(),"New game started, good luck!",Toast.LENGTH_SHORT).show();
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

    private void SetUsernameFields(String player1Name, String player2Name) {
        //this is used when the UI needs to be synced from the inside to out (such as recovering state)
        this.player1UsernameTextEntry.setText(player1Name);
        this.player2UsernameTextEntry.setText(player2Name);
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
            Toast.makeText(this,"Player 1 needs a valid username!",Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        if(player2Username.length() == 0 || player2Username.equals("Enter username")) {
            Toast.makeText(this,"Player 2 needs a valid username!",Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        if(player1Username.equals(player2Username)) {
            Toast.makeText(this,"Players cannot have the same username!",Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        return isValid;
    }



}
