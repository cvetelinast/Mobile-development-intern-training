package com.example.tsvetelinastoyanova.drawingapp.drawing;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class ActionDialog {

    Dialog dialog;
    Button buttonOk;
    Button buttonCancel;

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
}
