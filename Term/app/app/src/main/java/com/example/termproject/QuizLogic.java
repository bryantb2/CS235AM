package com.example.termproject;

import android.util.Log;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class QuizLogic implements Serializable {
    // STATIC CATEGORY TAGS (these are the general quiz categories that help determine a user's lean, or tech-attribute preference)
    public static final String SCRIPTING_LANGUAGE = "SCRIPTING_LANGUAGE";
    public static final String TRADITIONAL_LANGUAGE = "TRADITIONAL_LANGUAGE";
    public static final String FAST_DEV_TIME = "FAST_DEV_TIME"; // ALSO EASE OF USE
    public static final String MAINTAINABILITY = "MAINTAINABILITY";
    public static final String EFFICIENCY_AND_SPEED = "EFFICIENCY_AND_SPEED";
    public static final String STRUCTURE = "STRUCTURE";
    public static final String NO_SPECIFIC_CATEGORY = "NO_SPECIFIC_CATEGORY"; // this exists because sometimes only one particular answer matters in a quiz question

    // RECOMMENDED TECH STATIC TAGS (these are the technologies that will ultimately be recommended)
    public static final String REACT_JS = "REACT_JS";
    public static final String ANGULAR = "ANGULAR";
    //public static final String VUE = "VUE";
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

    // PRIVATE TECH TYPE CONSTANT TAGS (these describe the various technologies listed above)
    private final String JS_FRAMEWORK="JS_FRAMEWORK";
    private final String DOTNET_FRAMEWORK="DOTNET_FRAMEWORK";
    private final String PYTHON_WEB_FRAMEWORK="DOTNET_FRAMEWORK";
    private final String CSS_FRAMEWORK="CSS_FRAMEWORK";
    private final String CSS_MODULE="CSS_MODULE";
    private final String STATE_MANAGEMENT="STATE_MANAGEMENT";
    private final String JS_SERVER_FRAMEWORK="JS_SERVER_FRAMEWORK";
    private final String DOTENET_SERVER_FRAMEWORK="DOTENET_SERVER_FRAMEWORK";
    private final String NON_RELATIONAL_DB="NON_RELATIONAL_DB";
    private final String RELATIONAL_DB="RELATIONAL_DB";

    // STATIC SECTION TAGS
    public static final String GENERAL_SECTION = "GENERAL_SECTION";
    public static final String FRONT_END_SECTION = "FRONT_END_SECTION";
    public static final String BACK_END_SECTION = "BACK_END_SECTION";
    public static final String DB_SECTION = "DB_SECTION";

    // CLASS FIELDS
    private final int lean = 2; // +2 points for what category the lean is in
    private String generalCategoryLean;
    private String languageLean;
    private String PreferedFrontEndTech;
    private String PreferedCSSFramework;
    private String PreferedBackEndTech;
    private String PreferedDBTech;

    private ArrayList<QuizRecommendation> finalRecommendations = new ArrayList<QuizRecommendation>(); // stores the recommendations that will be returned and shown in the UI
    private ArrayList<QuizRecommendation> preGeneratedRecommendations = new ArrayList<QuizRecommendation>(); // this just stores all of the possible recommendations that are generated

    public QuizLogic() {
        // Pre-fill quiz recommendation list with recommendation objects
        BuildAndAddRecommendations();
    }

    // METHODS
    public ArrayList<QuizRecommendation> GetRecommendations() {
        return this.finalRecommendations;
    }

    public void PassNCalcTestResults(ArrayList<ArrayList<String>> testResults) {
        // takes in ArrayList of ArrayList strings
            // first four embedded arraylists are section results
            // the last is an ArrayList of category strings that corresponds to the order and type of the section results
        // parse answers into their own ArrayLists
        // calc general lean
        // calc pref technology for each section
        // apply lean
        // calc final recommendations
        ArrayList<String> generalSectionAnswers = GetSectionResultsByCategory(GENERAL_SECTION, testResults);
        ArrayList<String> frontEndSectionAnswers = GetSectionResultsByCategory(FRONT_END_SECTION, testResults);
        ArrayList<String> backEndSectionAnswers = GetSectionResultsByCategory(BACK_END_SECTION, testResults);
        ArrayList<String> dbSectionAnswers = GetSectionResultsByCategory(DB_SECTION, testResults);

        CalcLeans(generalSectionAnswers);

    }

    private void CalcPrefTechnologyBySection(ArrayList<String> testResults, String sectionTag) {
        //TODO: finish this
    }

    private void CalcLeans(ArrayList<String> generalSectionResults) {
        // this method utilizes the answer converter's methods to get two things:
            // the category that corresponds to general section quiz answer
            // the language bias of the user
        // the category returned from each answer will be:
            // added to several local running counter representing the various quiz categories
        // the two highest categories will be determined and then set to the class variable
        int traditionalLanguageBias = 0;
        int scriptingLanguageBias = 0;
        int[] categoryOccuranceTracker = new int[4];

        // array counter indices
        final int FAST_DEV_TIME_INDEX = 0;
        final int MAINTAINABILITY_INDEX = 1;
        final int EFFICIENCY_INDEX = 2;
        final int STRUCTURE_INDEX = 3;
        //final int NO_CATEGORY_INDEX = 4;

        // Getting and setting general answer results
        final int CATEGORY_INCREMENTOR = 1;
        for(int i = 0; i < generalSectionResults.size(); i++) {
            // since the order of the quiz results haven't changed,
            // the index+1 is the question number (a natural index is required for the answer converter)
            int questionNumber = i + 1;
            int answerPosition = Integer.parseInt(generalSectionResults.get(i));
            String answerAsCategory = AnswerConverter.DetermineGeneralSectionLean(questionNumber,answerPosition);
            if(answerAsCategory == FAST_DEV_TIME) {
                int oldCount = categoryOccuranceTracker[FAST_DEV_TIME_INDEX];
                categoryOccuranceTracker[FAST_DEV_TIME_INDEX] = (oldCount + CATEGORY_INCREMENTOR);
            }
            else if(answerAsCategory == MAINTAINABILITY) {
                int oldCount = categoryOccuranceTracker[MAINTAINABILITY_INDEX];
                categoryOccuranceTracker[MAINTAINABILITY_INDEX] = (oldCount + CATEGORY_INCREMENTOR);
            }
            else if(answerAsCategory == EFFICIENCY_AND_SPEED) {
                int oldCount = categoryOccuranceTracker[EFFICIENCY_INDEX];
                categoryOccuranceTracker[EFFICIENCY_INDEX] = (oldCount + CATEGORY_INCREMENTOR);
            }
            else if(answerAsCategory == STRUCTURE) {
                int oldCount = categoryOccuranceTracker[STRUCTURE_INDEX];
                categoryOccuranceTracker[STRUCTURE_INDEX] = (oldCount + CATEGORY_INCREMENTOR);
            }
            else if(answerAsCategory == TRADITIONAL_LANGUAGE) {
                traditionalLanguageBias += CATEGORY_INCREMENTOR;
            }
            else if(answerAsCategory == SCRIPTING_LANGUAGE) {
                scriptingLanguageBias += CATEGORY_INCREMENTOR;
            }
            else
                Log.d("CAT_LOGGER","No category was returned as an answer category");
        }

        // Determine and set the language bias
        if(scriptingLanguageBias > traditionalLanguageBias)
            this.languageLean = SCRIPTING_LANGUAGE;
        else
            this.languageLean = TRADITIONAL_LANGUAGE;

        // Determine general category lean
        String categoryWithMostPoints = "";
        int mostCategoryPoints = 0;
        for(int i = 0; i <categoryOccuranceTracker.length; i++) {
            if(i == 0) {
                // set initial category points tracker to first element
                categoryWithMostPoints = FAST_DEV_TIME;
                mostCategoryPoints = categoryOccuranceTracker[FAST_DEV_TIME_INDEX];
            }
            else {
                // compared currently indexed points tracker to the highest
                int currentCategoryPoints = categoryOccuranceTracker[i];
                if(currentCategoryPoints > mostCategoryPoints) {
                    // set new highest point counter
                    mostCategoryPoints = currentCategoryPoints;

                    // set the string for the highest point category
                    if(i == FAST_DEV_TIME_INDEX)
                        categoryWithMostPoints = FAST_DEV_TIME;
                    else if(i == MAINTAINABILITY_INDEX)
                        categoryWithMostPoints = MAINTAINABILITY;
                    else if(i == EFFICIENCY_INDEX)
                        categoryWithMostPoints = EFFICIENCY_AND_SPEED;
                    else
                        categoryWithMostPoints = STRUCTURE;
                }
            }
        }
        this.generalCategoryLean = categoryWithMostPoints;
    }

    private ArrayList<String> GetSectionResultsByCategory(String category, ArrayList<ArrayList<String>> resultsList) {
        // method takes in the 2D ArrayList of results and a section category tag
        // returns the proper results list based off the position of the category tag
        ArrayList<String> categoryTagArray = resultsList.get(resultsList.size()-1);
        for(int i =0; i <categoryTagArray.size(); i++) {
            if(categoryTagArray.get(i) == category) {
                // since the category tags are in perfect parallel with the section answers,
                // the index in the category tags correlates with the index of the results array
                return resultsList.get(i);
            }
        }
        return null;
    }

    private void BuildAndAddRecommendations() {
        // FRONTEND RECOMMENDATIONS
        // react js recommendation object
        ArrayList<String> categoryTags = new ArrayList<>();
        categoryTags.add(FAST_DEV_TIME);
        categoryTags.add(SCRIPTING_LANGUAGE);
        QuizRecommendation recommendation = new QuizRecommendation(REACT_JS, JS_FRAMEWORK, FRONT_END_SECTION,categoryTags);
        this.preGeneratedRecommendations.add(recommendation);

        // angular js recommendation object
        categoryTags = new ArrayList<>();
        categoryTags.add(MAINTAINABILITY);
        categoryTags.add(STRUCTURE);
        categoryTags.add(SCRIPTING_LANGUAGE);
        recommendation = new QuizRecommendation(ANGULAR, JS_FRAMEWORK, FRONT_END_SECTION,categoryTags);
        this.preGeneratedRecommendations.add(recommendation);

        // bootstrap AND material css recommendation object
        categoryTags = new ArrayList<>();
        categoryTags.add(FAST_DEV_TIME);
        categoryTags.add(STRUCTURE);
        recommendation = new QuizRecommendation((BOOTSTRAP + " OR "+ MATERIAL_UI), CSS_FRAMEWORK, FRONT_END_SECTION,categoryTags);
        this.preGeneratedRecommendations.add(recommendation);

        // styled components recommendation object
        categoryTags = new ArrayList<>();
        categoryTags.add(MAINTAINABILITY);
        recommendation = new QuizRecommendation((STYLED_COMPONENTS), CSS_MODULE, FRONT_END_SECTION,categoryTags);
        this.preGeneratedRecommendations.add(recommendation);

        // ASP.NET recommendation object
        categoryTags = new ArrayList<>();
        categoryTags.add(FAST_DEV_TIME);
        categoryTags.add(STRUCTURE);
        categoryTags.add(TRADITIONAL_LANGUAGE);
        recommendation = new QuizRecommendation((ASP_DOTNET), DOTNET_FRAMEWORK, FRONT_END_SECTION,categoryTags);
        this.preGeneratedRecommendations.add(recommendation);

        // DJANGO recommendation object
        categoryTags = new ArrayList<>();
        categoryTags.add(FAST_DEV_TIME);
        categoryTags.add(STRUCTURE);
        categoryTags.add(SCRIPTING_LANGUAGE);
        recommendation = new QuizRecommendation((DJANGO), PYTHON_WEB_FRAMEWORK, FRONT_END_SECTION,categoryTags);
        this.preGeneratedRecommendations.add(recommendation);

        // BACKEND RECOMMENDATIONS
        // Node.js recommendation object
        categoryTags = new ArrayList<>();
        categoryTags.add(FAST_DEV_TIME);
        categoryTags.add(EFFICIENCY_AND_SPEED);
        categoryTags.add(SCRIPTING_LANGUAGE);
        recommendation = new QuizRecommendation((NODE_JS), JS_SERVER_FRAMEWORK, BACK_END_SECTION,categoryTags);
        this.preGeneratedRecommendations.add(recommendation);

        // Express.js recommendation object
        categoryTags = new ArrayList<>();
        categoryTags.add(FAST_DEV_TIME);
        categoryTags.add(EFFICIENCY_AND_SPEED);
        categoryTags.add(STRUCTURE);
        categoryTags.add(SCRIPTING_LANGUAGE);
        recommendation = new QuizRecommendation((EXPRESS_JS), JS_SERVER_FRAMEWORK, BACK_END_SECTION,categoryTags);
        this.preGeneratedRecommendations.add(recommendation);

        // ASP.net web api recommendation object
        categoryTags = new ArrayList<>();
        categoryTags.add(MAINTAINABILITY);
        categoryTags.add(STRUCTURE);
        categoryTags.add(TRADITIONAL_LANGUAGE);
        recommendation = new QuizRecommendation((ASP_DOTNET_WEB_API), DOTENET_SERVER_FRAMEWORK, BACK_END_SECTION,categoryTags);
        this.preGeneratedRecommendations.add(recommendation);

        // DB RECOMMENDATIONS
        // Mongodb recommendation object
        categoryTags = new ArrayList<>();
        categoryTags.add(FAST_DEV_TIME);
        categoryTags.add(EFFICIENCY_AND_SPEED);
        recommendation = new QuizRecommendation((MONGODB), NON_RELATIONAL_DB, DB_SECTION,categoryTags);
        this.preGeneratedRecommendations.add(recommendation);

        // MySQL and PostgreSQL recommendation object
        categoryTags = new ArrayList<>();
        categoryTags.add(STRUCTURE);
        recommendation = new QuizRecommendation((MYSQL + " OR " + POSTGRESQL), RELATIONAL_DB, DB_SECTION,categoryTags);
        this.preGeneratedRecommendations.add(recommendation);
    }
}
