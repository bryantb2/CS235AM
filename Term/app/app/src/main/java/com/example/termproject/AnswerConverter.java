package com.example.termproject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AnswerConverter {

    public static String DetermineGeneralSectionLean(int questionNumber, int questionAnswerPosition) {
        // Takes in a question number, category, and adapter integer position.
        // Each question's answer corresponds to one of the category tags in the main activity
        switch (questionNumber) {
            case 1: // overall project size
                if (questionAnswerPosition == 1)
                    return QuizLogic.FAST_DEV_TIME; // startups care more about quick deployment
                else
                    return QuizLogic.MAINTAINABILITY; // big companies care more about clean code
            case 2: // development time
                if (questionAnswerPosition == 1)
                    return QuizLogic.FAST_DEV_TIME; // shorter timeframes require quicker dev tech
                else
                    return QuizLogic.NO_SPECIFIC_CATEGORY; // longer projects do not need fast dev time
            case 3: // project scalability
                if (questionAnswerPosition == 1)
                    return QuizLogic.EFFICIENCY_AND_SPEED; // shorter timeframes require quicker dev tech
                else
                    return QuizLogic.NO_SPECIFIC_CATEGORY; // no category association because user does not need scalable project
            case 4: // programming language preference
                if (questionAnswerPosition == 1)
                    return QuizLogic.SCRIPTING_LANGUAGE; // JS is ease to use and learn, hard to master
                else
                    return QuizLogic.TRADITIONAL_LANGUAGE; // Java and C# are a bit harder to learn, but have the benefit of being more structured
            case 5: // project performance and efficiency
                if (questionAnswerPosition == 1)
                    return QuizLogic.NO_SPECIFIC_CATEGORY; // user does not have any particular performance requirements
                else if (questionAnswerPosition == 2)
                    return QuizLogic.EFFICIENCY_AND_SPEED; // performance is crucial
            case 6: // project development environment
                if (questionAnswerPosition == 1)
                    return QuizLogic.STRUCTURE; // user will need consistency and structure when working in a dev team
                else if (questionAnswerPosition == 2)
                    return QuizLogic.NO_SPECIFIC_CATEGORY; // user might have a variety of needs, so this answer has no category
            default:
                throw new IllegalArgumentException("something is wrong dude");
        }
    }

    public static ArrayList<String> DeterminePreferedSectionTech(String questionCategory, int questionNumber, int questionAnswerPosition) {
        // Takes in a question number, category, and adapter integer position.
        // Each question's answer corresponds to a recommended technology
        ArrayList<String> tempArr = new ArrayList<String>();
        switch (questionCategory) {
            case QuizLogic.FRONT_END_SECTION:
                switch (questionNumber) {
                    case 1: // amount of structure
                        if(questionAnswerPosition == 1){
                            tempArr.add(QuizLogic.ANGULAR);
                            //tempArr.add(QuizLogic.VUE);
                            tempArr.add(QuizLogic.ASP_DOTNET);
                            return tempArr; // structure is most important aspect
                        }
                        else {
                            tempArr.add(QuizLogic.REACT_JS);
                            tempArr.add(QuizLogic.DJANGO);
                            return tempArr; // big companies care more about clean code
                        }
                    case 2: // UI scalability
                        if(questionAnswerPosition == 1) {
                            tempArr.add(QuizLogic.STYLED_COMPONENTS);
                            return tempArr; // css framework is not needed
                        }
                        else {
                            tempArr.add(QuizLogic.MATERIAL_UI);
                            tempArr.add(QuizLogic.BOOTSTRAP);
                            return tempArr; // css framework is needed
                        }
                    case 3: // UI component reuse
                        if(questionAnswerPosition == 1) {
                            tempArr.add(QuizLogic.ANGULAR);
                            tempArr.add(QuizLogic.REACT_JS);
                            return tempArr; // JS framework is required
                        }
                        else {
                            tempArr.add(QuizLogic.ASP_DOTNET);
                            tempArr.add(QuizLogic.DJANGO);
                            return tempArr; // structure is most important aspect
                        }
                    default:
                        throw new IllegalArgumentException("something is wrong dude");
                }
            case QuizLogic.BACK_END_SECTION:
                switch (questionNumber) {
                    case 1: // RT connection criteria
                        if(questionAnswerPosition == 1){
                            tempArr.add(QuizLogic.ASP_DOTNET_WEB_API);
                            return tempArr; // not building RT services
                        }
                        else {
                            tempArr.add(QuizLogic.NODE_JS);
                            tempArr.add(QuizLogic.EXPRESS_JS);
                            return tempArr; // RT services required
                        }
                    case 2: // high network traffic requirements
                        if(questionAnswerPosition == 1){
                            tempArr.add(QuizLogic.NODE_JS);
                            tempArr.add(QuizLogic.EXPRESS_JS);
                            return tempArr; // dealing with high traffic requirements
                        }
                        else {
                            tempArr.add(QuizLogic.ASP_DOTNET_WEB_API);
                            return tempArr; // not dealing with high traffic
                        }
                    case 3: // platform maturity requirements
                        if(questionAnswerPosition == 1){
                            tempArr.add(QuizLogic.ASP_DOTNET_WEB_API);
                            return tempArr; // platform maturity is required
                        }
                        else {
                            tempArr.add(QuizLogic.NODE_JS);
                            tempArr.add(QuizLogic.EXPRESS_JS);
                            return tempArr; // platform maturity is NOT required
                        }
                    case 4: // documentation requirements
                        if(questionAnswerPosition == 1){
                            tempArr.add(QuizLogic.NODE_JS);
                            tempArr.add(QuizLogic.EXPRESS_JS);
                            return tempArr; // platform maturity is required
                        }
                        else {
                            tempArr.add(QuizLogic.ASP_DOTNET_WEB_API);
                            return tempArr; // platform maturity is NOT required
                        }
                    default:
                        throw new IllegalArgumentException("something is wrong dude");
                }
            case QuizLogic.DB_SECTION:
                switch (questionNumber) {
                    case 1: // strong schema structure required
                        if(questionAnswerPosition == 1){
                            tempArr.add(QuizLogic.MYSQL);
                            tempArr.add(QuizLogic.POSTGRESQL);
                            return tempArr; // SQL needed
                        }
                        else {
                            tempArr.add(QuizLogic.MONGODB);
                            return tempArr; // NoSQL needed
                        }
                    case 2: // ORM tools required
                        if(questionAnswerPosition == 1){
                            tempArr.add(QuizLogic.MYSQL);
                            tempArr.add(QuizLogic.POSTGRESQL);
                            return tempArr; // mature and well-tested ORM required
                        }
                        else {
                            tempArr.add(QuizLogic.MONGODB);
                            return tempArr; // newer ORM acceptable
                        }
                    case 3: // DB administration
                        if(questionAnswerPosition == 1){
                            tempArr.add(QuizLogic.MONGODB);
                            return tempArr; // willing to deploy and self-administer the DB
                        }
                        else {
                            tempArr.add(QuizLogic.MYSQL);
                            tempArr.add(QuizLogic.POSTGRESQL);
                            return tempArr; // access to data center AND/OR a professional DB admin
                        }
                    case 4: // Schema changes
                        if(questionAnswerPosition == 1){
                            tempArr.add(QuizLogic.MONGODB);
                            return tempArr; // wishes to change db later
                        }
                        else {
                            tempArr.add(QuizLogic.MYSQL);
                            tempArr.add(QuizLogic.POSTGRESQL);
                            return tempArr; // db will not change
                        }
                }
        }
        return tempArr;
    }
}