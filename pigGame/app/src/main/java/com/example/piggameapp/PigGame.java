package com.example.piggameapp;

import com.example.piggameapp.Die;
import com.example.piggameapp.Player;

public class PigGame {
    //CLASS FIELDS
    private Player player1;
    private Player player2;
    private Die eightSidedDie;
    private int currentPlayerTurn;
    private int runningTotal;

    //CONSTRUCTOR
    public PigGame(String playerOneName, String playerTwoName, int dieSize) {
        this.player1 = new Player(playerOneName);
        this.player2 = new Player(playerTwoName);
        this.eightSidedDie = new Die(dieSize);
        this.currentPlayerTurn = 1;
    }

    //PROPERTIES
    public String getPlayerName(int playerNumber) {

    }

    public int getPlayerScore(int playerNumber) {

    }


    //METHODS
}

/*
Pig has these properties:
playerList
currentPlayerTurn
runningTotal (for when player is rolling the dice)


Pig has these methods:
getPlayerName(1 or 2)
getPlayerScore(1 or 2)
getCurrentPlayerTurn()
getPointsForCurrentTurn()

resetPlayerScore(1 or 2)
setPlayerTurn(1 or 2)


addPointsToPlayer(1 or 2)
addPointsToRunningTotal()


 */