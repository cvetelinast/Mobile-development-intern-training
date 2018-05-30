package com.example.tsvetelinastoyanova.color_picker.colorpicker;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.tsvetelinastoyanova.color_picker.R;
import com.example.tsvetelinastoyanova.color_picker.colorcontainers.GradientLine;
import com.example.tsvetelinastoyanova.color_picker.colorcontainers.Palette;

public class ColorPickerDialog {

    private Dialog dialog;

    private TextView boxShowingChosenColor;

    private View paletteView;

    private int lastLocationX;

    private int lastLocationY;

    private int lastColor;

    public ColorPickerDialog(Dialog dialog) {
        this.dialog = dialog;
        boxShowingChosenColor = dialog.findViewById(R.id.color);
        initSmallComponents();
        setClickListeners(dialog.findViewById(R.id.gradient), dialog.findViewById(R.id.palette));
    }

    private void initSmallComponents() {
        boxShowingChosenColor.setBackgroundColor(Color.RED);
        updateTextViews(new PixelComponents(255, 255, 0, 0, 0, 0));
    }

    private void setClickListeners(GradientLine gradientLine, Palette palette) {
        gradientLine.setOnTouchListener((v, event) -> {
            PixelComponents pixelComponents = getColorComponentsOnTouch(v, event);
            modificationsOnGradientLineClicked(gradientLine, palette, pixelComponents);
            return true;
        });

        palette.setOnTouchListener((v, event) -> {
            PixelComponents pixelComponents = getColorComponentsOnTouch(v, event);
            modificationsOnPaletteClicked(v, palette, pixelComponents);
            return true;
        });
    }

    private PixelComponents getColorComponentsOnTouch(View v, MotionEvent event) {
        v.buildDrawingCache();
        Bitmap bitmap = v.getDrawingCache();
        int x = validateCoordinates((int) event.getX(), bitmap.getWidth());
        int y = validateCoordinates((int) event.getY(), bitmap.getHeight());
        int pixel = bitmap.getPixel(x, y);
        v.destroyDrawingCache();
        return new PixelComponents(Color.alpha(pixel), Color.red(pixel), Color.green(pixel), Color.blue(pixel), x, y);
    }

    private void modificationsOnGradientLineClicked(GradientLine gradientLine, Palette palette, PixelComponents pixelComponents) {
        palette.mainColorChange(pixelComponents);
        gradientLine.changeLocationForIndicator(pixelComponents.getX());
        if(paletteView == null){
            updateSmallComponents(pixelComponents);
        } else {
            PixelComponents newPixelComponents = getColorFromPreviousMarkLocation();
            updateSmallComponents(newPixelComponents);
        }
    }

    private void modificationsOnPaletteClicked(View v, Palette palette, PixelComponents pixelComponents) {
        palette.updateMark(pixelComponents.getX(), pixelComponents.getY());
        savePaletteComponents(v, pixelComponents.getX(), pixelComponents.getY());
        updateSmallComponents(pixelComponents);
    }

    private PixelComponents getColorFromPreviousMarkLocation() {
        paletteView.buildDrawingCache();
        Bitmap bitmap = paletteView.getDrawingCache();
        int pixel = bitmap.getPixel(this.lastLocationX, this.lastLocationY);
        paletteView.destroyDrawingCache();
        return new PixelComponents(Color.alpha(pixel), Color.red(pixel), Color.green(pixel), Color.blue(pixel), this.lastLocationX, this.lastLocationY);
    }

    private void updateSmallComponents(PixelComponents pixelComponents) {
        updateBoxWithChosenColor(pixelComponents);
        updateTextViews(pixelComponents);
    }

    private void updateBoxWithChosenColor(PixelComponents pixelComponents) {
        lastColor = Color.argb(pixelComponents.getAlpha(), pixelComponents.getRed(), pixelComponents.getGreen(), pixelComponents.getBlue());
        boxShowingChosenColor.setBackgroundColor(lastColor);
    }

    private void savePaletteComponents(View v, int x, int y) {
        this.paletteView = v;
        this.lastLocationX = x;
        this.lastLocationY = y;
    }

    private void updateTextViews(PixelComponents pixelComponents) {
        TextView colorCode = dialog.findViewById(R.id.colorCode);
        colorCode.setText(pixelComponents.getCode());
        updateRBG(pixelComponents);
        updateHSV(pixelComponents);
    }

    private void updateRBG(PixelComponents pixelComponents) {
        TextView redCode = dialog.findViewById(R.id.red);
        TextView greenCode = dialog.findViewById(R.id.green);
        TextView blueCode = dialog.findViewById(R.id.blue);

        redCode.setText("R: " + pixelComponents.getRed());
        greenCode.setText("G: " + pixelComponents.getGreen());
        blueCode.setText("B: " + pixelComponents.getBlue());
    }

    private void updateHSV(PixelComponents pixelComponents) {
        TextView hueCode = dialog.findViewById(R.id.hue);
        TextView saturationCode = dialog.findViewById(R.id.saturation);
        TextView brightnessCode = dialog.findViewById(R.id.brightness);

        float values[] = pixelComponents.getHSV();
        if (values.length == 3) {
            hueCode.setText("H: " + String.format("%.2f", values[0]));
            saturationCode.setText("S: " + String.format("%.2f", values[1]));
            brightnessCode.setText("V: " + String.format("%.2f", values[2]));
        }
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