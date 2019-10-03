package com.example.tipcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

    public void percentButtonEventHanlder(String operation) {
        //check and validate the operation
        if (operation == "increment" || operation == "decrement") {
            if(operation == "increment") {
                //increment tip
                tipPercent.incrementPercent();
            }
            else {
                //decrement tip
                tipPercent.decrementPercent();
            }

            //get subTotal from UI
            double subTotal = Double.parseDouble(this.subTotalEntry.getText().toString());
            //run calculation
            double tipTotal = this.calculateTipTotal(tipPercent.getPercent(),subTotal);
            //final bill total
            double billTotal = tipTotal + subTotal;

            //get FINAL values for output
            String finalTipPercent = String.valueOf(tipPercent.getPercent());
            String finalTipTotal = String.valueOf(tipTotal);
            String finalBillTotal = String.valueOf(billTotal);

            //DISPLAY ON UI
            tipPercentOutput.setText(finalTipPercent + "%");
            tipTotalOutput.setText(finalTipTotal);
            billTotalOutput.setText(finalBillTotal);
        }
        else {
            throw new java.lang.IllegalArgumentException("argument must be string and either 'increment' or 'decrement");
        }

    }

    private double calculateTipTotal(int tipPercent, double billSubTotal) {
        double tipAsDecimal = (tipPercent/100D);
        return (billSubTotal * tipAsDecimal);
    }
}

