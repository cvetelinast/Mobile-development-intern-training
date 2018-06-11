package com.example.tsvetelinastoyanova.drawingapp.drawing.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;

public class RectangleTool extends Tool {

    @Override
    public void executeAction(Canvas drawCanvas, float beginX, float beginY, float endX, float endY, Paint drawPaint){
        drawCanvas.drawRect(beginX, beginY, endX, endY, drawPaint);
    }
}
