package com.example.tsvetelinastoyanova.drawingapp.drawing.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class ElipseTool extends Tool {

    @Override
    public void executeAction(Canvas drawCanvas, float beginX, float beginY, float endX, float endY, Paint drawPaint){
        RectF oval = new RectF(beginX, beginY, endX, endY);
        drawCanvas.drawOval(oval, drawPaint);
    }
}
