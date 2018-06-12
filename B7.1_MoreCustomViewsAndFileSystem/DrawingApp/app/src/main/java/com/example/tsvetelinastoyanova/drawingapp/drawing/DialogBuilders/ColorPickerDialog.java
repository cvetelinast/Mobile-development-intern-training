package com.example.tsvetelinastoyanova.drawingapp.drawing.DialogBuilders;

import android.content.Context;
import android.content.res.Resources;
import android.widget.SeekBar;

import com.example.tsvetelinastoyanova.drawingapp.drawing.colorpicker.colorpicker.ColorPicker;

public class ColorPickerDialog extends ActionDialog {

    private Resources resources;

    public ColorPickerDialog(Context context) {
        super(context);
        resources = context.getResources();
    }

    @Override
    public ColorPickerDialog initializeView() {
        new ColorPicker(dialog, resources);
        return this;
    }

    @Override
    public ColorPickerDialog setPreviousSizeOfTool(int seekbarId, int sizeEraser) {
        SeekBar seekErase = dialog.findViewById(seekbarId);
        seekErase.setProgress(sizeEraser);
        return this;
    }

}
