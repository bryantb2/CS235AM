package com.example.piggameapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.app.Fragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import static android.content.Context.MODE_PRIVATE;

public class GameScreenFragment extends Fragment {
    //CLASS UI FIELDS
    private PigGame pigGame;
    private EditText player1UsernameTextEntry;
    private EditText player2UsernameTextEntry;
    private TextView player1ScoreLabel;
    private TextView player2ScoreLabel;
    private ImageView dieImage1;
    private ImageView dieImage2;
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

    //SAVE STATE KEYS
    private String RUNNING_POINTS_TOTAL = "RUNNING_POINTS_TOTAL";
    private String PLAYER1_USERNAME_KEY = "PLAYER1_USERNAME_KEY";
    private String PLAYER2_USERNAME_KEY = "PLAYER2_USERNAME_KEY";
    private String PLAYER1_SCORE_KEY = "PLAYER1_SCORE_KEY";
    private String PLAYER2_SCORE_KEY = "PLAYER2_SCORE_KEY";
    private String CURRENT_PLAYER_KEY = "CURRENT_PLAYER_KEY";
    private String IS_GAME_RUNNING = "IS_GAME_RUNNING";
    private String DIE_IMAGE_NUMBER = "DIE_IMAGE_NUMBER";
    private String DIE_IMAGE_NUMBER_TWO = "DIE_IMAGE_NUMBER_TWO";

    /*private String OLD_PREF_ENABLE_AI = "OLD_PREF_ENABLE_AI";
    private String OLD_PREF_NUMBER_OF_DIE = "OLD_PREF_NUMBER_OF_DIE";
    private String ENABLE_CUSTOM_SCORE = "ENABLE_CUSTOM_SCORE";
    private String CUSTOM_SCORE = "CUSTOM_SCORE"; */

    //PREFERENCES/SETTINGS VARIABLES
    private SharedPreferences prefs;
    private boolean enableAI;
    private int numberOfDie;
    private boolean enableCustomScore;
    private int customScore;

    //LIFECYCLES
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //inflate layout
        View view = inflater.inflate(R.layout.game_screen_fragment, container, false);

        //turns on menu rendering
        setHasOptionsMenu(true);

        //GETTING UI ELEMENTS
        this.player1UsernameTextEntry = view.findViewById(R.id.player1UsernameTextEntry);
        this.player1ScoreLabel = view.findViewById(R.id.player1Score_Label);
        this.player2ScoreLabel = view.findViewById(R.id.player2Score_Label);
        this.player2UsernameTextEntry = view.findViewById(R.id.player2UsernameTextEntry);
        this.currentPlayerLabel = view.findViewById(R.id.currentPlayer_Label);
        this.dieImage1 = view.findViewById(R.id.dieRollResult_Label);
        this.dieImage2 = view.findViewById(R.id.dieRollResult_Label2);
        this.pointsAccumulatorLabel = view.findViewById(R.id.runningPointsTotal_Label);
        this.rollDieButton = view.findViewById(R.id.rollDie_Button);
        this.endTurnButton = view.findViewById(R.id.endTurn_Button);
        this.newGameButton = view.findViewById(R.id.newGame_Button);


        return view;
    }

    // TODO: revise all lifecycle and call back methods, removing unnecessary code such as settings checks
    // TODO: build a createGame method that is called in onResume that builds the game's backend AND UI
    // TODO: remove all default else blocks that build the UI or backend on the assumption that the game might not be running
        // we want to remove these code blocks because the only time that this game screen will
        // KEEP UI resetting methods, though
    // TODO: get the damn action bar to show the back button
    // TODO: migrate getting UI elements to onCreateView
    // TODO: get the intent extraState variables in onResume for implementation in the game object !!!!!!!!!!!!!
    // TODO: if there is a winner, set the gameInProgress boolean to false


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getActivity().setContentView(R.layout.game_screen_fragment);



        //GETS REFERENCE TO SharedPrefs OBJECT
        savedValues = this.getActivity().getSharedPreferences("savedValues", MODE_PRIVATE);
        prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("PigGame", "inisde onPause method");
        SharedPreferences.Editor editor = savedValues.edit();
        //fetching and storing data to be put into saveInstance
        if(pigGame != null) {
            //editor.putString(this.PLAYER1_USERNAME_KEY, pigGame.getPlayerName(1));
            //editor.putString(this.PLAYER2_USERNAME_KEY, pigGame.getPlayerName(2));
            editor.putInt(this.PLAYER1_SCORE_KEY, pigGame.getPlayerScore(1));
            editor.putInt(this.PLAYER2_SCORE_KEY, pigGame.getPlayerScore(2));
            editor.putInt(this.CURRENT_PLAYER_KEY,  pigGame.getCurrentPlayerNumber());
            editor.putInt(this.RUNNING_POINTS_TOTAL, pigGame.getPointsForCurrentTurn());
            editor.putInt(this.DIE_IMAGE_NUMBER, pigGame.getLastRolledNumber());
            if(this.numberOfDie == 2) {
                editor.putInt(this.DIE_IMAGE_NUMBER_TWO, pigGame.getLastRolledNumber2());
            }
            editor.commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //checks if the game was running before the state change
        //pointless to update and recover state if it was never running in the first place
        //IMPORTANT: onResume only takes effect if saveState is null (otherwise there will be conflicts!!!
        //testing to see if the class-level variable stateHasbeenRecovered is true, meaning that oncreate already restored the state
        boolean isGameRunning = savedValues.getBoolean(IS_GAME_RUNNING,false);
        if(isGameRunning == true) {

            //getting variables from sharedPrefs
            String player1Username = savedValues.getString(PLAYER1_USERNAME_KEY, "");
            String player2Username = savedValues.getString(PLAYER2_USERNAME_KEY, "");
            int player1Score = savedValues.getInt(PLAYER1_SCORE_KEY,0);
            int player2Score = savedValues.getInt(PLAYER2_SCORE_KEY,0);
            //boolean isGameRunning = savedInstanceState.getBoolean(IS_GAME_RUNNING,false);
            int currentPlayerTurn = savedValues.getInt(CURRENT_PLAYER_KEY,0);
            int runningPointsTotal = savedValues.getInt(RUNNING_POINTS_TOTAL,0);
            int lastRolledNumber = savedValues.getInt(DIE_IMAGE_NUMBER,8);
            int lastRolledNumber2 = 8;
            if(this.numberOfDie==2) {
                lastRolledNumber2 = savedValues.getInt(DIE_IMAGE_NUMBER_TWO,8);
            }

            //rebuild game objects and settings
            pigGame = new PigGame(player1Username,player2Username,8,this.numberOfDie,(this.enableCustomScore == true? this.customScore : 100));
            this.gameInProgress = isGameRunning;
            pigGame.setPlayerScore(1,player1Score);
            pigGame.setPlayerScore(2,player2Score);;
            pigGame.setCurrentPlayerTurn(currentPlayerTurn);
            pigGame.setPointsForCurrentTurn(runningPointsTotal);
            pigGame.setLastRolledNumber(lastRolledNumber);
            if(this.numberOfDie == 2) {
                pigGame.setLastRolledNumber2(lastRolledNumber2);
            }

            //UI preparation and restoration
            this.DisableUsernameEntryFields();
            this.SetUsernameFields(player1Username,player2Username);
            this.UpdatePlayerScore(1,pigGame.getPlayerScore(1));
            this.UpdatePlayerScore(2,pigGame.getPlayerScore(2));
            this.UpdatePointsRunningTotal(pigGame.getPointsForCurrentTurn());
            this.UpdateCurrentPlayer();
            this.UpdateDieImage(lastRolledNumber,1);
            if(this.numberOfDie==2) {
                this.dieImage2.setVisibility(View.VISIBLE);
                this.UpdateDieImage(lastRolledNumber2,2);
            }
            else {
                this.dieImage2.setVisibility(View.GONE);
            }
        }
        else {
            //executes if there is no save state data to be utilized OR a game restart is required
            //ASSUMPTION:
            //if the state has been recovered, a game was already running, therefore not requiring the roll and disable buttons to be disabled
            //these will only be disabled if there was no previously saved state data, because a new game will need to be created first
            Log.d("PigGame","Inside default state setter");
            //setting UI to default state
            this.isWinner = false;
            this.ResetUsernameFields();
            this.ResetScoreLabels();
            this.ResetRunningTotalLabel();
            this.ResetCurrrentPlayerLabel();
            this.gameInProgress = false;
            if(this.enableAI == true) {
                //ensures that the AI is displayed and the user cannot change it
                //TODO: FIX THIS (GET NAME FROM STATE VARIABLE)
                this.SetUsernameFields("BOB","Computer");
            }
            if(this.numberOfDie==2) {
                this.dieImage2.setVisibility(View.VISIBLE);
                this.UpdateDieImage(8,2);
            }
            else {
                this.dieImage2.setVisibility(View.GONE);
            }
            //disable buttons until a new game has been created (or if the state has been recovered for a game that was not in progress)
            this.DisableRollButton();
            this.DisableEndTurnButton();
        }
        //Methods calls
        CreateUIEventListeners();
    }

    //Event Listener Assigner
    private void CreateUIEventListeners() {
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

    //COMPUTER AI
    public void AITurn(int numberOfPlayDie) {
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
        int maxNumberOfRolls = 4;
        int maxComputerTurnPoints;
        if(this.numberOfDie == 2) {
            maxComputerTurnPoints = 30;
        }
        else {
            maxComputerTurnPoints = 20;
        }
        boolean rolledAnEight = false;
        int numberOfRolls = 0;

        while(rolledAnEight == false &&
                numberOfRolls < maxNumberOfRolls &&
                pigGame.getPointsForCurrentTurn() < maxComputerTurnPoints) {
            if(this.numberOfDie == 2) {

                int rollResult = pigGame.RollAndCalc();
                int rollResult2 = pigGame.RollAndCalc();
                pigGame.setLastRolledNumber(rollResult);
                pigGame.setLastRolledNumber2(rollResult2);
                UpdateDieImage(rollResult,1);
                UpdateDieImage(rollResult2,2);
                UpdatePointsRunningTotal(pigGame.getPointsForCurrentTurn());

                if(rollResult == 8 || rollResult2 == 8) {
                    rolledAnEight = true;
                }
            }
            else {
                int rollResult = pigGame.RollAndCalc();
                pigGame.setLastRolledNumber(rollResult);
                UpdateDieImage(rollResult,1);
                UpdatePointsRunningTotal(pigGame.getPointsForCurrentTurn());

                if(rollResult == 8) {
                    rolledAnEight = true;
                }
            }
            numberOfRolls++;
        }
        if(rolledAnEight==true) {
            Toast.makeText(getActivity(),("Ouch, "+pigGame.getCurrentPlayerName()+" has rolled an 8!"),Toast.LENGTH_SHORT);
        }
        this.UpdatePlayerScore(pigGame.getCurrentPlayerNumber(),0);
        this.EndTurn();
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
        final int DIE_ONE_UI_POSITION = 1;
        final int DIE_TWO_UI_POSITION = 2;

        if(this.numberOfDie == 2) {
            int rollResult1 = pigGame.RollAndCalc();
            int rollResult2 = pigGame.RollAndCalc();

            pigGame.setLastRolledNumber(rollResult1);
            pigGame.setLastRolledNumber2(rollResult2);
            this.UpdateDieImage(rollResult1,DIE_ONE_UI_POSITION);
            this.UpdateDieImage(rollResult2,DIE_TWO_UI_POSITION);

            if(rollResult1 != 8 && rollResult2 != 8) {
                this.UpdatePointsRunningTotal(pigGame.getPointsForCurrentTurn());
            }
            else {
                Toast.makeText(getActivity(),("Ouch, "+pigGame.getCurrentPlayerName()+" has rolled an 8!"),Toast.LENGTH_SHORT);
                this.UpdatePlayerScore(pigGame.getCurrentPlayerNumber(),0);
                this.ResetRunningTotalLabel();
                this.EndTurn();
            }
        }
        else {
            int rollResult = pigGame.RollAndCalc();
            pigGame.setLastRolledNumber(rollResult);
            this.UpdateDieImage(rollResult,DIE_ONE_UI_POSITION);
            if(rollResult != 8) {
                this.UpdatePointsRunningTotal(pigGame.getPointsForCurrentTurn());
            }
            else {
                Toast.makeText(getActivity(),("Ouch, "+pigGame.getCurrentPlayerName()+" has rolled an 8!"),Toast.LENGTH_SHORT);
                this.UpdatePlayerScore(pigGame.getCurrentPlayerNumber(),0);
                this.ResetRunningTotalLabel();
                this.EndTurn();
            }
        }
        try {
            //this process is done to slow down the user and prevent spamming
            Thread.sleep(200);
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
            Toast.makeText(getActivity(), (pigGame.getPlayerName(winnerNumber) + " has won!"),Toast.LENGTH_SHORT);
            //turn isWinner to true, preventing the roll die button from activating
            this.isWinner = true;
        }
        else {
            if(this.enableAI == true && playerThatJustWent == 1) {
                //show that computer is taking its turn
                //execute AI logic method
                UpdateCurrentPlayer();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AITurn(numberOfDie);
                    }
                }, 500);
            }
            //THIS CODE IS MEANT TO PRIME THE UI FOR A HUMAN USER
            //end turn auto switches the player turn if there is no winner, so all we have to do here reflect the change in the UI
            //re-enable the endturn button
            //re-enable the roll button
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    UpdateCurrentPlayer();
                    EnableEndTurnButton();
                    EnableRollButton();
                }
            }, 500);
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
            //this.EnableUsernameEntryFields();
            if(this.enableAI == true) {
                //ensures that the AI is displayed and the user cannot change it
                //this.SetAndDisableAIUsernameField("Computer");
            }
            this.ResetScoreLabels();
            this.ResetRunningTotalLabel();
            this.ResetCurrrentPlayerLabel();
            this.gameInProgress = false;
            Toast.makeText(getActivity(),"Please enter valid usernames, press new game",Toast.LENGTH_SHORT).show();
        }
        //execute this block if the app was not running
        //get the text from both edit text fields
        //if data in fields is valid, reset PigGame object
        //pass in usernames and die size
        //display current player's turn
        //unlock the roll and endturn buttons
        //if(AreUsernamesValid() == true) {
            this.DisableUsernameEntryFields();
            this.player1Name = this.player1UsernameTextEntry.getText().toString();
            this.player2Name = this.player2UsernameTextEntry.getText().toString();
            if(pigGame != null) {
                //checking if the object was instantiated
                pigGame.RestartGame(player1Name,player2Name);
            }
            else {
                pigGame = new PigGame(player1Name,player2Name,8,this.numberOfDie,(this.enableCustomScore == true? this.customScore : 100));
            }
            this.gameInProgress = true;
            this.UpdateCurrentPlayer();
            this.EnableRollButton();
            this.EnableEndTurnButton();
            Toast.makeText(getActivity(),"New game started, good luck!",Toast.LENGTH_SHORT).show();
            Log.d("pigGameUILayer","inside newGameButtonClick method, usernames valid");
        //}
        Log.d("pigGameUILayer","inside newGameButtonClick method, usernames NOT valid");
    }

    //METHODS
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

    private void DisableUsernameEntryFields() {
        this.areEntryFieldsDisabled = true;
        this.player1UsernameTextEntry.setEnabled(false);
        this.player2UsernameTextEntry.setEnabled(false);
    }

    private void UpdateDieImage(int rolledNumber,int diePositionInUI) {
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
                referenceID = R.drawable.die8side8;
                break;
            }
        }
        Drawable drawableImage = getResources().getDrawable(referenceID);
        if(diePositionInUI == 1) {
            this.dieImage1.setImageDrawable(drawableImage);
        }
        else {
            this.dieImage2.setImageDrawable(drawableImage);
        }

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
}
