package com.example.termproject;

import java.io.Serializable;
import java.util.ArrayList;

public class QuizLogic implements Serializable {
    // STATIC CATEGORY TAGS
    public static final String FAST_DEV_TIME = "FAST_DEV_TIME";
    public static final String FRAMEWORK_MATURITY = "FRAMEWORK_MATURITY";
    public static final String EASE_OF_USE = "EASE_OF_USE";
    public static final String UBIQUITY = "UBIQUITY";
    public static final String MAINTAINABILITY = "MAINTAINABILITY";
    public static final String EFFICIENCY_AND_SPEED = "EFFICIENCY_AND_SPEED";
    public static final String STRUCTURE = "STRUCTURE";
    public static final String NO_SPECIFIC_CATEGORY = "NO_SPECIFIC_CATEGORY"; // this exists because sometimes only one particular answer matters in a quiz question

    // STATIC SECTION TAGS
    public static final String GENERAL_SECTION = "GENERAL_SECTION";
    public static final String FRONT_END_SECTION = "FRONT_END_SECTION";
    public static final String BACK_END_SECTION = "BACK_END_SECTION";
    public static final String DB_SECTION = "DB_SECTION";

    // CLASS FIELDS
    private int lean;
    private String leanCategoryTag;
    private ArrayList<QuizRecommendation> finalRecommendations = new ArrayList<QuizRecommendation>();
    private ArrayList<QuizSection> quizSections = new ArrayList<QuizSection>();



}
