package com.example.piggameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
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

    //TODO: convert mainActivity to hold fragments of the game or the main screen
    //TODO: build fragments for GAME_SCREEN and TITLE_SCREEN
    //TODO: add up button to GAME_SCREEN FRAGMENT

    //TODO: logic modifications to GAME_SCREEN (aka mainAcitivty) class/layout:
    //TODO:         --LOCK ROLL AND END TURN BUTTONS
    //TODO:         --move newgame button to GAME_SCREEN
    //TODO:         --auto lock usernames when the GAME_SCREEN starts
    //TODO:         --get username data from TITLE_SCREEN and auto set the current player's turn
    //TODO:         --UNLOCK ROLL AND END TURN BUTTONS
    //TODO:         --display toast message if there is a winner saying "return the main screen to start a newGame"


    //TODO: logic ADDITIONS to TITLE_SCREEN (aka mainAcitivty) class/layout:
    //TODO:         --DISPLAY PREFERENCE SETTINGS IN THIS FRAGMENT INSTEAD OF GAME_SCREEN
    //TODO:         --move new game button into fragment
    //TODO:         --move on edit change listener and areUserValid methods into this fragment
    //TODO:         --add code that passes username data to GAME_SCREEN AND forces an onResume of the GAME_SCREEN fragment

}