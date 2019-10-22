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
    private int lastRolledNumber;
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
        //this method takes the class's currentPlayerTurn variable and gets the current player's name as a string
        if(currentPlayerTurn == 1) {
            return this.player1.getName();
        }
        else {
            return this.player2.getName();
        }
    }

    public int getCurrentPlayerNumber() {
        //this method returns the number of the player that is currently rolling
        return this.currentPlayerTurn;
    }

    public void setCurrentPlayerTurn(int playerNumber) {
        this.currentPlayerTurn = playerNumber;
    }

    public int getPlayerScore(int playerNumber) {
        int points;
        if(playerNumber==1) {
            points = player1.getPoints();
        }
        else {
            points = player2.getPoints();
        }
        return points;
    }

    public void setPlayerScore(int playerNumber, int points) {
        //this method will be used to restore player point numbers after some lifecycle change has occured
        if(playerNumber==1) {
            this.player1.addPoints(points);
        }
        else {
            this.player2.addPoints(points);
        }
    }

    public int getPointsForCurrentTurn() {
        return runningTotal;
    }

        public void setPointsForCurrentTurn(int points) {
        this.runningTotal = points;
    }

    public int getLastRolledNumber() {
        return this.lastRolledNumber;
    }

    public void setLastRolledNumber(int rolledNumber) {
        this.lastRolledNumber = rolledNumber;
    }

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

    public int EndTurn() {
        //add runningTotal to player score
        //calculate winner
        //reset runningTotal
        //CALCULATE WINNER WOULD GET CALLED WITH THIS
        //set next player turn to opposite of current player turn
        this.addToPlayerScore(currentPlayerTurn,runningTotal);
        int winner = this.CalcWinner();
        if(winner != 0) {
            //if the return int is not 0, then there is a winner
            return winner;
        }
        else {
            //otherwise reset the internal game mechanisms for next player
            this.resetRunningTotal();
            this.setNextPlayerTurn();
        }
        //returns zero if no winner
        return 0;
    }

    public void RestartGame(String player1Name, String player2Name) {
        //reset player names
        //reset player scores
        //reset internal running score
        //set player turn back to default
        //reset points limit first tracker
        this.player1.setName(player1Name);
        this.player2.setName(player2Name);
        this.resetPlayerScore(1);
        this.resetPlayerScore(2);
        this.resetPlayerTurn();
        this.resetRunningTotal();
        this.playerOneReached100 = false;
    }

    public int CalcWinner() {
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

        final int player1 = 1;
        final int player2 = 2;

        if (player2Point >= 100) {
            return player2;
        }
        else if(player1Points >= 100) {
            if(this.playerOneReached100 == true && player1Points > player2Point) {
                return player1;
            }
            this.playerOneReached100 = true;
        }
        //this returns if there are no winners
        return 0;
    }

    //PRIVATE CLASS METHODS
    private int rollDie() {
        return this.eightSidedDie.roll();
    }

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
        this.runningTotal += points;
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