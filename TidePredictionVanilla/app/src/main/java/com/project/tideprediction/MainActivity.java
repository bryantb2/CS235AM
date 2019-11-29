package com.project.tideprediction;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends ListActivity implements AdapterView.OnItemClickListener{

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
    public static final String TIDE_HEIGHT_CM = "TIDE_HEIGHT_CM";

    ArrayList<HashMap<String, String>> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get references to UI objects
        dayAbbrTextView = R.id.dayAbbrv;
        dateTimeTextView = R.id.dateTime;
        tideStatusTextView = R.id.tideStatus;
        tideTimeTextView = R.id.tideTime;

        // Creates an Arraylist of HashMap objects that stores key/value pairs for TideItem data
        hashMap = generateListItemHashMap();

        // Build an adapter object (will transfer the Arraylist hashmap data to the ListView in the layout)
            // takes in context, data source, the target layout, and two parallel arrays (key/value pairs for the data and target UI elements)
        SimpleAdapter adapter = new SimpleAdapter(this,
                hashMap,
                R.layout.list_layout,
                new String[]{ABBRV_DAY, Date_Time, TIDE_STATUS, TIDE_TIME},
                new int[] {
                        dayAbbrTextView,
                        dateTimeTextView,
                        tideStatusTextView,
                        tideTimeTextView
                });

        // these methods deal with the getting and setting of ListActivity properties and/or listdata
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
        getListView().setFastScrollEnabled(true);
    }

    private ArrayList<HashMap<String, String>> generateListItemHashMap() {
        // Create a parser object from Brian's Dal java file
        Dal xmlParser = new Dal(this);
        // Create a TideItem object to temporarily store results from parser
        ArrayList<TideItem> tempTideItemList = xmlParser.parseXmlFile("tide_predictions.xml");

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
            map.put(TIDE_HEIGHT_CM,tempTideItem.getPredInCm());
            tempHashMapList.add(map);
        }
        return tempHashMapList;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // get length in cm
        int heightInCM = Integer.parseInt(hashMap.get(i).get(TIDE_HEIGHT_CM));
        // convert to inches
        int heightInInches = (int)(heightInCM/2.54);
        Toast.makeText(this, "Tide will have a peak height of " + heightInInches + "in", Toast.LENGTH_SHORT).show();
    }
}
