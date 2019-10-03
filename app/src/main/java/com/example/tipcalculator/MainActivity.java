package com.example.tipcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tipcalculator.Percentage;

public class MainActivity extends AppCompatActivity {

    //class fields
    Button incrementorButton;
    Button decrementorButton;
    EditText subTotalEntry;
    TextView tipPercentOutput;
    TextView tipTotalOutput;
    TextView billTotalOutput;

    Percentage tipPercent = new Percentage();

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
    }

    public void textEntryEventHandler(View view) {

    }

    public void percentButtonEventHandler(View view) {
        //view conversion
        int viewID = view.getId();
        Button clickedButton = findViewById(viewID);
        //check and validate the operation
        if (clickedButton.getText() == "+" || clickedButton.getText() == "-") {
            //update tip internal object and UI label
            if(clickedButton.getText() == "+") {
                //increment tip
                tipPercent.incrementPercent();
                percentageUIUpdater("increment");
            }
            else {
                //decrement tip
                tipPercent.decrementPercent();
                percentageUIUpdater("decrement");
            }
            //this ensures that calculations only run when the subtotal has been entered
            if(this.subTotalEntry.getText() != null && this.subTotalEntry.getText().toString() != "") {
                //get subTotal from UI
                double subTotal = Double.parseDouble(this.subTotalEntry.getText().toString());
                //get tip from internal object
                int tipAmount = tipPercent.getPercent();
                //cal tip total
                double finalTipTotal = calculateTipTotal(tipAmount,subTotal);
                //calc final bill total
                double finalBillTotal = calculateFinalTotal(finalTipTotal,subTotal);
                updateUI(tipAmount,finalTipTotal,finalBillTotal);
            }
        }
        else {
            //throw if + or - text signs are not seen by the method
            throw new java.lang.RuntimeException("event handler connected to wrong UI elements");
        }
    }

    private void updateUI(int tipPercentFinal, double tipTotalFinal, double BillTotalFinal) {
        //DISPLAY ON UI
        tipPercentOutput.setText(String.valueOf(tipPercentFinal) + "%");
        tipTotalOutput.setText(String.valueOf(tipTotalFinal));
        billTotalOutput.setText(String.valueOf(BillTotalFinal));
    }

    private void percentageUIUpdater(String incrementorBehavior) {
        //parameter validation
        if(incrementorBehavior == "increment" || incrementorBehavior == "decrement") {
            //get raw percentage string
            String percentageString = (tipPercentOutput.getText()).toString();
            //drop the last character since it isn't a number
            percentageString = percentageString.substring(2,2);
            //convert string to int
            int convertedPercentage = Integer.parseInt(percentageString);

            //determine percentage math
            if(incrementorBehavior == "increment") {
                convertedPercentage = convertedPercentage++;
            }
            else {
                convertedPercentage = convertedPercentage--;
            }

            //DISPLAY CHANGE IN UI
            tipPercentOutput.setText(String.valueOf(convertedPercentage) + "%");
        }
        else {
            throw new java.lang.IllegalArgumentException("improper behavior parameter was passed; must be either 'increment' or 'decrement");
        }
    }

    private double calculateTipTotal(int tipPercent, double billSubTotal) {
        double tipAsDecimal = (tipPercent/100D);
        return (billSubTotal * tipAsDecimal);
    }

    private double calculateFinalTotal(double tipTotal, double billSubTotal) {
        return (billSubTotal + tipTotal);
    }
}

