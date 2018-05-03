package com.example.tsvetelinastoyanova.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FirstScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);
    }

    // Should something be done to delete variables when back btn is clicked
    @Override
    public void onBackPressed() {
        String editTextText = getTextFromEditTextField().toString();
        if (!editTextText.equals("")) {
            EditText editTextField = (EditText) findViewById(R.id.edit_text_field);
            editTextField.setText("");
        } else {
            super.onBackPressed();
        }

        Log.d("back", "back");
    }

    private CharSequence getTextFromEditTextField() {
        EditText editTextField = (EditText) findViewById(R.id.edit_text_field);
        return editTextField.getText();
    }

    public void openToast(View view) {
        CharSequence text = getTextFromEditTextField();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
    }

    public void openSnackbar(View view) {
        CharSequence text = getTextFromEditTextField();
        final Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
        snackbar.setAction("Close", ignoredView -> snackbar.dismiss());
        snackbar.show();
    }

    public void openDialog(View view) {
        CharSequence text = getTextFromEditTextField();

        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("Dialog")
                .setMessage(text)
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> FirstScreenActivity.this.finish())
                .setNegativeButton("No", (dialog, id) -> dialog.cancel())
                .create();
        alertDialog.show();
    }

}
