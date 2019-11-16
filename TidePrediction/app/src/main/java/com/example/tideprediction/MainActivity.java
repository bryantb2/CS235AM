package com.example.tideprediction;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends ListActivity {

    // UI ELEMENTS
    private int dayAbbrTextView;
    private int dateTimeTextView;
    private int tideStatusTextView;
    private int tideTimeTextView;

    // STRING CONSTANTS
    public static final String ABBRV_DAY = "ABBRV_DAY";
    public static final String Date_Time = "Date_Time";
    public static final String TIDE_STATUS = "TIDE_STATUS";
    public static final String TIDE_TIME = "TIDE_TIME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set the layout viewa
        setContentView(R.layout.activity_main);

        // Get references to UI objects
        dayAbbrTextView = R.id.dayAbbrv;
        dateTimeTextView = R.id.dateTime;
        tideStatusTextView = R.id.tideStatus;
        tideTimeTextView = R.id.tideTime;

        // Creates an Arraylist of HashMap objects that stores key/value pairs for TideItem data
        ArrayList<HashMap<String, String>> hashMap = generateListItemHashMap();


    }

    private ArrayList<HashMap<String, String>> generateListItemHashMap() {
        // Create a parser object from Brian's Dal java file
        Dal xmlParser = new Dal(this);
        // Create a TideItem object to temporarily store results from parser
        ArrayList<TideItem> tempTideItemList = xmlParser.parseXmlFile("Florence_2019_tide_predictions.xml");

        // Loop through TideItem ArrayList results and build a HashMap of the data
        ArrayList<HashMap<String, String>> tempHashMapList = new ArrayList<HashMap<String, String>>();
        for(int i = 0; i <tempTideItemList.size();i++) {
            // store the currently indexed tide item
            // create a Hashmap object to store key value pairs
            // put the tideItem data into the hashmap
            // add hashmap object to ArrayList
            TideItem tempTideItem = tempTideItemList.get(i);
            HashMap<String,String> map = new HashMap<String,String>();

            map.put(ABBRV_DAY,tempTideItem.getDate());
            map.put(Date_Time,tempTideItem.getDate());
            map.put(TIDE_STATUS,tempTideItem.getHighlow());
            map.put(TIDE_TIME,tempTideItem.getTime());
            tempHashMapList.add(map);
        }
        return tempHashMapList;
    }
}
