package com.example.tsvetelinastoyanova.color_picker;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.example.tsvetelinastoyanova.color_picker.colorpicker.ColorPickerDialog;

public class MainActivity extends AppCompatActivity {
    final Context context = this;
    private Button buttonOpenDialog;
    private Button buttonOkToCloseDialog;
    private Dialog dialog;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonOpenDialog = findViewById(R.id.buttonShowCustomDialog);
        setOnClickListenerWhenOpenDialog();
    }

    private void setOnClickListenerWhenOpenDialog() {
        buttonOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog);

                TextView headingDialog = dialog.findViewById(R.id.headingDialog);
                headingDialog.setText(getResources().getString(R.string.headingDialog));

                buttonOkToCloseDialog = dialog.findViewById(R.id.dialogButtonOk);
                /*ColorPickerDialog colorPickerDialog = */
                new ColorPickerDialog(dialog);
                setOnClickListenerForClose();
                dialog.show();
            }
        });
    }

    private void setOnClickListenerForClose() {
        buttonOkToCloseDialog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                colorButtonOpenDialog();
            }
        });
    }

    private void colorButtonOpenDialog() {
        TextView chosenColor = dialog.findViewById(R.id.color);
        if (chosenColor.getBackground() instanceof ColorDrawable) {
            ColorDrawable cd = (ColorDrawable) chosenColor.getBackground();
            buttonOpenDialog.setBackgroundColor(cd.getColor());
        }
    }
}
