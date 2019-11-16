package com.example.tideprediction;

public class TideItem {

    private String date = null;
    private String day = null;
    private String time = null;
    private String predInFt = null;
    private String predInCm = null;
    private String highLow = null;

    //getters and setters to access the fields' values
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }
    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public String getPredInFt() {
        return predInFt;
    }
    public void setPredInFt(String predInFt) {
        this.predInFt = predInFt;
    }

    public String getPredInCm() {
        return predInCm;
    }
    public void setPredInCm(String predictionInCm) {
        this.predInCm = predictionInCm;
    }

    public String getHighlow() {
        return highLow;
    }
    public void setHighlow(String highlow) {
        this.highLow = highlow;
    }
}