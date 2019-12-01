package com.example.termproject;

public class AnswerConverter {

    public static String GetAnswerAsCategory(String questionCategory, int questionNumber, int questionAnswerPosition) {
        // This class will take in a question number, category, and adapter integer position.
        // Each question's answer corresponds to one of the category tags in the main activity
        switch(questionCategory) {
            case QuizLogic.GENERAL_SECTION:
                switch (questionNumber) {
                    case 1: // overall project size
                        if(questionAnswerPosition == 1)
                            return QuizLogic.FAST_DEV_TIME; // startups care more about quick deployment
                        else
                            return QuizLogic.MAINTAINABILITY; // big companies care more about clean code
                        break;
                    case 2: // development time
                        if(questionAnswerPosition == 1)
                            return QuizLogic.FAST_DEV_TIME; // shorter timeframes require quicker dev tech
                        else
                            return QuizLogic.NO_SPECIFIC_CATEGORY; // longer projects do not need fast dev time
                        break;
                    case 3: // project scalability
                        if(questionAnswerPosition == 1)
                            return QuizLogic.EFFICIENCY_AND_SPEED; // shorter timeframes require quicker dev tech
                        else
                            return QuizLogic.NO_SPECIFIC_CATEGORY; // no category association because user does not need scalable project
                        break;
                    case 4: // programming language preference
                        if(questionAnswerPosition == 1)
                            return QuizLogic.EASE_OF_USE; // JS is ease to use and learn, hard to master
                        else if(questionAnswerPosition == 2)
                            return QuizLogic.STRUCTURE; // Java and C# are a bit harder to learn, but have the benefit of being more structured
                        break;
                }
                break;
            case QuizLogic.FRONT_END_SECTION:
                switch (questionNumber) {

                }
                break;
            case QuizLogic.BACK_END_SECTION:
                switch (questionNumber) {

                }
                break;
            case QuizLogic.DB_SECTION:
                switch (questionNumber) {

                }
                break;
            default:
                throw new IllegalArgumentException("something is wrong dude");
        }
    }
}
