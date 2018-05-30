package com.example.tsvetelinastoyanova.color_picker.colorcontainers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.example.tsvetelinastoyanova.color_picker.colorpicker.PixelComponents;

public class Palette extends View {

    Paint paint = new Paint();

    int red;

    int green;

    int blue;

    int x;

    int y;

    Palette(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.red = 255;
        this.green = 0;
        this.blue = 0;
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int repeat = this.getWidth() / 255;
        int saveLocation = 0;
        int[] colors = new int[2];

        for (int x = 0; x < 256; x++) {
            colors[0] = Color.argb(255 - x, red, green, blue);
            colors[1] = Color.BLACK;
            for (int i = 0; i < repeat; i++) {
                Shader shader = new LinearGradient(0, saveLocation + i, this.getWidth(), saveLocation + i, colors, null, Shader.TileMode.CLAMP);
                paint.setShader(shader);
                canvas.drawLine(0, saveLocation + i, this.getWidth(), saveLocation + i, paint);
            }
            saveLocation = saveLocation + repeat;
        }
        paint.setShader(null); // clear any previous shader
        drawMark(canvas);
    }

    public void mainColorChange(PixelComponents c) {
        this.red = c.getRed();
        this.green = c.getGreen();
        this.blue = c.getBlue();
        invalidate();
    }

    public void updateMark(int x, int y) {
        this.x = x;
        this.y = y;
        invalidate();
    }

    private void drawMark(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        canvas.drawCircle(this.x, this.y, 25, paint);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(this.x, this.y, 20, paint);
    }

}
