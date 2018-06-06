package com.example.tsvetelinastoyanova.drawingapp.drawing.DialogBuilders;

import android.content.Context;
import android.widget.SeekBar;

public class Eraser extends ActionDialog {

    public Eraser(Context context) {
        super(context);
    }

    @Override
    public Eraser setPreviousSizeOfEraser(int seekbarId, int sizeEraser) {
        SeekBar seekErase = dialog.findViewById(seekbarId);
        seekErase.setProgress(sizeEraser);
        return this;
    }
}
