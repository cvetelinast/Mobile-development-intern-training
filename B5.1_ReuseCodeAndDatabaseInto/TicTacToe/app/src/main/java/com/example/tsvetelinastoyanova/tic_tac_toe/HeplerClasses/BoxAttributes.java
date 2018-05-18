package com.example.tsvetelinastoyanova.tic_tac_toe.HeplerClasses;

import android.graphics.Paint;

public class BoxAttributes {
    private int width;
    private int height;
    private int margin;
    private Paint paint;

    public BoxAttributes(int width, int height, int margin) {
        this.width = width;
        this.height = height;
        this.margin = margin;
        this.paint = initPaint();
    }

    private Paint initPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(30);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        return paint;
    }

    public Paint getPaint() {
        return paint;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMargin() {
        return margin;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }
}
