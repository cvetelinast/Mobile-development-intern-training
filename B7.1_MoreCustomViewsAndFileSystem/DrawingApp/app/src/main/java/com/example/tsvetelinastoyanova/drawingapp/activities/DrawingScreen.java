package com.example.tsvetelinastoyanova.drawingapp.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.example.tsvetelinastoyanova.drawingapp.drawing.DialogBuilders.EraserDialog;
import com.example.tsvetelinastoyanova.drawingapp.handlers.FilesHandler;
import com.example.tsvetelinastoyanova.drawingapp.R;
import com.example.tsvetelinastoyanova.drawingapp.drawing.DialogBuilders.ColorPickerDialog;
import com.example.tsvetelinastoyanova.drawingapp.enums.DrawingTool;
import com.example.tsvetelinastoyanova.drawingapp.drawing.DrawingView;
import com.example.tsvetelinastoyanova.drawingapp.drawing.DialogBuilders.SaveDialog;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DrawingScreen extends AppCompatActivity {
    private int sizeEraser = 30;
    private int sizeBrush = 30;
    private Bitmap bitmap;
    private DrawingView drawingView;
    private String nameOfDrawing = "";
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing_screen);
        Intent intent = getIntent();
        path = intent.getStringExtra(this.getResources().getString(R.string.edit_drawing));
    }

    @Override
    protected void onStart() {
        super.onStart();
        setDefaultComponentsOfDrawingView();
        initDialog();
    }

    private void initDialog() {
        setOnBrushButtonClickListener(findViewById(R.id.brush));
        setOnEraseButtonClickListener(findViewById(R.id.eraser));
        setOnPipetteButtonClickListener(findViewById(R.id.pipette));
        setOnRectangleButtonClickListener(findViewById(R.id.rectangle));
        setOnOvalButtonClickListener(findViewById(R.id.oval));
        setOnSaveButtonClickListener(findViewById(R.id.save));
    }

    private void setDefaultComponentsOfDrawingView() {
        drawingView = findViewById(R.id.drawing);
        if (path != null) {
            bitmap = BitmapFactory.decodeFile(path);
            drawingView.setCanvasBitmap(bitmap);
            nameOfDrawing = path.substring(path.lastIndexOf("/") + 1);
        }
        drawingView.setSizeEraser(sizeEraser);
        drawingView.setSizeBrush(sizeBrush);
        drawingView.setCurrentColorFieldId(findViewById(R.id.currentColor));
        currentColorChange(drawingView.getPaintColor());
        setBackgroundColorToButton(findViewById(R.id.brush));
    }

    private void setOnBrushButtonClickListener(ImageButton openBrushButton) {
        openBrushButton.setOnClickListener((v) -> {
            changeButtonBackground(openBrushButton);
            drawingView.setDrawingTool(DrawingTool.BRUSH);
        });
        openBrushButton.setOnLongClickListener((v) -> {
            changeButtonBackground(openBrushButton);
            drawingView.setDrawingTool(DrawingTool.BRUSH);
            ColorPickerDialog colorPickerDialog = new ColorPickerDialog(this);
            colorPickerDialog.setContentView(R.layout.color_picker_dialog)
                    .setTitleToDialog(R.id.dialogTitle, getResources().getString(R.string.color_picker_dialog_title))
                    .setPreviousSizeOfTool(R.id.seekbar, sizeBrush)
                    .initButtonOk(R.id.dialogButtonOk)
                    .createInstance()
                    .showDialog();
            setOnClickListenerForBrushOk(colorPickerDialog.getDialog(), colorPickerDialog.getButtonOk());
            return true;
        });
    }

    private void setOnEraseButtonClickListener(ImageButton openEraserButton) {
        openEraserButton.setOnClickListener((v) -> {
            changeButtonBackground(openEraserButton);
            drawingView.setDrawingTool(DrawingTool.ERASER);
        });

        openEraserButton.setOnLongClickListener((v) -> {
            changeButtonBackground(openEraserButton);
            drawingView.setDrawingTool(DrawingTool.ERASER);
            EraserDialog eraserDialog = new EraserDialog(this);
            eraserDialog.setContentView(R.layout.erase_dialog)
                    .setTitleToDialog(R.id.title, getResources().getString(R.string.size_eraser_prompt))
                    .setPreviousSizeOfTool(R.id.seekbar, sizeEraser)
                    .initButtonOk(R.id.button_ok)
                    .initButtonCancel(R.id.button_cancel)
                    .showDialog();

            setOnClickListenerForEraserOk(eraserDialog.getDialog(), eraserDialog.getButtonOk());
            setOnClickListenerForCancel(eraserDialog.getDialog(), eraserDialog.getButtonCancel());
            return true;
        });
    }

    private void setOnPipetteButtonClickListener(ImageButton selectPipetteButton) {
        selectPipetteButton.setOnTouchListener((v, event) -> {
            changeButtonBackground(selectPipetteButton);
            drawingView.setDrawingTool(DrawingTool.PIPETTE);
            return true;
        });
    }

    private void setOnRectangleButtonClickListener(ImageButton selectRectangleButton) {
        selectRectangleButton.setOnTouchListener((v, event) -> {
            changeButtonBackground(selectRectangleButton);
            drawingView.setDrawingTool(DrawingTool.RECTANGLE);
            return true;
        });
    }

    private void setOnOvalButtonClickListener(ImageButton selectOvalButton) {
        selectOvalButton.setOnTouchListener((v, event) -> {
            changeButtonBackground(selectOvalButton);
            drawingView.setDrawingTool(DrawingTool.OVAL);
            return true;
        });
    }

    private void setOnSaveButtonClickListener(ImageButton openSaveDialog) {
        openSaveDialog.setOnClickListener((v) -> {
            changeButtonBackground(openSaveDialog);
            SaveDialog saveDialog = new SaveDialog(this);
            saveDialog.setContentView(R.layout.save_drawing_dialog)
                    .setTitleToDialog(R.id.title, getResources().getString(R.string.save_dialog_title))
                    .initNameInput(R.id.drawing_name_input, nameOfDrawing)
                    .initButtonOk(R.id.button_ok)
                    .initButtonCancel(R.id.button_cancel)
                    .showDialog();
            setOnClickListenerForSaverOk(saveDialog.getDialog(), saveDialog.getButtonOk());
            setOnClickListenerForCancel(saveDialog.getDialog(), saveDialog.getButtonCancel());
        });
    }

    private void setOnClickListenerForBrushOk(Dialog dialog, Button buttonOk) {
        buttonOk.setOnClickListener((v) -> {
            SeekBar seekBrush = dialog.findViewById(R.id.seekbar);
            sizeBrush = seekBrush.getProgress();
            drawingView.setSizeBrush(sizeBrush);
            colorButtonOpenDialog(dialog);
            dialog.dismiss();
        });
    }

    private void colorButtonOpenDialog(Dialog dialog) {
        TextView chosenColor = dialog.findViewById(R.id.color);
        if (chosenColor.getBackground() instanceof ColorDrawable) {
            ColorDrawable cd = (ColorDrawable) chosenColor.getBackground();
            currentColorChange(cd.getColor());
            drawingView.setDrawingTool(DrawingTool.BRUSH);
            drawingView.setPaintColor(cd.getColor());
        }
    }

    private void currentColorChange(int color) {
        ImageButton currentColor = findViewById(R.id.currentColor);
        currentColor.setBackgroundColor(color);
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

    private void setOnClickListenerForSaverOk(Dialog dialog, Button buttonOk) {
        buttonOk.setOnClickListener((v) -> {
            TextInputLayout textInputLayout = dialog.findViewById(R.id.drawing_name_input);
            EditText editText = textInputLayout.getEditText();
            nameOfDrawing = getResources().getString(R.string.jpg_format, editText.getText().toString());
            String nameOfDirectory = getResources().getString(R.string.directory);
            String fullPathToNewFile = getFilesDir().getAbsolutePath() + File.separator + nameOfDirectory;

            FilesHandler filesHandler = new FilesHandler();
            filesHandler.saveDrawing(drawingView.getCanvasBitmap(), nameOfDrawing,
                    fullPathToNewFile, this,
                    () -> Toast.makeText(this, getResources().getString(R.string.before_save_dialog), Toast.LENGTH_SHORT).show(),
                    () -> {
                        Toast.makeText(this, getResources().getString(R.string.success_saved_drawing), Toast.LENGTH_SHORT).show();
                        returnToDrawingListActivity();
                    });
            dialog.dismiss();
        });
    }

    private void returnToDrawingListActivity() {
        Intent intent = new Intent(this, DrawingList.class);
        startActivity(intent);
    }

    private void removeAllBackgroundTints() {
        findViewById(R.id.brush).setBackgroundColor(getResources().getColor(R.color.grey));
        findViewById(R.id.eraser).setBackgroundColor(getResources().getColor(R.color.grey));
        findViewById(R.id.pipette).setBackgroundColor(getResources().getColor(R.color.grey));
        findViewById(R.id.rectangle).setBackgroundColor(getResources().getColor(R.color.grey));
        findViewById(R.id.oval).setBackgroundColor(getResources().getColor(R.color.grey));
        findViewById(R.id.save).setBackgroundColor(getResources().getColor(R.color.grey));
    }

    private void setBackgroundColorToButton(ImageButton imageButton) {
        imageButton.setBackgroundColor(getResources().getColor(R.color.dark_grey));
    }

    private void changeButtonBackground(ImageButton imageButton) {
        removeAllBackgroundTints();
        setBackgroundColorToButton(imageButton);
    }

}
