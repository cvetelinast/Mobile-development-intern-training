package com.example.tsvetelinastoyanova.drawingapp.drawing.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class EllipseTool implements Tool {

    @Override
    public void drawOnCanvas(Canvas drawCanvas, float beginX, float beginY, float endX, float endY, Paint drawPaint){
        RectF oval = new RectF(beginX, beginY, endX, endY);
        drawCanvas.drawOval(oval, drawPaint);
    }
}
