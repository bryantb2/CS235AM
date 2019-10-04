package com.example.tipcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tipcalculator.Percentage;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    //class fields
    Button incrementorButton;
    Button decrementorButton;
    EditText subTotalEntry;
    TextView tipPercentOutput;
    TextView tipTotalOutput;
    TextView billTotalOutput;
    Percentage tipPercent = new Percentage();

    //CLASS-LEVEL STATE KEYS
    String tempState;
    private String SUB_TOTAL_ENTRY = "SUB_TOTAL_ENTRY";
    private String TIP_PERCENT_UI_OUTPUT = "TIP_PERCENT_UI_OUTPUT";
    private String TIP_TOTAL_OUTPUT = "TIP_TOTAL_OUTPUT";
    private String BILL_TOTAL_OUTPUT = "BILL_TOTAL_OUTPUT";

    String INSIDE_ONCREATE = "INSIDE_ONCREATE";
    String GENERAL_TEST = "GENERAL_TEST";
    String INSIDE_ONRESTORE = "INSIDE_ONRESTORE";
    String INSIDE_ONSAVE = "INSIDE_ONSAVE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FETCHING UI ELEMENTS
        this.incrementorButton = findViewById(R.id.raisePercentButton);
        this.decrementorButton = findViewById(R.id.lowerPercentButton);
        this.subTotalEntry = findViewById(R.id.subTotalTextInput);
        this.tipPercentOutput = findViewById(R.id.tipPercentOutputTag);
        this.tipTotalOutput = findViewById(R.id.tipTotalOutputTag);
        this.billTotalOutput = findViewById(R.id.billTotalOutputTag);

        Log.d(GENERAL_TEST,"test of logging system");
        //getting the state (prepping UI)
        if(savedInstanceState != null) {
            //get existing saved values from state
            /*this.tipPercentOutput.setText(savedInstanceState.getString(TIP_PERCENT_UI_OUTPUT));
            Log.d(INSIDE_ONCREATE,"test of onCreate utilizing savedInstanceState");*/
            this.TIP_PERCENT_UI_OUTPUT = savedInstanceState.getString(TIP_PERCENT_UI_OUTPUT);
            this.TIP_TOTAL_OUTPUT = savedInstanceState.getString(TIP_TOTAL_OUTPUT);
            this.SUB_TOTAL_ENTRY = savedInstanceState.getString(SUB_TOTAL_ENTRY);
            this.BILL_TOTAL_OUTPUT = savedInstanceState.getString(BILL_TOTAL_OUTPUT);
        }
        else {
            //otherwise set default values
           /* this.tipPercentOutput.setText(String.valueOf(tipPercent.getPercent())+ "%");
            Log.d(INSIDE_ONCREATE,"test of onCreate utilizing savedInstanceState, NOT WORKING");*/
            percentageUIUpdater(this.tipPercent.getPercent());
            resetUIOutputElements();
        }

        //ASSIGNING EVENT HANDLERS
        assignEventListeners();
    }
/*
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.getString(TIP_PERCENT_UI_OUTPUT) != null) {
            this.subTotalEntry.setText(savedInstanceState.getString(SUB_TOTAL_ENTRY));
            this.tipPercentOutput.setText(savedInstanceState.getString(TIP_PERCENT_UI_OUTPUT));
            this.tipTotalOutput.setText(savedInstanceState.getString(TIP_TOTAL_OUTPUT));
            this.billTotalOutput.setText(savedInstanceState.getString(BILL_TOTAL_OUTPUT));
            Log.d(INSIDE_ONRESTORE,"test of onRestoreInstance");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
        //outState.putString(GAME_STATE_KEY, gameState);
        //outState.putString(TEXT_VIEW_KEY, textView.getText());
        outState.putString(SUB_TOTAL_ENTRY,this.subTotalEntry.getText().toString());
        outState.putString(TIP_PERCENT_UI_OUTPUT,this.tipPercentOutput.getText().toString());
        outState.putString(TIP_TOTAL_OUTPUT,String.valueOf(this.tipPercent.getPercent()));
        outState.putString(BILL_TOTAL_OUTPUT,this.billTotalOutput.getText().toString());
        Log.d(INSIDE_ONSAVE,"test of onSavInstance");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

    }*/


    public void textEntryEventHandler(CharSequence inputFieldData) {
        ///get subTotal from UI (converting Charsequence to string and then to double)
        double subTotal = Double.parseDouble(inputFieldData.toString());
        //get tip from internal object
        int tipAmount = tipPercent.getPercent();
        //cal tip total
        double finalTipTotal = calculateTipTotal(tipAmount,subTotal);
        //calc final bill total
        double finalBillTotal = calculateFinalTotal(finalTipTotal,subTotal);
        updateUI(finalTipTotal,finalBillTotal);
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
                tipPercent.incrementPercent();
                percentageUIUpdater(this.tipPercent.getPercent());
            }
            else {
                //decrement tip
                tipPercent.decrementPercent();
                percentageUIUpdater(this.tipPercent.getPercent());
            }
            //special case: this conditional block ensures that calculations only run when the subtotal has been entered
            String subTotalEntry = this.subTotalEntry.getText().toString();
            if(expressionIsValid(subTotalEntry)==true) {
                //get subTotal from UI
                double subTotal = Double.parseDouble(this.subTotalEntry.getText().toString());
                //get tip from internal object
                int tipAmount = tipPercent.getPercent();
                //cal tip total
                double finalTipTotal = calculateTipTotal(tipAmount,subTotal);
                //calc final bill total
                double finalBillTotal = calculateFinalTotal(finalTipTotal,subTotal);
                updateUI(finalTipTotal,finalBillTotal);
                percentageUIUpdater(this.tipPercent.getPercent());
            }
        }
        else {
            //throw if + or - text signs are not seen by the method
            throw new java.lang.RuntimeException("event handler connected to wrong UI elements");
        }
    }

    private void updateUI(double tipTotalFinal, double BillTotalFinal) {
        //DISPLAY FINAL VALUES ON UI
        tipTotalOutput.setText(String.valueOf(tipTotalFinal) + "$");
        billTotalOutput.setText(String.valueOf(BillTotalFinal) + "$");
    }

    private void resetUIOutputElements() {
        //RESETS DOLLAR OUTPUT CALCULATIONS IN UI
        this.tipTotalOutput.setText("0.00$");
        this.billTotalOutput.setText("0.00$");
    }

    private void percentageUIUpdater(int percent) {
        //DISPLAY CHANGE IN UI
        tipPercentOutput.setText(String.valueOf(percent) + "%");
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

    private void assignEventListeners() {
       this.subTotalEntry.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               String inputFieldData = s.toString();
                 if((inputFieldData.length() >=1)) {
                   //checks validity of characters
                   if (expressionIsValid(inputFieldData) == true) {
                       textEntryEventHandler(s);
                   }
                   else {
                       //erase the bad chars
                       subTotalEntry.setText("");
                   }
               }
                else {
                    //checks to see if text is being deleted
                     if(inputFieldData.length()==0 && before > inputFieldData.length()) {
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
}

