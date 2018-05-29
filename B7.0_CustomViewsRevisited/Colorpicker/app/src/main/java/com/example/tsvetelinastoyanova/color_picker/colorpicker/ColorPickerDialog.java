package com.example.tsvetelinastoyanova.color_picker.colorpicker;


import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.tsvetelinastoyanova.color_picker.R;

public class ColorPickerDialog {
    private GradientLine gradientLine;
    private Palette palette;
    private Dialog dialog;
    private TextView chosenColor;

    public ColorPickerDialog(Dialog dialog) {
        this.dialog = dialog;
        gradientLine = dialog.findViewById(R.id.gradient);
        palette = dialog.findViewById(R.id.palette);
        chosenColor = dialog.findViewById(R.id.color);
        initSmallComponents();
        setClickListeners();
    }

    private void initSmallComponents() {
        chosenColor.setBackgroundColor(Color.RED);
        updateTextViews(new ColorComponents(255, 255, 0, 0));
    }

    private void setClickListeners() {
        gradientLine.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ColorComponents colorComponents = getColorsOnTouch(v, event);
                palette.mainColorChange(colorComponents);
                chosenColor.setBackgroundColor(Color.argb(colorComponents.getAlpha(), colorComponents.getRed(), colorComponents.getGreen(), colorComponents.getBlue()));
                updateTextViews(colorComponents);
                return true;
            }
        });

        palette.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ColorComponents colorComponents = getColorsOnTouch(v, event);
                chosenColor.setBackgroundColor(Color.argb(colorComponents.getAlpha(), colorComponents.getRed(), colorComponents.getGreen(), colorComponents.getBlue()));
                updateTextViews(colorComponents);
                return true;
            }
        });
    }

    private void updateTextViews(ColorComponents colorComponents) {
        TextView colorCode = dialog.findViewById(R.id.colorCode);
        colorCode.setText(colorComponents.getCode());
        updateRBG(colorComponents);
        updateHSV(colorComponents);
    }

    private void updateRBG(ColorComponents colorComponents) {
        TextView redCode = dialog.findViewById(R.id.red);
        TextView greenCode = dialog.findViewById(R.id.green);
        TextView blueCode = dialog.findViewById(R.id.blue);

        redCode.setText("R: " + colorComponents.getRed());
        greenCode.setText("G: " + colorComponents.getGreen());
        blueCode.setText("B: " + colorComponents.getBlue());
    }

    private void updateHSV(ColorComponents colorComponents) {
        TextView hueCode = dialog.findViewById(R.id.hue);
        TextView saturationCode = dialog.findViewById(R.id.saturation);
        TextView brightnessCode = dialog.findViewById(R.id.brightness);

        float values[] = colorComponents.getHSV();
        if (values.length == 3) {
            hueCode.setText("H: " + values[0]);
            saturationCode.setText("S: " + values[1]);
            brightnessCode.setText("V: " + values[2]);
        }
    }

    private ColorComponents getColorsOnTouch(View v, MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        // v.getBackground();
        v.buildDrawingCache();
        Bitmap bitmap = v.getDrawingCache();
        x = validateCoordinates(x, bitmap.getWidth());
        y = validateCoordinates(y, bitmap.getHeight());
        int pixel = bitmap.getPixel(x, y);
        v.destroyDrawingCache();

        return new ColorComponents(Color.alpha(pixel), Color.red(pixel), Color.green(pixel), Color.blue(pixel));
    }

    private int validateCoordinates(int c, int max) {
        if (c >= max) {
            c = max - 1;
        } else if (c < 0) {
            c = 0;
        }
        return c;
    }

}