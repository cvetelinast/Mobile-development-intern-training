package com.example.tsvetelinastoyanova.drawingapp.drawing.colorpicker.colorcontainers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.v4.graphics.ColorUtils;
import android.util.AttributeSet;
import android.view.View;

import com.example.tsvetelinastoyanova.drawingapp.drawing.colorpicker.colorpicker.PixelComponents;

public class Palette extends View {

    private Bitmap bitmap;
    private int red;
    private int green;
    private int blue;
    private int x;
    private int y;
    private boolean shouldRedrawBitmap = true;
    private Paint paint = new Paint();

    public Palette(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setDrawingCacheEnabled(true);
        initDefaultColor();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.RGB_565);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (shouldRedrawBitmap) {
            redrawCanvas();
        }
        canvas.drawBitmap(bitmap, 0, 0, paint);
        drawCircle(canvas);
    }

    public void mainColorChange(PixelComponents c) {
        this.red = c.getRed();
        this.green = c.getGreen();
        this.blue = c.getBlue();
        shouldRedrawBitmap = true;
        invalidate();
    }

    public void updateMark(int x, int y) {
        this.x = x;
        this.y = y;
        invalidate();
    }

    private void initDefaultColor() {
        this.red = 255;
        this.green = 0;
        this.blue = 0;
    }

    private void drawCircle(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        canvas.drawCircle(this.x, this.y, 25, paint);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(this.x, this.y, 20, paint);
    }

    private void redrawCanvas() {
        Canvas testCanvas = new Canvas(bitmap);
        for (int x = 0; x < 256; x++) {
            final int input = Color.argb(255 - x, red, green, blue);
            int firstColor = ColorUtils.compositeColors(input, Color.BLACK);
            int secondColor = Color.WHITE;
            Shader shader = new LinearGradient(0, x, this.getWidth(), x, new int[]{firstColor, secondColor}, null, Shader.TileMode.CLAMP);
            paint.setShader(shader);
            testCanvas.drawLine(0, x, this.getWidth(), x, paint);
        }
        shouldRedrawBitmap = false;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(this.getMeasuredWidth(), 255);
    }
}
