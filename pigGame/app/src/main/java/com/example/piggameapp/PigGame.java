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
        if(playerNumber==1) {
            return player1.getName();
        }
        else {
            return player2.getName();
        }
    }

    public int getPlayerScore(int playerNumber) {
        if(playerNumber==1) {
            return player1.getPoints();
        }
        else {
            return player2.getPoints();
        }
    }

    public int getCurrentPlayerNumber() {
        return currentPlayerTurn;
    }

    public String getCurrentPlayerName() {
        return getPlayerName(currentPlayerTurn);
    }

    public int getPointsForCurrentTurn() {
        return runningTotal;
    }


    //METHODS
    public void resetPlayerScore(int playerNumber) {
        if(playerNumber==1) {
            this.player1.wipePoints();
        }
        else {
            this.player2.wipePoints();
        }
    }

    public void setPlayerTurn(int playerNumber) {
        this.currentPlayerTurn = playerNumber;
    }

    public void addToPlayerScore(int playerNumber, int points) {
        if(playerNumber==1) {
            this.player1.addPoints(points);
        }
        else {
            this.player2.addPoints(points);
        }
    }
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