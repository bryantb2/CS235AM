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

    //CLASS-LEVEL FIELDS
    Button incrementorButton;
    Button decrementorButton;
    EditText subTotalEntryField;
    TextView tipPercentLabel;
    TextView tipTotalLabel;
    TextView billTotalLabel;

    //CLASS-LEVEL FIELD DATA
    Percentage tipPercentObject;
    private String subTotalInputString; //converted char sequence of user input
    private Double subTotalData; //user-input converted to a numeric value
    private Double tipTotalData; //cost of the tip (USD)
    private Double billTotalData; //grand bill total (USD)

    //CLASS-LEVEL STATE KEYS
    private String SUB_TOTAL_STRING= "SUB_TOTAL_STRING";
    private String TIP_PERCENT_UI_STRING = "TIP_PERCENT_UI_STRING";
    private String TIP_TOTAL_STRING = "TIP_TOTAL_STRING";
    private String BILL_TOTAL_STRING = "BILL_TOTAL_STRING";

    //TESTING TAGS AND VARS
    public static final String TRACER = "tracer";
    String INSIDE_ONCREATE = "INSIDE_ONCREATE";
    String GENERAL_TEST = "GENERAL_TEST";
    String INSIDE_ONRESTORE = "INSIDE_ONRESTORE";
    String INSIDE_ONSAVE = "INSIDE_ONSAVE";

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

        Log.d(GENERAL_TEST,"test of logging system");
        //getting the state (prepping UI)
        /*if(savedInstanceState != null) {
            this.TIP_PERCENT_UI_STRING = savedInstanceState.getString(TIP_PERCENT_UI_STRING);
            this.TIP_TOTAL_STRING = savedInstanceState.getString(TIP_TOTAL_STRING);
            this.SUB_TOTAL_STRING = savedInstanceState.getString(SUB_TOTAL_STRING);
            this.BILL_TOTAL_STRING = savedInstanceState.getString(BILL_TOTAL_STRING);
            //use saveState data to create percent and assign new percentage
            this.tipPercentObject = new Percentage(Integer.parseInt(TIP_PERCENT_UI_STRING));
            percentageUIUpdater(tipPercentObject.getPercent());
            resetUIOutputElements();
        }
        else {*/
            //otherwise set default values
            this.tipPercentObject = new Percentage();
            percentageUIUpdater(tipPercentObject.getPercent());
            resetUIOutputElements();
        //}

        //ASSIGNING EVENT HANDLERS
        assignEventListeners();
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

    //LIFECYCLE METHODS

    @Override
    protected void onPause() {
        super.onPause();
        //onPause will store the sub total, tip total, tip percent, and full bill amount
        Editor editor = savedValues.edit();
        editor.putString(this.SUB_TOTAL_STRING, String.valueOf(this.subTotalData));
        editor.putString(this.TIP_PERCENT_UI_STRING, String.valueOf(this.tipPercentObject.getPercent()));
        editor.commit();
        notify("onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Get instance variables
        this.subTotalInputString = savedValues.getString(SUB_TOTAL_STRING, ""); //MAYBE THIS WILL BREAK STUFF
        this.tipPercentObject = new Percentage(Integer.parseInt(savedValues.getString(TIP_PERCENT_UI_STRING, "")));
        //Display the sub total and percent text
        this.subTotalEntryField.setText(this.subTotalInputString);
        percentageUIUpdater(this.tipPercentObject.getPercent());
        //Run data calculation
        textEntryEventHandler(this.subTotalInputString);
    }

    /*@Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        notify("onRestore");
        super.onRestoreInstanceState(savedInstanceState);
        this.tipPercentObject = new Percentage(Integer.parseInt(TIP_PERCENT_UI_STRING));
        percentageUIUpdater(tipPercentObject.getPercent());
        this.subTotalEntryField.setText(savedInstanceState.getString(SUB_TOTAL_STRING));
        this.tipPercentLabel.setText(savedInstanceState.getString(TIP_PERCENT_UI_STRING));
        this.tipTotalLabel.setText(savedInstanceState.getString(TIP_TOTAL_STRING));
        this.billTotalLabel.setText(savedInstanceState.getString(BILL_TOTAL_STRING));
        Log.d(INSIDE_ONRESTORE,"test of onRestoreInstance");
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        notify("onSaveInstanceState");
        //outState.putString(GAME_STATE_KEY, gameState);
        //outState.putString(TEXT_VIEW_KEY, textView.getText());
        savedInstanceState.putString(SUB_TOTAL_STRING,this.subTotalEntryField.getText().toString());
        savedInstanceState.putString(TIP_PERCENT_UI_STRING,this.tipPercentLabel.getText().toString());
        savedInstanceState.putString(TIP_TOTAL_STRING,String.valueOf(this.tipPercentObject.getPercent()));
        savedInstanceState.putString(BILL_TOTAL_STRING,this.billTotalLabel.getText().toString());
        Log.d(INSIDE_ONSAVE,"test of onSavInstance");
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(savedInstanceState);
    }*/
}

