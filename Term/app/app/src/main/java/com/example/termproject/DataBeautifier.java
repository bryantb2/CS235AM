package com.example.termproject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DataBeautifier {
    /*
    This class is meant to serve the role of converting ugly backend properties, strings, constants, ect. into data
    that is neat presentable for the UI. In the flow of information, this class is meant to be an intermediary data layer
    between the QuizLogic and ResultsActivity class.
     */
    public static ArrayList<QuizRecommendation> BeautifyAndReturnRecommendations(ArrayList<QuizRecommendation> recommendations) {
        // breakup list into basic object properties
        // call beautify helper methods on their respective properties
        // call rebuild recommendation
        ArrayList<QuizRecommendation> beautifiedList = new ArrayList<>();
        for(int i = 0; i < recommendations.size(); i++) {
            // store old property values
            QuizRecommendation tempRecommendation = recommendations.get(i);
            String oldTechName = tempRecommendation.GetTechnologyName();
            String oldTechType = tempRecommendation.GetTypeOfTech();
            String oldStackCat = tempRecommendation.GetStackCategory();
            ArrayList<String> oldTagList = tempRecommendation.GetQuizTags();

            // beautify and capture new values
            String revisedName = BeautifyTechName(oldTechName);
            String revisedType = BeautifyTechType(oldTechType);
            String revisedStackCat = BeautifyStackCategory(oldStackCat);
            ArrayList<String> revisedTagList = BeautifyQuizTags(oldTagList);

            // build new object and add it to tempList
            QuizRecommendation beautifiedRecommendation = RebuildBeautifiedRecommendation(revisedName, revisedType, revisedStackCat, revisedTagList);
            beautifiedList.add(beautifiedRecommendation);
        }
        return beautifiedList;
    }

    private static String BeautifyTechName(String name) {
        // find the category name and then return a user-friendly version of the string
        String returnString = "";
        if(name.equals(QuizLogic.ANGULAR))
            returnString = "Angular JS";
        else if(name.equals(QuizLogic.REACT_JS))
            returnString = "React JS";
        else if(name.equals(QuizLogic.BOOTSTRAP))
            returnString = "Bootstrap";
        else if(name.equals(QuizLogic.STYLED_COMPONENTS))
            returnString = "Styled Components";
        else if(name.equals(QuizLogic.MATERIAL_UI))
            returnString = "Material UI";
        else if(name.equals(QuizLogic.ASP_DOTNET))
            returnString = "ASP.net";
        else if(name.equals(QuizLogic.NODE_JS))
            returnString = "Node.JS";
        else if(name.equals(QuizLogic.EXPRESS_JS))
            returnString = "Express.JS";
        else if(name.equals(QuizLogic.ASP_DOTNET_WEB_API))
            returnString = "ASP.net Web API";
        else if(name.equals(QuizLogic.DJANGO))
            returnString = "Django";
        else if(name.equals(QuizLogic.MONGODB))
            returnString = "MongoDB";
        else if(name.equals(QuizLogic.MYSQL))
            returnString = "MySQL";
        else
            returnString = "PostGreSQL";
        return returnString;
    }

    private static String BeautifyTechType(String type) {
        // find the category name and then return a user-friendly version of the string
        String returnString = "";
        if(type.equals(QuizLogic.JS_FRAMEWORK))
            returnString = "JS Framework/Library";
        else if(type.equals(QuizLogic.DOTNET_FRAMEWORK))
            returnString = ".NET Framework";
        else if(type.equals(QuizLogic.PYTHON_WEB_FRAMEWORK))
            returnString = "Python Web Framework";
        else if(type.equals(QuizLogic.CSS_FRAMEWORK))
            returnString = "CSS Framework";
        else if(type.equals(QuizLogic.CSS_MODULE))
            returnString = "CSS Module";
        else if(type.equals(QuizLogic.JS_SERVER_FRAMEWORK))
            returnString = "JS Server Framework";
        else if(type.equals(QuizLogic.DOTENET_SERVER_FRAMEWORK))
            returnString = ".NET Server Framework";
        else if(type.equals(QuizLogic.NON_RELATIONAL_DB))
            returnString = "Non-Relational DB";
        else
            returnString = "Relational DB";
        return returnString;
    }

    private static String BeautifyStackCategory(String stackCat) {
        // find the category name and then return a user-friendly version of the string
        String returnString = "";
        if(stackCat.equals(QuizLogic.FRONT_END_SECTION))
            returnString = "Front-End Tech";
        else if(stackCat.equals(QuizLogic.BACK_END_SECTION))
            returnString = "Back-End Tech";
        else
            returnString = "DB Tech";
        return returnString;
    }

    private static ArrayList<String> BeautifyQuizTags(ArrayList<String> tags) {
        // find the category name and then return a user-friendly version of the string
        ArrayList<String> returnList = new ArrayList<>();
        for(String tag : tags) {
            if(tag.equals(QuizLogic.SCRIPTING_LANGUAGE))
                returnList.add("Scripting Language");
            else if(tag.equals(QuizLogic.TRADITIONAL_LANGUAGE))
                returnList.add("Traditional Language");
            else if(tag.equals(QuizLogic.FAST_DEV_TIME))
                returnList.add("Fast Dev Time");
            else if(tag.equals(QuizLogic.MAINTAINABILITY))
                returnList.add("Maintainability");
            else if(tag.equals(QuizLogic.EFFICIENCY_AND_SPEED))
                returnList.add("Efficiency and Speed");
            else if(tag.equals(QuizLogic.STRUCTURE))
                returnList.add("Structure");
        }
        return returnList;
    }

    private static QuizRecommendation RebuildBeautifiedRecommendation(String techName, String techType, String stackCategory, ArrayList<String> quizTags) {
        // call constructor and pass in params
        QuizRecommendation newRecommendation = new QuizRecommendation(techName, techType, stackCategory, quizTags);
        return newRecommendation;
    }
}
