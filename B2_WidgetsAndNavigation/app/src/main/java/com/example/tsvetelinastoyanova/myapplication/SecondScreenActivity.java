package com.example.tsvetelinastoyanova.myapplication;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

public class SecondScreenActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_screen);
        spinnerInit();
    }

    // callback - even if rotate
    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        //Log.d("change", "On item selected called");
        parent.getItemAtPosition(pos);
        changeTextInTextView();
    }

    private void changeTextInTextView() {
        //Log.d("change", "Change text in view called");
        Spinner mySpinner = (Spinner) findViewById(R.id.planets_spinner);
        String text = mySpinner.getSelectedItem().toString();
        TextView enteredText = (TextView) findViewById(R.id.entered_planet);
        enteredText.setText(text);
        checkIfAnyRadioBtnIsClicked();
    }

    // What to do with this case?
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
        Log.d("choise", "Do nothing");
    }

    private void spinnerInit() {
        Spinner spinner = (Spinner) findViewById(R.id.planets_spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void changeTextFontStyle(int t) {
        TextView tv = (TextView) findViewById(R.id.entered_planet);
        tv.setTypeface(null, t);
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.radio_bold:
                if (checked)
                    changeTextFontStyle(Typeface.BOLD);
                break;
            case R.id.radio_italic:
                if (checked)
                    changeTextFontStyle(Typeface.ITALIC);
                break;
            case R.id.radio_normal:
                if (checked)
                    changeTextFontStyle(Typeface.NORMAL);
                break;
        }
    }
    private void checkIfAnyRadioBtnIsClicked() {
        onRadioButtonClicked((RadioButton) findViewById(R.id.radio_bold));
        onRadioButtonClicked((RadioButton) findViewById(R.id.radio_italic));
        onRadioButtonClicked((RadioButton) findViewById(R.id.radio_normal));
    }
}
