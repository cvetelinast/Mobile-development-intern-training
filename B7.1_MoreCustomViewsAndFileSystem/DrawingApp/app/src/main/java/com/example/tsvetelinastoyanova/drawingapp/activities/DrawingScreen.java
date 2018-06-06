package com.example.tsvetelinastoyanova.drawingapp.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tsvetelinastoyanova.drawingapp.handlers.FilesHandler;
import com.example.tsvetelinastoyanova.drawingapp.R;
import com.example.tsvetelinastoyanova.drawingapp.drawing.DialogBuilders.ColorPickerDialogBuilder;
import com.example.tsvetelinastoyanova.drawingapp.enums.DrawingTool;
import com.example.tsvetelinastoyanova.drawingapp.drawing.DrawingView;
import com.example.tsvetelinastoyanova.drawingapp.drawing.DialogBuilders.Eraser;
import com.example.tsvetelinastoyanova.drawingapp.drawing.DialogBuilders.Saver;

import java.io.File;

public class DrawingScreen extends AppCompatActivity {
    private int sizeEraser = 30;
    private DrawingView drawingView;
    private String nameOfDrawing = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing_screen);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initDrawingView();
        setOnBrushButtonClickListener(findViewById(R.id.brush));
        setOnEraseButtonClickListener(findViewById(R.id.eraser));
        setOnPipetteButtonClickListener(findViewById(R.id.pipette));
        setOnSaveButtonClickListener(findViewById(R.id.save));
    }

    /*  private void initDialog(ImageButton openDialogButton){
          setOnEraseButtonClickListener(openDialogButton);
      }
    */

    private void initDrawingView() {
        drawingView = findViewById(R.id.drawing);
        drawingView.setSizeEraser(sizeEraser);
    }

    private void setOnBrushButtonClickListener(ImageButton openBrushButton) {
        openBrushButton.setOnClickListener((v) -> {
            drawingView.setDrawingTool(DrawingTool.BRUSH);
        });
        openBrushButton.setOnLongClickListener((v) -> {
            ColorPickerDialogBuilder colorPickerDialogBuilder = new ColorPickerDialogBuilder(this);
            colorPickerDialogBuilder.setContentView(R.layout.color_picker_dialog)
                    .setTitleToDialog(R.id.dialogTitle, getResources().getString(R.string.color_picker_dialog_title))
                    .initButtonOk(R.id.dialogButtonOk)
                    .createInstance()
                    .showDialog();
            setOnClickListenerForBrushOk(colorPickerDialogBuilder.getDialog(), colorPickerDialogBuilder.getButtonOk());
            return true;
        });
    }

    private void setOnClickListenerForBrushOk(Dialog dialog, Button buttonOk) {
        buttonOk.setOnClickListener((v) -> {
            dialog.dismiss();
            colorButtonOpenDialog(dialog);
        });
    }

    private void colorButtonOpenDialog(Dialog dialog) {
        TextView chosenColor = dialog.findViewById(R.id.color);
        if (chosenColor.getBackground() instanceof ColorDrawable) {
            ColorDrawable cd = (ColorDrawable) chosenColor.getBackground();
            ImageButton buttonOpenColorpickerDialog = findViewById(R.id.brush);
            buttonOpenColorpickerDialog.setBackgroundColor(cd.getColor());
            drawingView.setDrawingTool(DrawingTool.BRUSH);
            drawingView.setPaintColor(cd.getColor());
        }
    }

    private void setOnEraseButtonClickListener(ImageButton openEraserButton) {
        openEraserButton.setOnClickListener((v) -> {
            drawingView.setDrawingTool(DrawingTool.ERASER);
        });

        openEraserButton.setOnLongClickListener((v) -> {
            Eraser eraser = new Eraser(this);
            eraser.setContentView(R.layout.erase_dialog)
                    .setTitleToDialog(R.id.title, getResources().getString(R.string.size_eraser_prompt))
                    .setPreviousSizeOfEraser(R.id.seekbar, sizeEraser)
                    .initButtonOk(R.id.button_ok)
                    .initButtonCancel(R.id.button_cancel)
                    .showDialog();

            setOnClickListenerForEraserOk(eraser.getDialog(), eraser.getButtonOk());
            setOnClickListenerForCancel(eraser.getDialog(), eraser.getButtonCancel());
            return true;
        });
    }

    private void setOnClickListenerForEraserOk(Dialog dialog, Button buttonOk) {
        buttonOk.setOnClickListener((v) -> {
            SeekBar seekErase = dialog.findViewById(R.id.seekbar);
            sizeEraser = seekErase.getProgress();
            drawingView.setSizeEraser(sizeEraser);
            dialog.dismiss();
        });
    }

    private void setOnPipetteButtonClickListener(ImageButton openPipetteButton) {
        openPipetteButton.setOnClickListener((v) -> {
            drawingView.setDrawingTool(DrawingTool.PIPETTE);
        });
    }

    private void setOnClickListenerForCancel(Dialog dialog, Button buttonCancel) {
        buttonCancel.setOnClickListener((v) -> {
            dialog.dismiss();
        });
    }

    private void setOnSaveButtonClickListener(ImageButton openSaveDialog) {
        openSaveDialog.setOnClickListener((v) -> {
            Saver saver = new Saver(this);
            saver.setContentView(R.layout.save_drawing_dialog)
                    .setTitleToDialog(R.id.title, getResources().getString(R.string.save_dialog_title))
                    .initNameInput(R.id.drawing_name_input, nameOfDrawing)
                    .initButtonOk(R.id.button_ok)
                    .initButtonCancel(R.id.button_cancel)
                    .showDialog();
            setOnClickListenerForSaverOk(saver.getDialog(), saver.getButtonOk());
            setOnClickListenerForCancel(saver.getDialog(), saver.getButtonCancel());
        });
    }

    private void setOnClickListenerForSaverOk(Dialog dialog, Button buttonOk) {
        buttonOk.setOnClickListener((v) -> {
            TextInputLayout textInputLayout = dialog.findViewById(R.id.drawing_name_input);
            EditText editText = textInputLayout.getEditText();
            nameOfDrawing = getResources().getString(R.string.jpg_format, editText.getText().toString());
            String nameOfDirectory = getResources().getString(R.string.directory);
            String fullPathToNewFile = getFilesDir().getAbsolutePath() + File.separator + nameOfDirectory;

            FilesHandler filesHandler = new FilesHandler();
            boolean isSavingFileSuccessful = filesHandler.saveDrawing(drawingView.getCanvasBitmap(), nameOfDrawing, fullPathToNewFile);
            if (isSavingFileSuccessful) {
                Toast.makeText(this, getResources().getString(R.string.success_saved_drawing), Toast.LENGTH_LONG);
                returnToDrawingListActivity();
            }

            dialog.dismiss();
        });
    }

   
    private void returnToDrawingListActivity() {
        Intent intent = new Intent(this, DrawingList.class);
        startActivity(intent);
    }


}
