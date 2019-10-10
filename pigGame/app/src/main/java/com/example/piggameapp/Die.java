package com.example.piggameapp;

public class Die {
    //CLASS FIELDS
    private int numberOfSides;

    //CONSTRUCTOR
    public Die(int sides) {
        this.numberOfSides = sides;
    }

    //METHODS
    public int roll() {
        int outcome = numberGenerator(0,this.numberOfSides);
        return outcome;
    }

    private int numberGenerator(int min, int max) {
        int outcome = (int)(Math.random()*((max-min)+1)+min);
        return outcome;
    }
}
/*
Die has these properties:
numberOfSides

Die has these methods:
roll
 */