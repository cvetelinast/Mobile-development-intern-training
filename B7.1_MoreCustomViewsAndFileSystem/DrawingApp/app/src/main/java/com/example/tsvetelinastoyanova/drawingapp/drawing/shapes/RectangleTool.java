package com.example.tsvetelinastoyanova.drawingapp.drawing.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;

public class RectangleTool implements Tool {

    @Override
    public void drawOnCanvas(Canvas drawCanvas, float beginX, float beginY, float endX, float endY, Paint drawPaint){
        drawCanvas.drawRect(beginX, beginY, endX, endY, drawPaint);
    }
}
