package com.example.tsvetelinastoyanova.drawingapp.drawing.DialogBuilders;

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
            editText.setText(nameOfDrawing.substring(0, nameOfDrawing.length()-4)); // remove ".jpg" from the name);
        }
        return this;
    }
}
