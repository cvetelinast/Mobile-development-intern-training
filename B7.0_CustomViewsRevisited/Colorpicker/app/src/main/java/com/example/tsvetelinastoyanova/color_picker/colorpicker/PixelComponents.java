package com.example.tsvetelinastoyanova.color_picker.colorpicker;

import android.graphics.Color;

public class PixelComponents {

    private int alpha;

    private int red;

    private int green;

    private int blue;

    private int x;

    private int y;

    private String CODE_COLOR = "#%02x%02x%02x";

    PixelComponents(int alpha, int red, int green, int blue, int x, int y) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        this.x = x;
        this.y = y;
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getCode() {
        return String.format(CODE_COLOR, red, green, blue);
    }

    public float[] getHSV() {
        float values[] = new float[3];
        Color.RGBToHSV(this.getRed(), this.getGreen(), this.getBlue(), values);
        return values;
    }
}
