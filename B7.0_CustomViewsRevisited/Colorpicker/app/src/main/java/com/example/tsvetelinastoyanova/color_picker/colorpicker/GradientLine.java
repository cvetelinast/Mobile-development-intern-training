package com.example.tsvetelinastoyanova.color_picker.colorpicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class GradientLine extends View{
    private final int[] mHueBarColors = new int[258];
    private int[] mMainColors = new int[65536];
    private Paint mPaint;

    public GradientLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        initColors();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(12);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float[] dims = new float[258];
        for (int i = 0; i < 258; i++) {
            dims[i] = (float) 1 / 258 * i;
        }
        Shader shader = new LinearGradient(0, 0, this.getWidth(), 0, mHueBarColors, dims, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        canvas.drawRect(new RectF(0, 0, this.getWidth(), this.getHeight()), paint);
    }

    void initColors() {

        // Initialize the colors of the hue slider bar
        int index = 0;
        for (float i = 0; i < 256; i += 256 / 42) // Red (#f00) to pink
        // (#f0f)
        {
            mHueBarColors[index] = Color.rgb(255, 0, (int) i);
            index++;
        }
        for (float i = 0; i < 256; i += 256 / 42) // Pink (#f0f) to blue
        // (#00f)
        {
            mHueBarColors[index] = Color.rgb(255 - (int) i, 0, 255);
            index++;
        }
        for (float i = 0; i < 256; i += 256 / 42) // Blue (#00f) to light
        // blue (#0ff)
        {
            mHueBarColors[index] = Color.rgb(0, (int) i, 255);
            index++;
        }
        for (float i = 0; i < 256; i += 256 / 42) // Light blue (#0ff) to
        // green (#0f0)
        {
            mHueBarColors[index] = Color.rgb(0, 255, 255 - (int) i);
            index++;
        }
        for (float i = 0; i < 256; i += 256 / 42) // Green (#0f0) to yellow
        // (#ff0)
        {
            mHueBarColors[index] = Color.rgb((int) i, 255, 0);
            index++;
        }
        for (float i = 0; i < 256; i += 256 / 42) // Yellow (#ff0) to red
        // (#f00)
        {
            mHueBarColors[index] = Color.rgb(255, 255 - (int) i, 0);
            index++;
        }
        Log.d("tag", index + "");
    }

}