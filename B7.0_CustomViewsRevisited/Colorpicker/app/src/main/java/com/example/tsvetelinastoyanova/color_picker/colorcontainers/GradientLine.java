package com.example.tsvetelinastoyanova.color_picker.colorcontainers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class GradientLine extends View {

    private final int[] hueBarColors = new int[258];
    private final float[] dims = new float[258];
    private int locationX = 0;
    private Bitmap bitmap;
    private boolean shouldRedrawBitmap = true;
    private Paint paint = new Paint();

    public GradientLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        initColors();
        initDims();
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWitdth, int oldHeight) {
        super.onSizeChanged(width, height, oldWitdth, oldHeight);
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.RGB_565);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (shouldRedrawBitmap) {
            drawCanvasAtFirst();
        }
        canvas.drawBitmap(bitmap, 0, 0, paint);
        drawRectIndicator(canvas);
    }

    public void drawRectIndicator(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        canvas.drawRect(locationX - 14, 0, locationX + 14, getMeasuredHeight(), paint);
        paint.setColor(Color.WHITE);
        canvas.drawRect(locationX - 12, 0, locationX + 12, getMeasuredHeight(), paint);
    }

    public void changeLocationForIndicator(int x) {
        this.locationX = x;
        invalidate();
    }

    private void drawCanvasAtFirst() {
        Canvas testCanvas = new Canvas(bitmap);
        Shader shader = new LinearGradient(0, 0, this.getWidth(), 0, hueBarColors, dims, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        testCanvas.drawRect(new RectF(0, 0, this.getWidth(), this.getHeight()), paint);
        shouldRedrawBitmap = false;
    }

    private void initColors() {
        int index = 0;
        // Red (#f00) to pink (#f0f)
        for (float i = 0; i < 256; i += 256 / 42) {
            hueBarColors[index] = Color.rgb(255, 0, (int) i);
            index++;
        }
        // Pink (#f0f) to blue (#00f)
        for (float i = 0; i < 256; i += 256 / 42) {
            hueBarColors[index] = Color.rgb(255 - (int) i, 0, 255);
            index++;
        }
        // Blue (#00f) to light blue (#0ff)
        for (float i = 0; i < 256; i += 256 / 42) {
            hueBarColors[index] = Color.rgb(0, (int) i, 255);
            index++;
        }
        // Light blue (#0ff) to green (#0f0)
        for (float i = 0; i < 256; i += 256 / 42) {
            hueBarColors[index] = Color.rgb(0, 255, 255 - (int) i);
            index++;
        }
        // Green (#0f0) to yellow (#ff0)
        for (float i = 0; i < 256; i += 256 / 42) {
            hueBarColors[index] = Color.rgb((int) i, 255, 0);
            index++;
        }
        // Yellow (#ff0) to red (#f00)
        for (float i = 0; i < 256; i += 256 / 42) {
            hueBarColors[index] = Color.rgb(255, 255 - (int) i, 0);
            index++;
        }
    }

    private void initDims() {
        for (int i = 0; i < 258; i++) {
            dims[i] = (float) 1 / 258 * i;
        }
    }
}