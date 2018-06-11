package com.example.tsvetelinastoyanova.drawingapp.drawing.DialogBuilders;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

public class SaveDialog extends ActionDialog {

    public SaveDialog(Context context) {
        super(context);
    }

    @Override
    public SaveDialog initNameInput(int drawingInputDest, String nameOfDrawing) {
        if (!nameOfDrawing.isEmpty()) {
            TextInputLayout textInputLayout = dialog.findViewById(drawingInputDest);
            EditText editText = textInputLayout.getEditText();

            // remove ".jpg" from the name);
            int extensionIndex = nameOfDrawing.lastIndexOf(".");
            if (extensionIndex == -1) {
                editText.setText(nameOfDrawing);
            } else {
                editText.setText(nameOfDrawing.substring(0, extensionIndex));
            }
        }
        return this;
    }
}
