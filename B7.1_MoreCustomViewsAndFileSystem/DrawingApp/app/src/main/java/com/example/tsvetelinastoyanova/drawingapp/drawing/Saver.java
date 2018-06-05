package com.example.tsvetelinastoyanova.drawingapp.drawing;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

public class Saver extends ActionDialog {

    public Saver(Context context) {
        super(context);
    }

    @Override
    public Saver initNameInput(int drawingInputDest, String nameOfDrawing) {
        if (!nameOfDrawing.isEmpty()) {
            TextInputLayout textInputLayout = dialog.findViewById(drawingInputDest);
            EditText editText = textInputLayout.getEditText();
            editText.setText(nameOfDrawing);
        }
        return this;
    }
}
