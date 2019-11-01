package com.example.piggameapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class TitleScreenFragment extends Fragment {
    //CLASS FIELDS
    //views
    private EditText player1UsernameTextEntry;
    private EditText player2UsernameTextEntry;
    private Button newGameButton;


    //tracks if the game is running
    private boolean gameInProgress = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //inflate layout
        View view = inflater.inflate(R.layout.title_screen_fragment, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //mounting the XML to the main activity
        //will convert the XML items to java objects that can be displayed in the activity
        inflater.inflate(R.menu.settings_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            //determines which menu item was selected based off element IDs
            case R.id.about:
                Toast.makeText(getActivity(),"About", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.settings:
                Toast.makeText(getActivity(),"Settings", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(),SettingsActivity.class));
                return true;
            default:
                //allows for default OptionsItemSelected behavior from the super class
                return super.onOptionsItemSelected((item));
        }
    }

    private void CreateUIEventListeners() {
        this.newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewGame();
            }
        });

        this.player1UsernameTextEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gameInProgress == false && player1UsernameTextEntry.getText().toString().equals("Enter username")) {
                    player1UsernameTextEntry.setText("");
                }
            }
        });
        this.player2UsernameTextEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gameInProgress == false && player2UsernameTextEntry.getText().toString().equals("Enter username")) {
                    player2UsernameTextEntry.setText("");
                }
            }
        });
    }


    //EVENT CALLBACK METHODS
    public void NewGame() {startActivity(new Intent(getActivity(),GameScreenActivity.class)); }
        //this.DisableRollButton();
        //this.DisableEndTurnButton();

        //execute this block if the app was already running
        //delete existing usernames in fields
        //set game bool to not active
        //reset score labels to zero
        //reset runningtotal label to zero
        //reset current player label
        /*if(gameInProgress==true) {
            this.isWinner = false;
            this.ResetUsernameFields(); //this is what prevents the second block in this handler from firing
            this.EnableUsernameEntryFields();
            if(this.enableAI == true) {
                //ensures that the AI is displayed and the user cannot change it
                this.SetAndDisableAIUsernameField("Computer");
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
        if(AreUsernamesValid() == true) {
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
            //UpdateCurrentPlayer();
            //this.EnableRollButton();
            //this.EnableEndTurnButton();
            Toast.makeText(getActivity(),"New game started, good luck!",Toast.LENGTH_SHORT).show();
            Log.d("pigGameUILayer","inside newGameButtonClick method, usernames valid");
        }
        Log.d("pigGameUILayer","inside newGameButtonClick method, usernames NOT valid");
    }*/

    //UI-RELATED METHODS

    private void ResetUsernameFields() {
        final String defaultPlaceHolderText = "Enter username";
        this.player1UsernameTextEntry.setText(defaultPlaceHolderText);
        this.player2UsernameTextEntry.setText(defaultPlaceHolderText);
    }

    private boolean AreUsernamesValid() {
        String player1Username = this.player1UsernameTextEntry.getText().toString();
        String player2Username = this.player2UsernameTextEntry.getText().toString();
        boolean isValid = true;
        //validation passes if both players have original names that are not the same
        if(player1Username.length() == 0 || player1Username.equals("Enter username")) {
            Toast.makeText(getActivity(),"Player 1 needs a valid username!",Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        if(player2Username.length() == 0 || player2Username.equals("Enter username")) {
            Toast.makeText(getActivity(),"Player 2 needs a valid username!",Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        if(player1Username.equals(player2Username)) {
            Toast.makeText(getActivity(),"Players cannot have the same username!",Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        return isValid;
    }
}
