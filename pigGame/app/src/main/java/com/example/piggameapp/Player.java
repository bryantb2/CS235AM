package com.example.piggameapp;

public class Player {
    //CLASS FIELDS
    private int points;
    private String name;

    //CONSTRUCTOR
    public Player(String name) {
        this.name = name;
        this.points = 0;
    }

    //PROPERTIES
    public String getName() {
        return this.name;
    }

    public int getPoints() {
        return this.points;
    }

    //METHODS
    public void addPoints(int points) {
        this.points += points;
    }

    public void wipePoints() {
        this.points = 0;
    }
}



/*
Player has these properties:
score
name

Player has these methods:
addPoints
wipePoints
getPoint
 */