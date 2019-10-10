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
    private int reachedPointsLimitFirst;

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

    public String getCurrentPlayerName() {
        return getPlayerName(currentPlayerTurn);
    }

    public int getPlayerScore(int playerNumber) {
        if(playerNumber==1) {
            return player1.getPoints();
        }
        else {
            return player2.getPoints();
        }
    }

    public int getPointsForCurrentTurn() {
        return runningTotal;
    }

    /*public int getCurrentPlayerNumber() {
        return currentPlayerTurn;
    }*/

    //METHODS
    public int RollAndCalc() {
        //returns number of the roll
        //roll die
        //if roll doesn't equal 8
            //add to running total
            //return roll number
        //else
            //reset running total
            //reset player score
            //return roll number
    }

    public void EndTurn() {
        //add runningTotal to player score
        //reset runningTotal
        //set next player turn to opposite of current player turn

    }

    public void RestartGame() {
        //reset player scores
        //set player turn back to default
        //reset points limit first tracker
        resetPlayerScore(1);
        resetPlayerScore(2);
        setPlayerTurn(1);
        this.reachedPointsLimitFirst = 0;
    }

    public String CalcWinner()
    {
        //if currentPlayer has at least 100 points
            //if currentPlayer is player1
                //
            //if currentPlayer is player2
    }

    public int rollDie() {
        return this.eightSidedDie.roll();
    }

    private void resetPlayerScore(int playerNumber) {
        if(playerNumber==1) {
            this.player1.wipePoints();
        }
        else {
            this.player2.wipePoints();
        }
    }

    private void resetRunningTotal() {
        this.runningTotal = 0;
    }

    private void setPlayerTurn(int playerNumber) {
        this.currentPlayerTurn = playerNumber;
    }

    private void addToPlayerScore(int playerNumber, int points) {
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

THESE ARE THE PUBLIC METHODS
roll()
rollAndCalc()
restartGame()
endTurn()


 */