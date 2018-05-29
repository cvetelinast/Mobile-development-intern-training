package com.example.tsvetelinastoyanova.color_picker.colorpicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class Palette extends View {
    Paint paint = new Paint();
    int red;
    int green;
    int blue;

    Palette(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.red = 255;
        this.green = 0;
        this.blue = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int repeat = this.getWidth() / 255;
        int saveLocation = 0;

        for (int x = 0; x < 256; x++) {
            int[] colors = new int[2];
            colors[0] = getTheExactColor(x); //Color.argb(255-x, x, x, 255);
            colors[1] = Color.argb(255, 0, 0, 0);//Color.BLACK;
            for (int i = 0; i < repeat; i++) {
                Shader shader = new LinearGradient(0, saveLocation + i, this.getWidth(), saveLocation + i, colors, null, Shader.TileMode.CLAMP);
                paint.setShader(shader);
                canvas.drawLine(0, saveLocation + i, this.getWidth(), saveLocation + i, paint);
            }
            saveLocation = saveLocation + repeat;
        }
        paint.setShader(null);
    }


    void mainColorChange(ColorComponents c) {
        this.red = c.getRed();
        this.green = c.getGreen();
        this.blue = c.getBlue();
        invalidate();
    }


    private int getTheExactColor(int x) {
        Color color = new Color();
        if (this.red == 255) {
           /* if (this.blue == 0 && this.green == 0) {
                return Color.argb(255 - x, this.red, 0, 0); // red
            } else*/
            if (this.blue == 0) {
                return Color.argb(255 - x, this.red, this.green, 0); // orange
            } else if (this.green == 0) {
                return Color.argb(255 - x, this.red, 0, this.blue); // purple
            }
        } else if (this.green == 255) {
            /*if (this.blue == 0 && this.red == 0) {
                return Color.argb(255 - x, 0, this.green, 0); // green
            } else */
            if (this.blue == 0) {
                return Color.argb(255 - x, this.red, 255, 0); // yellow
            } else if (this.red == 0) {
                return Color.argb(255 - x, 0, this.green, this.blue); // green/blue
            }
        } else if (this.blue == 255) {
            /*if (this.red == 0 && this.green == 0) {
                return Color.argb(255 - x, 0, 0, this.blue); // blue
            } else */
            if (this.red == 0) {
                return Color.argb(255 - x, 0, this.green, this.blue); // cyan
            } else if (this.green == 0) {
                return Color.argb(255 - x, this.red, 0, this.blue); // purple
            }
        }
        return 0;
    }

}
