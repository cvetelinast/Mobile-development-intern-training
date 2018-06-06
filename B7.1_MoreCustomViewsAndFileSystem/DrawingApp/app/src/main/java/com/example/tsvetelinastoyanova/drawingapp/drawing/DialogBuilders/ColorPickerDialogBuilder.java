package com.example.tsvetelinastoyanova.drawingapp.drawing.DialogBuilders;

import android.content.Context;
import android.content.res.Resources;

import com.example.tsvetelinastoyanova.drawingapp.drawing.colorpicker.colorpicker.ColorPickerDialog;

public class ColorPickerDialogBuilder extends ActionDialog {

    Resources resources;

    public ColorPickerDialogBuilder(Context context) {
        super(context);
        resources = context.getResources();
    }
@Override
    public ColorPickerDialogBuilder createInstance(){
        new ColorPickerDialog(dialog, resources);
        return this;
    }

}
