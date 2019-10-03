package com.example.tipcalculator;

public class Percentage {
    //class fields
    private int tipPercent;

    //Constructor
    public Percentage(int userSpecifiedPercent) {
        if(userSpecifiedPercent >= 1) {
            if (userSpecifiedPercent <100) {
                this.tipPercent = percent;
            }
            else {
                throw new java.lang.IllegalArgumentException("tip percent must be less than 100");
            }
        }
        else {
            throw new java.lang.IllegalArgumentException("tip percent must be greater than 0");
        }
    }

    public Percentage() {
        this.tipPercent = 15;
    }

    //Properties
    public int getPercent() {
        return this.tipPercent;
    }

    public void incrementPercent() {
        this.tipPercent = this.tipPercent++;
    }

    public void decrementPercent() {
        if (this.tipPercent > 0) {
            this.tipPercent = this.tipPercent--;
        }
    }
}
