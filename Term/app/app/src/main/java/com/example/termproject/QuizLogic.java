package com.example.termproject;

import java.io.Serializable;
import java.util.ArrayList;

public class QuizLogic implements Serializable {
    // STATIC CATEGORY TAGS
    public static final String FAST_DEV_TIME = "FAST_DEV_TIME"; // ALSO EASE OF USE
    public static final String MAINTAINABILITY = "MAINTAINABILITY";
    public static final String EFFICIENCY_AND_SPEED = "EFFICIENCY_AND_SPEED";
    public static final String STRUCTURE = "STRUCTURE";
    public static final String SCRIPTING_LANGUAGE = "SCRIPTING_LANGUAGE";
    public static final String TRADITIONAL_LANGUAGE = "TRADITIONAL_LANGUAGE";
    public static final String NO_SPECIFIC_CATEGORY = "NO_SPECIFIC_CATEGORY"; // this exists because sometimes only one particular answer matters in a quiz question

    // RECOMMENDED TECH STATIC TAGS
    public static final String REACT_JS = "REACT_JS";
    public static final String ANGULAR = "ANGULAR";
    public static final String VUE = "VUE";
    public static final String REDUX = "REDUX";
    public static final String BOOTSTRAP = "BOOTSTRAP";
    public static final String STYLED_COMPONENTS = "STYLED_COMPONENTS";
    public static final String MATERIAL_UI = "MATERIAL_UI";
    public static final String ASP_DOTNET = "ASP_DOTNET";
    public static final String NODE_JS = "NODE_JS";
    public static final String EXPRESS_JS = "EXPRESS_JS";
    public static final String ASP_DOTNET_WEB_API = "ASP_DOTNET_WEB_API";
    public static final String DJANGO = "DJANGO";
    public static final String MONGODB = "MONGODB";
    public static final String MYSQL = "MYSQL";
    public static final String POSTGRESQL = "POSTGRESQL";

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
