package com.example.tsvetelinastoyanova.drawingapp.drawing.DialogBuilders;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tsvetelinastoyanova.drawingapp.enums.ActionDrawing;

public class ActionDialog {

    public Dialog dialog;
    public Button buttonOk;
    public Button buttonCancel;

    public Dialog getDialog() {
        return dialog;
    }

    public Button getButtonOk() {
        return buttonOk;
    }

    public Button getButtonCancel() {
        return buttonCancel;
    }

    public ActionDialog(Context context) {
        dialog = new Dialog(context);
    }

    public ActionDialog setContentView(int view) {
        dialog.setContentView(view);
        return this;
    }

    public ActionDialog hideTextInputField(int idDestination, boolean shouldHideField) {
        if (shouldHideField) {
            TextInputLayout textInputLayout = dialog.findViewById(idDestination);
            textInputLayout.setVisibility(View.GONE);
        }
        return this;
    }

    public ActionDialog setTitleToDialog(int idDestination, String title) {
        TextView destination = dialog.findViewById(idDestination);
        destination.setText(title);
        return this;
    }

    public ActionDialog initButtonOk(int buttonId) {
        buttonOk = dialog.findViewById(buttonId);
        return this;
    }

    public ActionDialog initButtonCancel(int buttonId) {
        buttonCancel = dialog.findViewById(buttonId);
        return this;
    }

    public void showDialog() {
        dialog.show();
    }

    public ActionDialog setPreviousSizeOfEraser(int seekbarId, int sizeEraser) {
        return this;
    }

    public ActionDialog initNameInput(int drawingInputDest, String nameOfDrawing) {
        return this;
    }

    public ActionDialog createInstance() {
        return this;
    }
}
