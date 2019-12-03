package com.example.termproject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class ResultsActivity extends ListActivity {

    // UI ELEMENTS
    private int stackCategoryTextView;
    private int techNameTextView;
    private int techFrameworkTextView;
    private int techTagTextView;

    // RECOMMENDATION TAG CONSTANTS
    public static final String STACK_CATEGORY = "STACK_CATEGORY";
    public static final String TECH_NAME = "TECH_NAME";
    public static final String TECH_FRAMEWORK = "TECH_FRAMEWORK";
    public static final String TECH_TAGS = "TECH_TAGS";

    // ListActivity adapter-compatible hash map
    ArrayList<HashMap<String, String>> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get references to UI objects
        stackCategoryTextView = R.id.stackCategoryText;
        techNameTextView = R.id.techNameText;
        techFrameworkTextView = R.id.typeOfTechText;
        techTagTextView = R.id.techTagText;

        // Capture intent sent from first activity
        Intent intent = getIntent();
        QuizLogic quiz = (QuizLogic)intent.getSerializableExtra(MainActivity.QUIZ_LOGIC); // get obj using serializable interface (cast to proper type)

        // Generate recommendation list from quiz logic
        ArrayList<QuizRecommendation> dirtyRecommendationList = quiz.GetFinalRecommendations();

        // Clean up list for UI friendliness
        ArrayList<QuizRecommendation> cleanRecommendationList = DataBeautifier.BeautifyAndReturnRecommendations(dirtyRecommendationList);

        // Creates an Arraylist of HashMap objects that stores key/value pairs for Recommendation data
        hashMap = generateListItemHashMap(cleanRecommendationList);

        // Build an adapter object (will transfer the Arraylist hashmap data to the ListView in the layout)
        // takes in context, data source, the target layout, and two parallel arrays (key/value pairs for the data and target UI elements)
        SimpleAdapter adapter = new SimpleAdapter(this,
                hashMap,
                R.layout.web_stack_results,
                new String[]{STACK_CATEGORY, TECH_NAME, TECH_FRAMEWORK, TECH_TAGS},
                new int[] {
                        stackCategoryTextView,
                        techNameTextView,
                        techFrameworkTextView,
                        techTagTextView
                });

        // these methods deal with the getting and setting of ListActivity properties and/or listdata
        setListAdapter(adapter);
    }

    private ArrayList<HashMap<String, String>> generateListItemHashMap(ArrayList<QuizRecommendation> recommendations) {
        // Loop through recommendation ArrayList results and build a HashMap of the data
        ArrayList<HashMap<String, String>> tempHashMapList = new ArrayList<HashMap<String, String>>();
        for(int i = 0; i <recommendations.size();i++) {
            // store the currently indexed tide item
            // create a Hashmap object to store key value pairs
            // put the tideItem data into the hashmap
            // add hashmap object to ArrayList
            QuizRecommendation tempRecommendationItem = recommendations.get(i);
            HashMap<String,String> map = new HashMap<String,String>();

            // special case: concatenate quiz tags into one line
            ArrayList<String> categoryTags = tempRecommendationItem.GetQuizTags();
            String concatenatedTags = "";
            for(String tag : categoryTags) {
                concatenatedTags += tag;
                concatenatedTags += ", ";
            }

            map.put(STACK_CATEGORY,tempRecommendationItem.GetStackCategory());
            map.put(TECH_NAME,tempRecommendationItem.GetTechnologyName());
            map.put(TECH_FRAMEWORK,tempRecommendationItem.GetTypeOfTech());
            map.put(TECH_TAGS,concatenatedTags);
            tempHashMapList.add(map);
        }
        return tempHashMapList;
    }

    //TODO: build a series of methods that get the recommendations from the quiz logic AND THEN display in a list on the results activity
}
