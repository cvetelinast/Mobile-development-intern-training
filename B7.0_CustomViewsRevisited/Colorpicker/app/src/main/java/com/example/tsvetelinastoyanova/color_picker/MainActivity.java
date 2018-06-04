package com.example.tsvetelinastoyanova.color_picker;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.example.tsvetelinastoyanova.color_picker.colorpicker.ColorPickerDialog;

public class MainActivity extends AppCompatActivity {
    private Button buttonOpenDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        buttonOpenDialog = findViewById(R.id.buttonShowCustomDialog);
        setOnClickListenerWhenOpenDialog();
    }

    private void setOnClickListenerWhenOpenDialog() {
        buttonOpenDialog.setOnClickListener((v) -> {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog);
            setTitleToDialog(dialog);

            new ColorPickerDialog(dialog, getResources());
            Button buttonOkToCloseDialog = dialog.findViewById(R.id.dialogButtonOk);
            setOnClickListenerForClose(dialog, buttonOkToCloseDialog);
            dialog.show();
        });
    }

    private void setTitleToDialog(Dialog dialog) {
        TextView dialogTitle = dialog.findViewById(R.id.dialogTitle);
        dialogTitle.setText(getResources().getString(R.string.dialogTitle));
    }

    private void setOnClickListenerForClose(Dialog dialog, Button buttonOkToCloseDialog) {
        buttonOkToCloseDialog.setOnClickListener((v) -> {
            dialog.dismiss();
            colorButtonOpenDialog(dialog);
        });
    }

    private void colorButtonOpenDialog(Dialog dialog) {
        TextView chosenColor = dialog.findViewById(R.id.color);
        if (chosenColor.getBackground() instanceof ColorDrawable) {
            ColorDrawable cd = (ColorDrawable) chosenColor.getBackground();
            buttonOpenDialog.setBackgroundColor(cd.getColor());
        }
    }
}
