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
    private final int LEAN_POINT_INCREMENTOR = 2; // +2 points for what category the lean is in
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

    // PARENT METHODS -------------------------------------------------------------------------------------------------------------->
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

        EvaluateGeneralAnswersAndCalcLeans(generalSectionAnswers); // gets the quiz leans and sets them to the class variables
        CalcAllPrefTechnologies(frontEndSectionAnswers,backEndSectionAnswers,dbSectionAnswers); // uses quiz leans to determine pref stack technologies
        SetFinalRecommendations();
    }

    // GETTER/SETTER METHODS
    public ArrayList<QuizRecommendation> GetFinalRecommendations() {
        return this.finalRecommendations;
    }

    private void SetFinalRecommendations() {
        // this method will get each of the pref tech class Strings (which were set previously) and get the corresponding recommendation objects
        // will add to the final recommendation object list
        QuizRecommendation cssFramework = GetRecommendationByTechname(this.PreferedCSSFramework);
        QuizRecommendation frontEndTech = GetRecommendationByTechname(this.PreferedFrontEndTech);
        QuizRecommendation backEndTech = GetRecommendationByTechname(this.PreferedBackEndTech);
        QuizRecommendation DBTech = GetRecommendationByTechname(this.PreferedDBTech);

        this.finalRecommendations.add(cssFramework);
        this.finalRecommendations.add(frontEndTech);
        this.finalRecommendations.add(backEndTech);
        this.finalRecommendations.add(DBTech);
    }

    // DATA PARSING METHODS
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

    // CALC LEAN METHODS -------------------------------------------------------------------------------------------------------------->
    private void EvaluateGeneralAnswersAndCalcLeans(ArrayList<String> generalSectionResults) {
        // this method utilizes the answer converter's methods to get two things:
        // the category that corresponds to general section quiz answer
        // the language bias of the user
        // the category returned from each answer will be:
        // added to several local running counters representing the various quiz categories
        // the highest language and general category will be determined and then set to their respective class variables
        int traditionalLanguageBias = 0;
        int scriptingLanguageBias = 0;
        int[] categoryOccuranceTracker = new int[4];

        // array counter indices
        final int FAST_DEV_TIME_INDEX = 0;
        final int MAINTAINABILITY_INDEX = 1;
        final int EFFICIENCY_INDEX = 2;
        final int STRUCTURE_INDEX = 3;

        // Getting and setting general answer results
        final int CATEGORY_INCREMENTOR = 1;
        for(int i = 0; i < generalSectionResults.size(); i++) {
            // since the order of the quiz results haven't changed,
            // the index+1 is the question number (a natural index is required for the answer converter)
            int questionNumber = i + 1;
            int answerPosition = Integer.parseInt(generalSectionResults.get(i));
            String answerAsCategory = AnswerConverter.ConvertGeneralQuestionToCategory(questionNumber,answerPosition);
            if(answerAsCategory == FAST_DEV_TIME) {
                categoryOccuranceTracker[FAST_DEV_TIME_INDEX] += CATEGORY_INCREMENTOR;
            }
            else if(answerAsCategory == MAINTAINABILITY) {
                int oldCount = categoryOccuranceTracker[MAINTAINABILITY_INDEX];
                categoryOccuranceTracker[MAINTAINABILITY_INDEX] += CATEGORY_INCREMENTOR;
            }
            else if(answerAsCategory == EFFICIENCY_AND_SPEED) {
                int oldCount = categoryOccuranceTracker[EFFICIENCY_INDEX];
                categoryOccuranceTracker[EFFICIENCY_INDEX] += CATEGORY_INCREMENTOR;
            }
            else if(answerAsCategory == STRUCTURE) {
                int oldCount = categoryOccuranceTracker[STRUCTURE_INDEX];
                categoryOccuranceTracker[STRUCTURE_INDEX] += CATEGORY_INCREMENTOR;
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

    // CALC PREF SECTION TECH / APPLY LEAN METHODS -------------------------------------------------------------------------------------------------------------->
    private void CalcAllPrefTechnologies(ArrayList<String> frontEndAnswers, ArrayList<String> backEndAnswers, ArrayList<String> dbAnswers) {
        // takes in parsed section answers
        // calls CalcPrefTechnologyBySection to get the preferred tech
        String prefFrontEndTech = CalcPrefTechBySection(frontEndAnswers,FRONT_END_SECTION, false);
        String prefCSSFramework = CalcPrefTechBySection(frontEndAnswers,FRONT_END_SECTION, true);
        String prefBackEndTech = CalcPrefTechBySection(backEndAnswers,BACK_END_SECTION,false);
        String prefDBTech = CalcPrefTechBySection(dbAnswers,DB_SECTION,false);

        this.PreferedFrontEndTech = prefFrontEndTech;
        this.PreferedCSSFramework = prefCSSFramework;
        this.PreferedBackEndTech = prefBackEndTech;
        this.PreferedDBTech = prefDBTech;
    }

    private String CalcPrefTechBySection(ArrayList<String> sectionAnswers, String categoryTag, boolean calculateCSSFramework) {
        // Determine which tag has been passed in
        // utilize the answer converter to get the tech that corresponds to each answer
        // the tech choice returned from this method will be reached by:
            // added to several local running counters representing the various quiz tech answers
            // apply a lean to the finished, counted, tech categories
            // returning the tech with the highest point value
        String sectionTech = "";

        // tech counter array
        int cssFramework = 0;
        int cssModules = 0;
        int[] techOccuranceTracker = new int[4];

        // array counter indices
        final int ANGULAR_INDEX = 0;
        final int ASP_DOTNET_INDEX = 1;
        final int REACT_JS_INDEX = 2;
        final int DJANO_INDEX = 3;

        // array counter indices
        final int NODE_JS_INDEX = 0;
        final int EXPRESS_JS_INDEX = 1;
        final int ASP_DOTNET_WEB_API_INDEX = 2;

        // array counter indices
        final int MONGODB_INDEX = 0;
        final int MYSQL_INDEX = 1;
        final int POSTGRESQL = 2;

        // Getting and setting front end section answer results
        final int TECH_RESPONSE_INCREMENTOR = 1;
        for(int i = 0; i < sectionAnswers.size(); i++) {
            // since the order of the quiz results haven't changed,
            // the index+1 is the question number (a natural index is required for the answer converter)
            int questionNumber = i + 1;
            int answerPosition = Integer.parseInt(sectionAnswers.get(i)); // parsing as int because the sectionAnswers are originally sent to second activity as strings
            ArrayList<String> answerAsTechnologies = AnswerConverter.ConvertSectionQuestionToTech(categoryTag,questionNumber,answerPosition);

            // will loop through the list of answers from the converter (NOTE: not all answers will have a list, but they will still be contained within an ArrayList data structure)
            for(int j = 0; j < answerAsTechnologies.size(); j++) {
                if(categoryTag == FRONT_END_SECTION) {
                    // loops through the returned ArrayList
                    // find the technology and increment its counter
                    if(answerAsTechnologies.get(j) == ANGULAR) {
                        techOccuranceTracker[ANGULAR_INDEX] += TECH_RESPONSE_INCREMENTOR;
                    }
                    else if(answerAsTechnologies.get(j)== ASP_DOTNET) {
                        techOccuranceTracker[ASP_DOTNET_INDEX] += TECH_RESPONSE_INCREMENTOR;
                    }
                    else if(answerAsTechnologies.get(j)== DJANGO) {
                        techOccuranceTracker[DJANO_INDEX] += TECH_RESPONSE_INCREMENTOR;
                    }
                    else if(answerAsTechnologies.get(j)== REACT_JS) {
                        techOccuranceTracker[REACT_JS_INDEX] += TECH_RESPONSE_INCREMENTOR;
                    }
                    else if(answerAsTechnologies.get(j)== MATERIAL_UI || answerAsTechnologies.get(j)== BOOTSTRAP) {
                        cssFramework += TECH_RESPONSE_INCREMENTOR;
                    }
                    else {
                        cssModules += TECH_RESPONSE_INCREMENTOR;
                    }
                }
                else if(categoryTag == BACK_END_SECTION) {
                    // loops through the returned ArrayList
                    // find the technology and increment its counter
                    if(answerAsTechnologies.get(j) == NODE_JS) {
                        techOccuranceTracker[NODE_JS_INDEX] += TECH_RESPONSE_INCREMENTOR;
                    }
                    else if(answerAsTechnologies.get(j)== EXPRESS_JS) {
                        techOccuranceTracker[EXPRESS_JS_INDEX] += TECH_RESPONSE_INCREMENTOR;
                    }
                    else {
                        techOccuranceTracker[ASP_DOTNET_WEB_API_INDEX] += TECH_RESPONSE_INCREMENTOR;
                    }
                }
                else {
                    // loops through the returned ArrayList
                    // find the technology and increment its counter
                    if(answerAsTechnologies.get(j) == MONGODB) {
                        techOccuranceTracker[MONGODB_INDEX] += TECH_RESPONSE_INCREMENTOR;
                    }
                    else if(answerAsTechnologies.get(j)== MYSQL) {
                        techOccuranceTracker[MYSQL_INDEX] += TECH_RESPONSE_INCREMENTOR;
                    }
                    else {
                        techOccuranceTracker[POSTGRESQL] += TECH_RESPONSE_INCREMENTOR;
                    }
                }
            }
        }
        if(calculateCSSFramework == true && categoryTag == FRONT_END_SECTION) {
            // if this boolean is true, it means the user wants the selected cssFramework instead of the frontend tech
            // NOTE: the css framework tech is part of the front end section, so that's why this is outside the loop
            if(cssModules > cssFramework)
                return STYLED_COMPONENTS;
            else
                return BOOTSTRAP;
        }
        return EvaluateTechCounterAndApplyLean(categoryTag,techOccuranceTracker); // pass in answers and calc pref section tech
    }

    private String EvaluateTechCounterAndApplyLean(String sectionCategory, int[] techCounterArray) {
        // method will take in a section category and an array that corresponds to the number of times a particular technology has been returned from the answer converter
        // the lean method will be run on the techcounter
        // then, a conditional block will loop through the number of points for each technology
        // method will return the string that represents the most scored technology
        int[] techCounterWithLean = ApplyLeanToPrefTechnologyCounter(techCounterArray,sectionCategory);
        String techWithMostPoints = "";
        // array counter indices
        final int ANGULAR_INDEX = 0;
        final int ASP_DOTNET_INDEX = 1;
        final int REACT_JS_INDEX = 2;
        final int DJANO_INDEX = 3;

        // array counter indices
        final int NODE_JS_INDEX = 0;
        final int EXPRESS_JS_INDEX = 1;
        final int ASP_DOTNET_WEB_API_INDEX = 2;

        // array counter indices
        final int MONGODB_INDEX = 0;
        final int MYSQL_INDEX = 1;
        final int POSTGRESQL_INDEX = 2;

        // Determine pref section technology
        int mostTechPoints = 0;
        for(int i = 0; i <techCounterWithLean.length; i++) {
            if(i == 0) {
                if(sectionCategory == FRONT_END_SECTION) {
                    // set initial category points tracker to first element
                    techWithMostPoints = ANGULAR;
                    mostTechPoints = techCounterWithLean[ANGULAR_INDEX];
                }
                else if(sectionCategory == BACK_END_SECTION) {
                    // set initial category points tracker to first element
                    techWithMostPoints = NODE_JS;
                    mostTechPoints = techCounterWithLean[NODE_JS_INDEX];
                }
                else {
                    // set initial category points tracker to first element
                    techWithMostPoints = MONGODB;
                    mostTechPoints = techCounterWithLean[MONGODB_INDEX];
                }
            }
            else {
                // compare currently indexed points element to the highest point tracker
                int currentCategoryPoints = techCounterWithLean[i];
                if(currentCategoryPoints > mostTechPoints) {
                    // set new highest point counter
                    mostTechPoints = currentCategoryPoints;

                    if(sectionCategory == FRONT_END_SECTION) {
                        // set the string for the highest point category
                        if(i == ANGULAR_INDEX)
                            techWithMostPoints = ANGULAR;
                        else if(i == REACT_JS_INDEX)
                            techWithMostPoints = REACT_JS;
                        else if(i == ASP_DOTNET_INDEX)
                            techWithMostPoints = ASP_DOTNET;
                        else
                            techWithMostPoints = DJANGO;
                    }
                    else if(sectionCategory == BACK_END_SECTION) {
                        // set the string for the highest point category
                        if(i == NODE_JS_INDEX)
                            techWithMostPoints = NODE_JS;
                        else if(i == EXPRESS_JS_INDEX)
                            techWithMostPoints = EXPRESS_JS;
                        else
                            techWithMostPoints = ASP_DOTNET_WEB_API;
                    }
                    else {
                        // set the string for the highest point category
                        if(i == MONGODB_INDEX)
                            techWithMostPoints = NODE_JS;
                        else if(i == MYSQL_INDEX)
                            techWithMostPoints = MYSQL;
                        else
                            techWithMostPoints = POSTGRESQL;
                    }
                }
            }
        }
        return techWithMostPoints;
    }

    private int[] ApplyLeanToPrefTechnologyCounter(int[] rawTechCounterArray, String sectionCategoryTag) {
        // takes in an array that represents the number of times a particular technology resulted from a quiz answer
        // will determine which category calculation rules to apply, since each section has different property names and quantities

        // Functionality:
            // loop through counter array
            // foreach element,
                // get it's corresponding tech recommendation object
                    // loop through object category tags
                    // if one of the tags is the lean category
                        // add two points to array element
                    // otherwise, go to next array counter element
            // return counter array
        int[] counterArrayWithLean = rawTechCounterArray;
        // array counter indices
        final int ANGULAR_INDEX = 0;
        final int ASP_DOTNET_INDEX = 1;
        final int REACT_JS_INDEX = 2;
        final int DJANO_INDEX = 3;

        // array counter indices
        final int NODE_JS_INDEX = 0;
        final int EXPRESS_JS_INDEX = 1;
        final int ASP_DOTNET_WEB_API_INDEX = 2;

        // array counter indices
        final int MONGODB_INDEX = 0;
        final int MYSQL_INDEX = 1;
        final int POSTGRESQL_INDEX = 2;

        for(int i = 0; i < rawTechCounterArray.length; i++) {
            if(sectionCategoryTag == FRONT_END_SECTION) {
                if(i == ANGULAR_INDEX) {
                    // apply incrementer
                    counterArrayWithLean[ANGULAR_INDEX] += GetRecommendationLeanPointsByTechname(ANGULAR);
                }
                else if(i == REACT_JS_INDEX) {
                    // apply incrementer
                    counterArrayWithLean[REACT_JS_INDEX] += GetRecommendationLeanPointsByTechname(REACT_JS);
                }
                else if(i == ASP_DOTNET_INDEX) {
                    // apply incrementer
                    counterArrayWithLean[ASP_DOTNET_INDEX] += GetRecommendationLeanPointsByTechname(ASP_DOTNET);
                }
                else {
                    // apply incrementer
                    counterArrayWithLean[DJANO_INDEX] += GetRecommendationLeanPointsByTechname(DJANGO);
                }
            }
            else if(sectionCategoryTag == BACK_END_SECTION) {
                if(i == NODE_JS_INDEX) {
                    // apply incrementer
                    counterArrayWithLean[NODE_JS_INDEX] += GetRecommendationLeanPointsByTechname(NODE_JS);
                }
                else if(i == EXPRESS_JS_INDEX) {
                    // apply incrementer
                    counterArrayWithLean[EXPRESS_JS_INDEX] += GetRecommendationLeanPointsByTechname(EXPRESS_JS);
                }
                else {
                    // apply incrementer
                    counterArrayWithLean[ASP_DOTNET_WEB_API_INDEX] += GetRecommendationLeanPointsByTechname(ASP_DOTNET_WEB_API);
                }
            }
            else {
                if(i == MONGODB_INDEX) {
                    // apply incrementer
                    counterArrayWithLean[MONGODB_INDEX] += GetRecommendationLeanPointsByTechname(MONGODB);
                }
                else if(i == MYSQL_INDEX) {
                    // apply incrementer
                    counterArrayWithLean[MYSQL_INDEX] += GetRecommendationLeanPointsByTechname(MYSQL);
                }
                else {
                    // apply incrementer
                    counterArrayWithLean[POSTGRESQL_INDEX] += GetRecommendationLeanPointsByTechname(POSTGRESQL);
                }
            }
        }
        return counterArrayWithLean;
    }

    private int GetRecommendationLeanPointsByTechname(String recommendationName) {
        // helper method to get the recommendation object by name and return number of lean points
        // NOTE: this method can, and will, return zero if there are no leans applicable to the recommendation
        QuizRecommendation recommendation = GetRecommendationByTechname(recommendationName);
        return CalcNumberOfLeanPoints(recommendation);
    }

    private QuizRecommendation GetRecommendationByTechname(String techName) {
        // this method takes in the string name of a recommendation, finds it in the pre-generated list, and returns it
        for(int i = 0; i < preGeneratedRecommendations.size(); i++) {
            String recommendationTechname = preGeneratedRecommendations.get(i).GetTechnologyName();
            if(recommendationTechname == REACT_JS)
                return preGeneratedRecommendations.get(i);
            else if(recommendationTechname == ANGULAR) {
                return preGeneratedRecommendations.get(i);
            }
            else if(recommendationTechname == BOOTSTRAP) {
                return preGeneratedRecommendations.get(i);
            }
            else if(recommendationTechname == STYLED_COMPONENTS) {
                return preGeneratedRecommendations.get(i);
            }
            else if(recommendationTechname == MATERIAL_UI) {
                return preGeneratedRecommendations.get(i);
            }
            else if(recommendationTechname == ASP_DOTNET) {
                return preGeneratedRecommendations.get(i);
            }
            else if(recommendationTechname == NODE_JS) {
                return preGeneratedRecommendations.get(i);
            }
            else if(recommendationTechname == EXPRESS_JS) {
                return preGeneratedRecommendations.get(i);
            }
            else if(recommendationTechname == ASP_DOTNET_WEB_API) {
                return preGeneratedRecommendations.get(i);
            }
            else if(recommendationTechname == DJANGO) {
                return preGeneratedRecommendations.get(i);
            }
            else if(recommendationTechname == MONGODB) {
                return preGeneratedRecommendations.get(i);
            }
            else if(recommendationTechname == MYSQL) {
                return preGeneratedRecommendations.get(i);
            }
            else if(recommendationTechname == POSTGRESQL) {
                return preGeneratedRecommendations.get(i);
            }
            else {
                throw new Error("we forgot something!");
            }
        }
        return null; // error if it returns null
    }

    private int CalcNumberOfLeanPoints(QuizRecommendation recommendation) {
        boolean doesGeneralLeanApply = DoesRecommendationGetGeneralLean(recommendation);
        boolean doesLanguageLeanApply = DoesRecommendationGetLanguageLean(recommendation);
        if(doesGeneralLeanApply == true && doesLanguageLeanApply == true) { // double the incrementer points
            return LEAN_POINT_INCREMENTOR * 2;
        }
        else if(doesGeneralLeanApply == true || doesLanguageLeanApply == true) { // increment once
            return LEAN_POINT_INCREMENTOR;
        }
        else {
            return 0;
        }
    }

    private boolean DoesRecommendationGetGeneralLean(QuizRecommendation recommendationObj) {
        // loop through recommendation objects tag
            // return true if one of the tags is equivalent to the lean category
            // return false if no such tag exists
        for(int i = 0; i < recommendationObj.GetQuizTags().size(); i++) {
            String currentRecommendationTag = recommendationObj.GetQuizTags().get(i);
            if(currentRecommendationTag == this.generalCategoryLean) {
                return true;
            }
        }
        return false;
    }

    private boolean DoesRecommendationGetLanguageLean(QuizRecommendation recommendationObj) {
        // loop through recommendation objects tag
            // return true if one of the tags is equivalent to the language lean category
            // return false if no such tag exists
        for(int i = 0; i < recommendationObj.GetQuizTags().size(); i++) {
            String currentRecommendationTag = recommendationObj.GetQuizTags().get(i);
            if(currentRecommendationTag == this.languageLean) {
                return true;
            }
        }
        return false;
    }

    // BOILER PLATE METHODS -------------------------------------------------------------------------------------------------------------->
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
