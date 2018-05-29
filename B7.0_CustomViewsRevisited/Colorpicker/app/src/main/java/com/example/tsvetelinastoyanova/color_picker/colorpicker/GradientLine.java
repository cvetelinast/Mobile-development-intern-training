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

public class GradientLine extends View {
    private final int[] hueBarColors = new int[258];
    private final float[] dims = new float[258];

    public GradientLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        initColors();
        initDims();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Shader shader = new LinearGradient(0, 0, this.getWidth(), 0, hueBarColors, dims, Shader.TileMode.CLAMP);
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
            hueBarColors[index] = Color.rgb(255, 0, (int) i);
            index++;
        }
        for (float i = 0; i < 256; i += 256 / 42) // Pink (#f0f) to blue
        // (#00f)
        {
            hueBarColors[index] = Color.rgb(255 - (int) i, 0, 255);
            index++;
        }
        for (float i = 0; i < 256; i += 256 / 42) // Blue (#00f) to light
        // blue (#0ff)
        {
            hueBarColors[index] = Color.rgb(0, (int) i, 255);
            index++;
        }
        for (float i = 0; i < 256; i += 256 / 42) // Light blue (#0ff) to
        // green (#0f0)
        {
            hueBarColors[index] = Color.rgb(0, 255, 255 - (int) i);
            index++;
        }
        for (float i = 0; i < 256; i += 256 / 42) // Green (#0f0) to yellow
        // (#ff0)
        {
            hueBarColors[index] = Color.rgb((int) i, 255, 0);
            index++;
        }
        for (float i = 0; i < 256; i += 256 / 42) // Yellow (#ff0) to red
        // (#f00)
        {
            hueBarColors[index] = Color.rgb(255, 255 - (int) i, 0);
            index++;
        }
        Log.d("tag", index + "");
    }

    private void initDims() {
        for (int i = 0; i < 258; i++) {
            dims[i] = (float) 1 / 258 * i;
        }
    }

}