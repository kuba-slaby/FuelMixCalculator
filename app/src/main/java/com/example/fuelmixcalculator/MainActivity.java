package com.example.fuelmixcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    EditText petrolAmount;
    Spinner fuelMix;
    EditText oilAmount;
    double petrol, oil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        petrolAmount = findViewById(R.id.petrolAmount);
        fuelMix = findViewById(R.id.fuelMix);
        oilAmount = findViewById(R.id.oilAmount);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ratios, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fuelMix.setAdapter(adapter);
        fuelMix.setOnItemSelectedListener(this);

        // petrol = (oil * ratio) / 1000
        // oil = (petrol * 1000) / ratio

        petrolAmount.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (petrolAmount.hasFocus()) {
                    if (TextUtils.isEmpty(petrolAmount.getText())) {
                        return;
                    }
                    petrol = Double.parseDouble(petrolAmount.getText().toString());
                    oil = fuelMixCount(petrol, Double.parseDouble(fuelMix.getSelectedItem().toString().replace("1:", "")), true);
                    oilAmount.setText(String.format(Locale.US,"%.2f", oil));
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });

        oilAmount.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (oilAmount.hasFocus()) {
                    if (TextUtils.isEmpty(oilAmount.getText())) {
                        return;
                    }
                    oil = Double.parseDouble(oilAmount.getText().toString());
                    petrol = fuelMixCount(oil, Double.parseDouble(fuelMix.getSelectedItem().toString().replace("1:", "")), false);
                    petrolAmount.setText(String.format(Locale.US,"%.2f", petrol));
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });
    }

    private double fuelMixCount(double input, double ratio, boolean choice){
        //input is petrol
        if (choice){
            return (input * 1000) / ratio;
        }
        //input is oil
        else{
            return (input * ratio) / 1000;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(petrolAmount.hasFocus()){
            oil = fuelMixCount(petrol, Double.parseDouble(fuelMix.getSelectedItem().toString().replace("1:", "")),true);
            oilAmount.setText(String.format(Locale.US,"%.2f", oil));
        }
        else if(oilAmount.hasFocus()){
            petrol = fuelMixCount(oil, Double.parseDouble(fuelMix.getSelectedItem().toString().replace("1:", "")),false);
            petrolAmount.setText(String.format(Locale.US,"%.2f", petrol));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }
}