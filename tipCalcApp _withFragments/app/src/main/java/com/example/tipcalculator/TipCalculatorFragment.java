package com.example.tipcalculator;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;

public class TipCalculatorFragment extends Fragment {
    //Class-level sharedPreferences object
    private SharedPreferences savedValues;

    //FIELDS
    private Button incrementorButton;
    private Button decrementorButton;
    private EditText subTotalEntryField;
    private TextView tipPercentLabel;
    private TextView tipTotalLabel;
    private TextView billTotalLabel;

    //FIELD DATA
    private Percentage tipPercentObject;
    private String subTotalInputString; //converted char sequence of user input
    private double subTotalData; //user-input converted to a numeric value
    private double tipTotalData; //cost of the tip (USD)
    private double billTotalData; //grand bill total (USD)

    //RESUME/PAUSE TEMP STATE KEYS
    private String SUB_TOTAL = "SUB_TOTAL";
    private String TIP_PERCENT = "TIP_PERCENT";

    //SETTINGS VARIABLES
    private final int ROUND_NONE = 0;
    private final int ROUND_TIP = 1;
    private final int ROUND_TOTAL = 2;

    private SharedPreferences prefs;
    private boolean rememberTipPercent;
    private int rounding;

    private boolean isNoRounding = false;
    private boolean isRoundingTip = false;
    private boolean isRoundingTotal = false;

    //TESTING TAGS AND VARS
    private static final String TRACER = "tracer";
    private String INSIDE_ONCREATE = "INSIDE_ONCREATE";
    private String GENERAL_TEST = "GENERAL_TEST";
    private String INSIDE_ONRESTORE = "INSIDE_ONRESTORE";
    private String INSIDE_ONSAVE = "INSIDE_ONSAVE";


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //info on how to make onCreateOptions work with fragments is available at: https://stackoverflow.com/questions/15653737/oncreateoptionsmenu-inside-fragments

        //mounting the XML to the main activity
        //getting reference to settings fragment
        SettingsActivityFragment settingsFragment = (SettingsActivityFragment)
            this.getActivity().getFragmentManager().findFragmentById(R.id.settings_fragment);

        //get the menu
        inflater.inflate(R.menu.activity_menu,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            //determines which menu item was selected based off element IDs
            case R.id.about:
                Toast.makeText(this.getActivity(),"About", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.settings:
                Toast.makeText(this.getActivity(),"Settings", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this.getActivity().getApplicationContext(),SettingsActivity.class));
                return true;
            default:
                //allows for default OptionsItemSelected behavior from the super class
                return super.onOptionsItemSelected((item));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        notify("onCreate");
        super.onCreate(savedInstanceState);

        //getting default preference values
        PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, false);

        // Get reference to SharedPrefs object (having to specific context since fragments do not extend the context object)
        savedValues = this.getActivity().getSharedPreferences("savedValues", MODE_PRIVATE);

        //setting default values
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity()); //sets the preferences class object to an instance of sharedPreferences

        //enabling the options menu
        setHasOptionsMenu(true);
    }

    //LIFECYCLE METHODS
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //building layout for fragment
        View view = inflater.inflate(R.layout.tip_calc_fragment, container, false);

        //FETCHING UI ELEMENTS
        this.incrementorButton = view.findViewById(R.id.raisePercentButton);
        this.decrementorButton = view.findViewById(R.id.lowerPercentButton);
        this.subTotalEntryField = view.findViewById(R.id.subTotalTextInput);
        this.tipPercentLabel = view.findViewById(R.id.tipPercentOutputTag);
        this.tipTotalLabel = view.findViewById(R.id.tipTotalOutputTag);
        this.billTotalLabel = view.findViewById(R.id.billTotalOutputTag);

        //ASSIGNING EVENT HANDLERS
        assignEventListeners();

        //returning the View containing the XML layout
        return view;
    }

    @Override
    public void onPause() {
        notify("onPause");
        super.onPause();
        //onPause will store the sub total, tip total, tip percent, and full bill amount
        SharedPreferences.Editor editor = savedValues.edit();
        //doubles need to be saved as string and then parsed after
        editor.putString(this.SUB_TOTAL, String.valueOf(this.subTotalData));
        editor.putString(this.TIP_PERCENT, String.valueOf(this.tipPercentObject.getPercent()));
        editor.commit();
    }

    @Override
    public void onResume() {
        notify("onResume temp storage access");
        //GETTING VALUES FROM DEFAULT PREFERENCES
        //rounding = Integer.parseInt(prefs.getString("pref_rounding","0"));
        Log.d("TipCalc","logging rounding from prefs object: " + rounding);
        rememberTipPercent = prefs.getBoolean("pref_remember_percent", true);
        Log.d("TipCalc","logging rememberTipPercent from prefs object: " + rememberTipPercent);


        //SETTING CLASS BOOLS FOR ROUNDING BEHAVIOR
        if(rounding == ROUND_NONE) {
            //no rounding on any of the numbers
            this.isNoRounding = true;
        }
        else if(rounding == ROUND_TIP) {
            this.isRoundingTip = true;
        }
        else if(rounding == ROUND_TOTAL) {
            this.isRoundingTotal = true;
        }


        if(!(Double.parseDouble(savedValues.getString(SUB_TOTAL, "0")) == 0)) {
            //assumes that a tip percent return value of 0 means that there is no values in storage
            //testing to determine what value the tipPercent variable will hold
            if(rememberTipPercent == true) {
                this.tipTotalData = Double.parseDouble(savedValues.getString(TIP_PERCENT,"0"));
            }
            else {
                this.tipTotalData = 15;
            }

            //Getting instance variables
            this.subTotalData = Double.parseDouble(savedValues.getString(SUB_TOTAL, "0"));
            this.subTotalInputString = String.valueOf(this.subTotalData);
            int internalPercentage = (int)(tipTotalData);
            this.tipPercentObject = new Percentage(internalPercentage);

            //Displaying percent and sub total in text input field
            percentageUIUpdater(this.tipPercentObject.getPercent());
            //Calculating data (because there is something in the input field from a previous instance)
            this.subTotalEntryField.setText(this.subTotalInputString);
            textEntryEventHandler(this.subTotalInputString);
        }
        else {
            this.tipPercentObject = new Percentage();
            this.percentageUIUpdater(this.tipPercentObject.getPercent());
            resetUIOutputElements();
        }
        super.onResume();
    }

    //EVENT HANDLERS AND LISTENERS

    public void textEntryEventHandler(String inputFieldData) {
        ///get and set subTotal from UI
        this.subTotalData= Double.parseDouble(inputFieldData);
        //cal and set tip total
        this.tipTotalData = calculateTipTotal(this.tipPercentObject.getPercent(),this.subTotalData);
        //calc final bill total
        this.billTotalData = calculateFinalTotal(this.tipTotalData,this.subTotalData);
        updateUI(this.tipTotalData,this.billTotalData);
    }

    public void percentButtonEventHandler(View view) {
        //view conversion
        int viewID = view.getId();
        Button clickedButton = view.findViewById(viewID);
        //check and validate the operation;
        if (clickedButton == this.incrementorButton || clickedButton == this.decrementorButton) {
            //update tip internal object and UI label
            if(clickedButton == this.incrementorButton) {
                //increment tip
                tipPercentObject.incrementPercent();
                percentageUIUpdater(this.tipPercentObject.getPercent());
            }
            else {
                //decrement tip
                tipPercentObject.decrementPercent();
                percentageUIUpdater(this.tipPercentObject.getPercent());
            }
            //special case: this conditional block ensures that calculations only run when the subtotal has been entered
            String subTotalEntry = this.subTotalEntryField.getText().toString();
            if(expressionIsValid(subTotalEntry)==true) {
                //CALCULATING BILL AND TIP TOTAL
                //get subTotal from UI
                double subTotal = Double.parseDouble(this.subTotalEntryField.getText().toString());
                //get tip from internal object
                double tipAmount = tipPercentObject.getPercent();
                //cal tip total
                double finalTipTotal = calculateTipTotal(tipAmount,subTotal);
                //calc final bill total
                double finalBillTotal = calculateFinalTotal(finalTipTotal,subTotal);
                updateUI(finalTipTotal,finalBillTotal);
                percentageUIUpdater(this.tipPercentObject.getPercent());
            }
        }
        else {
            //throw if + or - text signs are not seen by the method
            throw new java.lang.RuntimeException("event handler connected to wrong UI elements");
        }
    }

    private void assignEventListeners() {
        this.subTotalEntryField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                subTotalInputString = s.toString();
                if((subTotalInputString.length() >=1)) {
                    //checks validity of characters
                    if (expressionIsValid(subTotalInputString) == true) {
                        textEntryEventHandler(s.toString());
                    }
                    else {
                        //erase the bad chars
                        subTotalEntryField.setText("");
                    }
                }
                else {
                    //checks to see if text is being deleted
                    if(subTotalInputString.length()==0 && before > subTotalInputString.length()) {
                        //reset UI
                        resetUIOutputElements();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    //METHODS

    private void notify(String methodName) {
        //IMPORTANT NOTE ----- credit for notify() source code goes to: https://www.vogella.com/tutorials/AndroidLifeCycle/article.html -----
        String name = this.getClass().getName();
        String[] strings = name.split("\\.");
        Notification.Builder notificationBuilder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder = new Notification.Builder(getActivity(), TRACER);
        } else {
            //noinspection deprecation
            notificationBuilder = new Notification.Builder(getActivity());
        }

        Notification notification = notificationBuilder
                .setContentTitle(methodName + " " + strings[strings.length - 1])
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(name).build();
        NotificationManager notificationManager =
                (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis(), notification);
    }

    private void updateUI(double tipTotalFinal, double BillTotalFinal) {
        //DISPLAY FINAL VALUES ON UI
        tipTotalLabel.setText(String.valueOf(tipTotalFinal) + "$");
        billTotalLabel.setText(String.valueOf(BillTotalFinal) + "$");
    }

    private void resetUIOutputElements() {
        //RESETS DOLLAR OUTPUT CALCULATIONS IN UI
        this.tipTotalLabel.setText("0.00$");
        this.billTotalLabel.setText("0.00$");
    }

    private void percentageUIUpdater(int percent) {
        //DISPLAY CHANGE IN UI
        tipPercentLabel.setText(String.valueOf(percent) + "%");
    }

    private double calculateTipTotal(double tipPercent, double billSubTotal) {
        double tipAsDecimal = (tipPercent/100D);
        double sum = (billSubTotal * tipAsDecimal);
        //will round to second decimal place
        return (Math.round(sum *100)/100.0);
    }

    private double calculateFinalTotal(double tipTotal, double billSubTotal) {
        double sum = (billSubTotal + tipTotal);
        return (Math.round(sum *100)/100.0);
    }

    private boolean expressionIsValid(String expression) {
        //method checks if a given expression contains any alphabetic characters
        //will loop through inputted expression and compare each character to those in the "acceptableValues" array
        char acceptableValues[]  = {'0','1','2','3','4','5','6','7','8','9','.'};
        for(int i = 0; i < expression.length(); i++) {
            char expressionCharacter = expression.charAt(i);
            for(int j = 0; j < acceptableValues.length; j++) {
                if (expressionCharacter == acceptableValues[j])
                    return true;
            }
        }
        return false;
    }
}
