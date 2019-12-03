package com.example.termproject;

import java.io.Serializable;
import java.util.ArrayList;

public class QuizRecommendation implements Serializable {
    // STATIC FIELDS


    // CLASS FIELDS
    private String technologyName;
    private String typeOfTechnology;
    private String stackCategory;
    private ArrayList<String> quizCategoryTags;

    public QuizRecommendation(String techName, String techType,
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
