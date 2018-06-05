package com.example.tsvetelinastoyanova.drawingapp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.example.tsvetelinastoyanova.drawingapp.drawing.DrawingTool;
import com.example.tsvetelinastoyanova.drawingapp.drawing.DrawingView;
import com.example.tsvetelinastoyanova.drawingapp.drawing.Eraser;
import com.example.tsvetelinastoyanova.drawingapp.drawing.Saver;

import java.io.File;
import java.io.FileOutputStream;

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
        setOnEraseButtonClickListener(findViewById(R.id.eraser));
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

    private void setOnEraseButtonClickListener(ImageButton openEraserDialog) {
        openEraserDialog.setOnClickListener((v) -> {
            drawingView.setDrawingTool(DrawingTool.ERASER);
        });

        openEraserDialog.setOnLongClickListener((v) -> {
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

            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = openFileOutput(nameOfDrawing, Context.MODE_PRIVATE);
                drawingView.getCanvasBitmap().compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            } catch (Exception e) {
                Log.d("Exception", "An exception with the stream while saving the drawing occured.");
                e.printStackTrace();
            }
            Log.d("tag", "SAVING");
            dialog.dismiss();
        });
    }


}
