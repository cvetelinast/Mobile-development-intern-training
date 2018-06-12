package com.example.tsvetelinastoyanova.drawingapp.drawing.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;

public interface Tool {
    public void drawOnCanvas(Canvas drawCanvas, float beginX, float beginY, float endX, float endY,
                             Paint drawPaint);
}
