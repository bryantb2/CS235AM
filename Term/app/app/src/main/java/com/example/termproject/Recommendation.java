package com.example.termproject;

import java.util.ArrayList;

public class Recommendation {
    // CLASS FIELDS
    private String technologyName;
    private String typeOfTechnology;
    private String stackCategory;
    private ArrayList<String> quizCategoryTags;

    public Recommendation(String techName, String techType,
                          String stackCat, ArrayList<String> quizCatTags) {
        this.technologyName = techName;
        this.typeOfTechnology = techType;
        this.stackCategory = stackCat;
        this.quizCategoryTags = quizCatTags;
    }

    public String GetTechnologyName() {
        return this.technologyName;
    }

    public String GetTypeOfTech() {
        return this.typeOfTechnology;
    }

    public String GetStackCategory() {
        return this.stackCategory;
    }

    public ArrayList<String> GetQuizTags() {
        return this.quizCategoryTags;
    }
}
