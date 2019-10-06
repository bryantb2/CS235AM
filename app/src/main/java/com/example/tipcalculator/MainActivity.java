package com.example.tipcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Build;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.text.Editable;
//UI elements
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
//OnPause and OnResume
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MainActivity extends AppCompatActivity {

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
    private Double subTotalData; //user-input converted to a numeric value
    private Double tipTotalData; //cost of the tip (USD)
    private Double billTotalData; //grand bill total (USD)

    //SAVE STATE KEYS
    private boolean isStateSaved = false;
    private String SUB_TOTAL_STRING= "SUB_TOTAL_STRING";
    private String TIP_PERCENT_STRING = "TIP_PERCENT_UI_STRING";
    private String TIP_TOTAL_STRING = "TIP_TOTAL_STRING";
    private String BILL_TOTAL_STRING = "BILL_TOTAL_STRING";

    //RESUME/PAUSE TEMP STATE KEYS
    private String SUB_TOTAL_TEMP_STRING= "SUB_TOTAL_STRING";
    private String TIP_PERCENT_TEMP_STRING = "TIP_PERCENT_UI_STRING";
    private String TIP_TOTAL_TEMP_STRING = "TIP_TOTAL_STRING";
    private String BILL_TOTAL_TEMP_STRING = "BILL_TOTAL_STRING";

    //TESTING TAGS AND VARS
    private static final String TRACER = "tracer";
    private String INSIDE_ONCREATE = "INSIDE_ONCREATE";
    private String GENERAL_TEST = "GENERAL_TEST";
    private String INSIDE_ONRESTORE = "INSIDE_ONRESTORE";
    private String INSIDE_ONSAVE = "INSIDE_ONSAVE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        notify("onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FETCHING UI ELEMENTS
        this.incrementorButton = findViewById(R.id.raisePercentButton);
        this.decrementorButton = findViewById(R.id.lowerPercentButton);
        this.subTotalEntryField = findViewById(R.id.subTotalTextInput);
        this.tipPercentLabel = findViewById(R.id.tipPercentOutputTag);
        this.tipTotalLabel = findViewById(R.id.tipTotalOutputTag);
        this.billTotalLabel = findViewById(R.id.billTotalOutputTag);

        Log.d(INSIDE_ONCREATE,"onCreate");
        //getting the state (prepping UI)
        if(savedInstanceState != null) {
            //RESTORING INSTANCE VARIABLES
            //getting saved strings and converting/setting them to the class data fields
            this.subTotalInputString = savedInstanceState.getString(TIP_PERCENT_STRING);
            this.tipPercentObject = new Percentage(Integer.parseInt(savedInstanceState.getString(TIP_PERCENT_STRING)));
            //set is stateSaved tracking to true
            isStateSaved = true;
        }

        // Get reference to SharedPrefs object
        savedValues = getSharedPreferences("savedValues", MODE_PRIVATE);

        //ASSIGNING EVENT HANDLERS
        assignEventListeners();
    }

    //LIFECYCLE METHODS

    @Override
    protected void onPause() {
        notify("onPause");
        super.onPause();
        //onPause will store the sub total, tip total, tip percent, and full bill amount
        Editor editor = savedValues.edit();
        editor.putString(this.SUB_TOTAL_TEMP_STRING, String.valueOf(this.subTotalData));
        editor.putString(this.TIP_PERCENT_TEMP_STRING, String.valueOf(this.tipPercentObject.getPercent()));
        editor.commit();
    }

    @Override
    protected void onResume() {
        //conditional selection between sharedPreferences (perma storage) and SAVESTATE (temp storage)
        if(this.isStateSaved == true) {
            //set text input field (if the subTotalInputString is a valid input)
            if(!(this.subTotalInputString == "" || this.subTotalInputString == " ")) {
                textEntryEventHandler(this.subTotalInputString);
            }
            else {
                //execution of this block is done when the saved subTotalInputString contians no numeric values (aka EMPTY)
                percentageUIUpdater(tipPercentObject.getPercent());
                resetUIOutputElements();
            }
        }
        else {
            notify("onResume temp storage access");
            if(!(savedValues.getString(SUB_TOTAL_TEMP_STRING, "") == "")) {
                //assumes that a tip percent return value of "" means that there is no values in perma storage
                //Getting instance variables
                this.subTotalInputString = savedValues.getString(SUB_TOTAL_TEMP_STRING, ""); //MAYBE THIS WILL BREAK STUFF
                this.tipPercentObject = new Percentage(Integer.parseInt(savedValues.getString(TIP_PERCENT_TEMP_STRING, "")));
                //Displaying percent and sub total in text input field
                percentageUIUpdater(this.tipPercentObject.getPercent());
                this.subTotalEntryField.setText(this.subTotalInputString);
                //Calculating data (because there is something in the input field from a previous instance)
                textEntryEventHandler(this.subTotalInputString);
            }
            else {
                this.tipPercentObject = new Percentage();
                this.percentageUIUpdater(this.tipPercentObject.getPercent());
                resetUIOutputElements();
            }
        }
        super.onResume();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //assumption is that onCreate has restored the instance variables required to rebuild the user data
        notify("onRestore");
        super.onRestoreInstanceState(savedInstanceState);
        if(this.subTotalInputString == "" || this.subTotalInputString == " ") {
            //Displaying percent and resetting UI
            percentageUIUpdater(this.tipPercentObject.getPercent());
            resetUIOutputElements();
        }
        else {
            //Displaying percent and sub total in text input field
            percentageUIUpdater(this.tipPercentObject.getPercent());
            this.subTotalEntryField.setText(this.subTotalInputString);
            //Calculate data (because there is something in the input field from a previous instance)
            textEntryEventHandler(this.subTotalInputString);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        notify("onSaveInstanceState");
        //saving important UI data to protect it from onDestroy()
        savedInstanceState.putString(SUB_TOTAL_STRING,this.subTotalInputString);
        savedInstanceState.putString(TIP_PERCENT_STRING,String.valueOf(this.tipPercentObject.getPercent()));
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(savedInstanceState);
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
        Button clickedButton = findViewById(viewID);
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
                int tipAmount = tipPercentObject.getPercent();
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
            notificationBuilder = new Notification.Builder(this, TRACER);
        } else {
            //noinspection deprecation
            notificationBuilder = new Notification.Builder(this);
        }

        Notification notification = notificationBuilder
                .setContentTitle(methodName + " " + strings[strings.length - 1])
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(name).build();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
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

    private double calculateTipTotal(int tipPercent, double billSubTotal) {
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

