package com.example.tsvetelinastoyanova.drawingapp.drawing.DialogBuilders;

import android.content.Context;
import android.widget.SeekBar;

public class EraserDialog extends ActionDialog {

    public EraserDialog(Context context) {
        super(context);
    }

    @Override
    public EraserDialog setPreviousSizeOfTool(int seekbarId, int sizeEraser) {
        SeekBar seekErase = dialog.findViewById(seekbarId);
        seekErase.setProgress(sizeEraser);
        return this;
    }
}
