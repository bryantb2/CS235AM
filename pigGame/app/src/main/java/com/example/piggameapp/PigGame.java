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
    private boolean playerOneReached100;

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
        //roll die
        //if roll doesn't equal 8
            //add to running total
            //return roll number
        //else
            //reset running total
            //reset player score
            //return roll number
        //returns number of the roll
        int rolledNumber = this.rollDie();
        if(rolledNumber != 8) {
            addToRunningTotal(rolledNumber);
            return rolledNumber;
        }
        else {
            this.resetRunningTotal();
            resetPlayerScore(currentPlayerTurn);
            return rolledNumber;
        }
    }

    public void EndTurn() {
        //add runningTotal to player score
        //reset runningTotal
        //CALCULATE WINNER WOULD GET CALLED WITH THIS
        //set next player turn to opposite of current player turn
        this.addToPlayerScore(currentPlayerTurn,runningTotal);
        this.resetRunningTotal();
        this.setNextPlayerTurn();
    }

    public void RestartGame() {
        //reset player scores
        //reset internal running score
        //set player turn back to default
        //reset points limit first tracker
        this.resetPlayerScore(1);
        this.resetPlayerScore(2);
        this.resetPlayerTurn();
        this.resetRunningTotal();
        this.playerOneReached100 = false;
    }

    public String CalcWinner() {
        //if player 2 has 100
            //auto win if reached 100 is false
            //also wins if reached 100 is on and (player2 > player1)
        //if player 1 has 100
            //turn on reached 100
                //return empty string
            //if reached 100 is on and (player1>player2)
                //return string player1
        final int player1Points = this.player1.getPoints();
        final int player2Point = this.player2.getPoints();

        if (player2Point >= 100) {
            return "player";
        }
        else if(player1Points >= 100) {
            if(this.playerOneReached100 == true && player1Points > player2Point) {
                return "player1";
            }
            this.playerOneReached100 = true;
        }
        return "";
    }

    public int rollDie() {
        return this.eightSidedDie.roll();
    }

    //PRIVATE CLASS METHODS
    private void setNextPlayerTurn() {
        if(this.currentPlayerTurn == 1)
            this.currentPlayerTurn = 2;
        else
            this.currentPlayerTurn = 1;
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

    private void addToRunningTotal(int points) {
        this.runningTotal = points;
    }

    private void resetPlayerTurn() {
        this.currentPlayerTurn = 1;
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