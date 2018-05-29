package com.example.tsvetelinastoyanova.color_picker.colorpicker;

import android.graphics.Color;

public class ColorComponents {
    private int alpha;
    private int red;
    private int green;
    private int blue;

    public ColorComponents(int alpha, int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public int getAlpha() {
        return alpha;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public String getCode() {
        return String.format("#%02x%02x%02x", red, green, blue);
    }

    public float[] getHSV() {
        float values[] = new float[3];
        Color.RGBToHSV(this.getRed(), this.getGreen(), this.getBlue(), values);
        return values;
    }
}
