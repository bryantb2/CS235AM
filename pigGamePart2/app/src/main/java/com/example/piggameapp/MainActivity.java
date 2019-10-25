package com.example.piggameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

    //these string vars are used to store the desired usernames of players that will be passed into the PigGame object
    //these will not be used after the PigGame object is created
    private String player1Name;
    private String player2Name;
    //tracks if the game is running
    private boolean gameInProgress = false;
    //conditionally locks the roll die button if there is a winner
    private boolean isWinner =false;
    private boolean areEntryFieldsDisabled = false;

    //Class-level sharedPreferences object and state variables
    private SharedPreferences savedValues;
    private boolean stateHasBeenRecovered = false;
    private boolean stateHasBeenSaved = false;

    //SAVE STATE KEYS
    private String RUNNING_POINTS_TOTAL = "RUNNING_POINTS_TOTAL";
    private String PLAYER1_USERNAME_KEY = "PLAYER1_USERNAME_KEY";
    private String PLAYER2_USERNAME_KEY = "PLAYER2_USERNAME_KEY";
    private String PLAYER1_SCORE_KEY = "PLAYER1_SCORE_KEY";
    private String PLAYER2_SCORE_KEY = "PLAYER2_SCORE_KEY";
    private String CURRENT_PLAYER_KEY = "CURRENT_PLAYER_KEY";
    private String IS_GAME_RUNNING = "IS_GAME_RUNNING";
    private String DIE_IMAGE_NUMBER = "DIE_IMAGE_NUMBER";

    private String OLD_PREF_ENABLE_AI = "OLD_PREF_ENABLE_AI";
    private String OLD_PREF_NUMBER_OF_DIE = "OLD_PREF_NUMBER_OF_DIE";
    private String ENABLE_CUSTOM_SCORE = "ENABLE_CUSTOM_SCORE";
    private String CUSTOM_SCORE = "CUSTOM_SCORE";

    //PREFERENCES/SETTINGS VARIABLES
    private SharedPreferences prefs;
    private boolean enableAI;
    private int numberOfDie;
    private boolean enableCustomScore;
    private int customScore;

    //LIFECYCLES
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //mounting the XML to the main activity
        //will convert the XML items to java objects that can be displayed in the activity
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            //determines which menu item was selected based off element IDs
            case R.id.about:
                Toast.makeText(this,"About", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.settings:
                Toast.makeText(this,"Settings", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                return true;
            default:
                //allows for default OptionsItemSelected behavior from the super class
                return super.onOptionsItemSelected((item));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //GETTING UI ELEMENTS
        this.player1UsernameTextEntry = findViewById(R.id.player1UsernameTextEntry);
        this.player1ScoreLabel = findViewById(R.id.player1Score_Label);
        this.player2ScoreLabel = findViewById(R.id.player2Score_Label);
        this.player2UsernameTextEntry = findViewById(R.id.player2UsernameTextEntry);
        this.currentPlayerLabel = findViewById(R.id.currentPlayer_Label);
        this.dieImage = findViewById(R.id.dieRollResult_Label);
        this.pointsAccumulatorLabel = findViewById(R.id.runningPointsTotal_Label);
        this.rollDieButton = findViewById(R.id.rollDie_Button);
        this.endTurnButton = findViewById(R.id.endTurn_Button);
        this.newGameButton = findViewById(R.id.newGame_Button);


        //GETS REFERENCE TO SharedPrefs OBJECT
        savedValues = getSharedPreferences("savedValues", MODE_PRIVATE);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    //TODO: wire onPause and onResume methods to work with settings preferences
    //TODO: create instance variables at the class level to get and set user's custom settings from the defaultPreferences object       ---DONE---
    //TODO: add AI rolling logic (calc, roll, display, ect.)        --- DONE ----
    //TODO: add functionality for two dice
    //TODO: modify game logic to accept a changing max game score
    //TODO: wire the entire app to dynamically change logic, using bools set at the class level, and display methods based off the user's desired game settings

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("PigGame", "inisde onPause method");
        SharedPreferences.Editor editor = savedValues.edit();
        if (this.gameInProgress == true) {
            //fetching and storing data to be put into saveInstance
            String username1 = pigGame.getPlayerName(1);
            String username2 = pigGame.getPlayerName(2);

            editor.putString(this.PLAYER1_USERNAME_KEY, username1);
            Log.d("PigGame", "inside onPause, logging player1 username key: ");
            Log.d("PigGame", username1);

            editor.putString(this.PLAYER2_USERNAME_KEY, username2);
            Log.d("PigGame", "inside onPause, logging player2 username key: ");
            Log.d("PigGame", username2);


            int player1Score = pigGame.getPlayerScore(1);
            int player2Score = pigGame.getPlayerScore(2);

            editor.putInt(this.PLAYER1_SCORE_KEY, player1Score);
            Log.d("PigGame", "inside onPause, logging player1 score key: ");
            Log.d("PigGame", String.valueOf(player1Score));

            editor.putInt(this.PLAYER2_SCORE_KEY, player2Score);
            Log.d("PigGame", "inside onPause, logging player2 score key: ");
            Log.d("PigGame", String.valueOf(player2Score));


            int currentPlayerNumber = pigGame.getCurrentPlayerNumber();
            editor.putInt(this.CURRENT_PLAYER_KEY, currentPlayerNumber);
            Log.d("PigGame", "inside onPause, logging current player key: ");
            Log.d("PigGame", String.valueOf(currentPlayerNumber));


            int runningPointsTotal = pigGame.getPointsForCurrentTurn();
            editor.putInt(this.RUNNING_POINTS_TOTAL, runningPointsTotal);
            Log.d("PigGame", "inside onPause, logging running points total key: ");
            Log.d("PigGame", String.valueOf(runningPointsTotal));


            int dieImageNumber = pigGame.getLastRolledNumber();
            editor.putInt(this.DIE_IMAGE_NUMBER, dieImageNumber);
            Log.d("PigGame", "inside onPause, logging die image number key: ");
            Log.d("PigGame", String.valueOf(dieImageNumber));

            boolean gameStatus = this.gameInProgress;
            editor.putBoolean(this.IS_GAME_RUNNING, gameStatus);
            Log.d("PigGame", "inside onPause, logging is game running key: ");
            Log.d("PigGame", String.valueOf(gameStatus));
        }

        //SAVING PREFERENCES
        editor.putBoolean(OLD_PREF_ENABLE_AI,this.enableAI);
        editor.putInt(OLD_PREF_NUMBER_OF_DIE,this.numberOfDie);
        editor.putBoolean(ENABLE_CUSTOM_SCORE,this.enableCustomScore);
        editor.putInt(CUSTOM_SCORE,this.customScore);
        editor.commit();
    }

    @Override
    protected void onResume() {
        //GETTING OLD PREFERENCE SETTINGS
        boolean oldEnableAISetting = prefs.getBoolean(OLD_PREF_ENABLE_AI,false);
        int oldNumberOfDieSetting = prefs.getInt(OLD_PREF_NUMBER_OF_DIE,1);
        boolean oldEnableCustomScoreSetting = prefs.getBoolean(ENABLE_CUSTOM_SCORE,false);
        int oldCustomScoreSetting = prefs.getInt(CUSTOM_SCORE,100);

        //GETTING NEW PREFERENCE SETTINGS
        this.enableAI = prefs.getBoolean("pref_enable_AI",false);
        this.numberOfDie = Integer.parseInt(prefs.getString("pref_number_of_die","1"));
        this.enableCustomScore = prefs.getBoolean("pref_enable_custom_score",false);
        String customMaxScoreString = prefs.getString("pref_max_play_score","100");
        if(IsCustomScoreValid(customMaxScoreString) == true) {
            //checking customScore for a misbehaved user
            //executes if the string version of the user scoreInput is valid
            this.customScore = Integer.parseInt(customMaxScoreString);
        }
        else {
            //executes if the CustomScore is not valid
            this.customScore = 100;
        }



        Log.d("onResume","inside onResume method, logging enable Ai preference value: " + enableAI );
        Log.d("onResume","inside onResume method, logging enable die # preference value: " + numberOfDie );

        Log.d("PigGame", "inisde onResume method");
        //checks if the game was running before the state change
        //pointless to update and recover state if it was never running in the first place
        //IMPORTANT: onResume only takes effect if saveState is null (otherwise there will be conflicts!!!
            //testing to see if the class-level variable stateHasbeenRecovered is true, meaning that oncreate already restored the state
        boolean isGameRunning = savedValues.getBoolean(IS_GAME_RUNNING,false);
        if(isGameRunning == true) {
            Log.d("PigGame","inside onCreate: reinitializing class variables");
            //getting variables from sharedPrefs
            String player1Username = savedValues.getString(PLAYER1_USERNAME_KEY, "");
            String player2Username = savedValues.getString(PLAYER2_USERNAME_KEY, "");
            int player1Score = savedValues.getInt(PLAYER1_SCORE_KEY,0);
            int player2Score = savedValues.getInt(PLAYER2_SCORE_KEY,0);
            //boolean isGameRunning = savedInstanceState.getBoolean(IS_GAME_RUNNING,false);
            int currentPlayerTurn = savedValues.getInt(CURRENT_PLAYER_KEY,0);
            int runningPointsTotal = savedValues.getInt(RUNNING_POINTS_TOTAL,0);
            int lastRolledNumber = savedValues.getInt(DIE_IMAGE_NUMBER,8);
            Log.d("PigGame","player1Username: " + player1Username);
            Log.d("PigGame","player2Username: " + player2Username);
            Log.d("PigGame","player1Score: " + String.valueOf(player1Score));
            Log.d("PigGame","player2Score: " + String.valueOf(player2Score));
            Log.d("PigGame","isGameRunning: " + String.valueOf(isGameRunning));
            Log.d("PigGame","currentPlayerTurn: " + String.valueOf(currentPlayerTurn));
            Log.d("PigGame","runningPointsTotal " + String.valueOf(runningPointsTotal));
            Log.d("PigGame","lastRolledNumber " + String.valueOf(lastRolledNumber));

            final int tempDieNumber = 1; /* todo: CHANGE THIS LATER */
            final int tempMaxScore = 100; /* todo: CHANGE THIS LATER */


            //rebuild game objects and settings
            pigGame = new PigGame(player1Username,player2Username,8,tempDieNumber,tempMaxScore);
            this.gameInProgress = isGameRunning;
            pigGame.setPlayerScore(1,player1Score);
            pigGame.setPlayerScore(2,player2Score);;
            pigGame.setCurrentPlayerTurn(currentPlayerTurn);
            pigGame.setPointsForCurrentTurn(runningPointsTotal);
            pigGame.setLastRolledNumber(lastRolledNumber);

            //UI preparation and restoration
            this.DisableUsernameEntryFields();
            this.SetUsernameFields(player1Username,player2Username);
            this.UpdatePlayerScore(1,pigGame.getPlayerScore(1));
            this.UpdatePlayerScore(2,pigGame.getPlayerScore(2));
            this.UpdatePointsRunningTotal(pigGame.getPointsForCurrentTurn());
            this.UpdateCurrentPlayer();
            this.UpdateDieImage(lastRolledNumber);
            stateHasBeenRecovered = true;
        }

        //executes if there is no save state data to be utilized
        if(stateHasBeenRecovered == false) {
            Log.d("PigGame","Inside default state setter");
            //setting UI to default state
            this.isWinner = false;
            this.ResetUsernameFields();
            this.ResetScoreLabels();
            this.ResetRunningTotalLabel();
            this.ResetCurrrentPlayerLabel();
            this.gameInProgress = false;
            this.EnableUsernameEntryFields();
        }

        //Methods calls
        CreateUIEventListeners();
        //disable buttons until a new game has been created (or if the state has been recovered for a game that was not in progress)
        if (stateHasBeenRecovered != true) {
            //ASSUMPTION:
            //if the state has been recovered, a game was already running, therefore not requiring the roll and disable buttons to be disabled
            //these will only be disabled if there was no previously saved state data, because a new game will need to be created first
            this.DisableRollButton();
            this.DisableEndTurnButton();
        }
        super.onResume();
    }

    //Event Listener Assigner
    private void CreateUIEventListeners() {
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

        this.player1UsernameTextEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(areEntryFieldsDisabled == false && player1UsernameTextEntry.getText().toString().equals("Enter username")) {
                    player1UsernameTextEntry.setText("");
               }
            }
        });
        this.player2UsernameTextEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(areEntryFieldsDisabled == false && player2UsernameTextEntry.getText().toString().equals("Enter username")) {
                    player2UsernameTextEntry.setText("");
                }
            }
        });
    }

    //COMPUTER AI
    public void AITurn() {
        //WORKING ASSUMPTION: EXISTING PLAYER 2 LOGIC WILL BE RIGGED TO HOLD COMPUTER ROLL DATA
        //looping -->
        //roll pigGame die
        //set the die image
            //if rolled number is not 8
                //update running total label
                //if running total equals 20
                    //stop rolling and end turn
                //else roll a total of 3 times or until an 8 is rolled
            //else
                //set canRoll to false
                //reset running total label AND pigGame internal runningTotal
        //end looping <--

        final int maxComputerTurnPoints = 20;
        boolean canRoll = true;
        int numberOfRolls = 0;

        while(canRoll == true && (pigGame.getPointsForCurrentTurn() < maxComputerTurnPoints)) {
            if(numberOfRolls < 4) {
                int rollResult = pigGame.RollAndCalc();
                pigGame.setLastRolledNumber(rollResult);
                this.UpdateDieImage(rollResult);
                if(rollResult != 8) {
                    this.UpdatePointsRunningTotal(pigGame.getPointsForCurrentTurn());
                }
                else {
                    Toast.makeText(this,("Ouch, "+pigGame.getCurrentPlayerName()+" has rolled an 8!"),Toast.LENGTH_SHORT);
                    this.UpdatePlayerScore(pigGame.getCurrentPlayerNumber(),0);
                    this.ResetRunningTotalLabel();
                    this.EndTurn();
                    canRoll = false;
                }
                //sets delay to allow user to see the UI information regarding the computer's roll
                try {
                    //this process is done to slow down the user and prevent spamming
                    Thread.sleep(800);
                }
                catch (InterruptedException e) {
                    Log.d("PigGame","sleep function for roll button was interrupted");
                }
                if(this.isWinner == false) {
                    this.EnableRollButton();
                }
            }
            else {
                canRoll = false;
            }
            numberOfRolls++;
        }
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
        int rollResult = pigGame.RollAndCalc();
        pigGame.setLastRolledNumber(rollResult);
        this.UpdateDieImage(rollResult);
        if(rollResult != 8) {
            this.UpdatePointsRunningTotal(pigGame.getPointsForCurrentTurn());
        }
        else {
            Toast.makeText(this,("Ouch, "+pigGame.getCurrentPlayerName()+" has rolled an 8!"),Toast.LENGTH_SHORT);
            this.UpdatePlayerScore(pigGame.getCurrentPlayerNumber(),0);
            this.ResetRunningTotalLabel();
            this.EndTurn();
        }
        try {
            //this process is done to slow down the user and prevent spamming
            Thread.sleep(800);
        }
        catch (InterruptedException e) {
            Log.d("PigGame","sleep function for roll button was interrupted");
        }
        if(this.isWinner == false) {
            this.EnableRollButton();
        }
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
        int playerThatJustWent = pigGame.getCurrentPlayerNumber();
        int winnerNumber = pigGame.EndTurn();
        this.UpdatePlayerScore(playerThatJustWent,pigGame.getPlayerScore(playerThatJustWent));
        this.ResetRunningTotalLabel();
        if(winnerNumber != 0) {
            //display winner on UI
            this.DisplayWinner(winnerNumber);
            //update turn label to reflect end of game
            this.UpdateCurrentPlayer("End of Game!");
            //disable game buttons
            this.DisableEndTurnButton();
            this.DisableRollButton();
            //display toast message
            Toast.makeText(getApplicationContext(), (pigGame.getPlayerName(winnerNumber) + " has won!"),Toast.LENGTH_SHORT);
            //turn isWinner to true, preventing the roll die button from activating
            this.isWinner = true;
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
        this.DisableRollButton();
        this.DisableEndTurnButton();
        //execute this block if the app was already running
        //delete existing usernames in fields
        //set game bool to not active
        //reset score labels to zero
        //reset runningtotal label to zero
        //reset current player label
        if(gameInProgress==true) {
            this.isWinner = false;
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
            if(pigGame != null) {
                //checking if the object was instantiated
                pigGame.RestartGame(player1Name,player2Name);
            }
            else {
                final int tempNumberOfDie = 1;/* TODO: CHANGE THIS LATER!!!! */
                final int tempMaxScore = 100; /* todo: CHANGE THIS LATER */
                pigGame = new PigGame(player1Name,player2Name,8,tempNumberOfDie,tempMaxScore);
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

    //METHODS
    private boolean IsCustomScoreValid(String customScore) {
        String[] acceptableValues = new String[] {"0","1","2","3","4","5","6","7","8","9"};
        for(int i = 0; i <customScore.length(); i++) {
            for(int j = 0; j <acceptableValues.length;i++) {
                if(i != j) {
                    return false; //returns false if one of the score char does NOT equal the list of acceptable values
                }
            }
        }
        return true; //returns true under the assumption no illegal values were found
    }

    private void DisplayWinner(int winnerNumber) {
        //set the display points label to the name of the winner
        this.pointsAccumulatorLabel.setText(pigGame.getPlayerName(winnerNumber) + " has won!");
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
        this.areEntryFieldsDisabled = false;
        this.player1UsernameTextEntry.setEnabled(true);
        this.player2UsernameTextEntry.setEnabled(true);
    }

    private void DisableUsernameEntryFields() {
        this.areEntryFieldsDisabled = true;
        this.player1UsernameTextEntry.setEnabled(false);
        this.player2UsernameTextEntry.setEnabled(false);
    }

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
                referenceID = 0;
                break;
            }
        }
        Drawable drawableImage = getResources().getDrawable(referenceID);
        this.dieImage.setImageDrawable(drawableImage);
    }

    private void UpdatePlayerScore(int playerNumber, int score) {
        if(playerNumber==1) {
            this.player1ScoreLabel.setText(String.valueOf(score + " total points"));
        }
        else {
            this.player2ScoreLabel.setText(String.valueOf(score + " total points"));
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
        //this is the default updateCurrentPlayer method and will automatically set the UI to reflect the currentPlayerName
        this.currentPlayerLabel.setText(pigGame.getCurrentPlayerName()+"'s turn");
    }

    private void UpdateCurrentPlayer(String optionalMessage) {
        //this is for optional and specific messages that need to be displayed in the UpdateCurrentPlayer UI label
        this.currentPlayerLabel.setText(optionalMessage);
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
