package com.example.tsvetelinastoyanova.drawingapp.drawing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tsvetelinastoyanova.drawingapp.drawing.DialogBuilders.ActionDialog;
import com.example.tsvetelinastoyanova.drawingapp.drawing.shapes.ElipseTool;
import com.example.tsvetelinastoyanova.drawingapp.drawing.shapes.RectangleTool;
import com.example.tsvetelinastoyanova.drawingapp.drawing.shapes.Tool;
import com.example.tsvetelinastoyanova.drawingapp.enums.ActionDrawing;
import com.example.tsvetelinastoyanova.drawingapp.enums.DrawingTool;


public class DrawingView extends View {
    private Path drawPath;
    private Paint drawPaint, canvasPaint;
    private int paintColor = 0xFF660000;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;

    private DrawingTool drawingTool;
    private int sizeEraser;
    private int sizeBrush;
    private ImageButton currentColorField;

    float beginX;
    float beginY;
    float endX;
    float endY;

    public Bitmap getCanvasBitmap() {
        return canvasBitmap;
    }

    public void setSizeEraser(int sizeEraser) {
        this.sizeEraser = sizeEraser;
    }

    public void setSizeBrush(int sizeBrush) {
        this.sizeBrush = sizeBrush;
    }

    public void setCurrentColorFieldId(ImageButton currentColorField) {
        this.currentColorField = currentColorField;
    }

    public void setDrawingTool(DrawingTool drawingTool) {
        this.drawingTool = drawingTool;
    }

    public void setPaintColor(int paintColor) {
        this.paintColor = paintColor;
        drawPaint.setColor(this.paintColor);
    }

    public void setCanvasBitmap(Bitmap canvasBitmap) {
        this.canvasBitmap = canvasBitmap.copy(Bitmap.Config.ARGB_8888, true);
        drawCanvas = new Canvas(this.canvasBitmap);
    }

    public boolean isFigureCreated() {
        return drawingTool.name().equals(DrawingTool.RECTANGLE.name()) || drawingTool.name().equals(DrawingTool.OVAL.name());
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    public int getPaintColor() {
        return paintColor;
    }

    private void setupDrawing() {
        //get drawing area setup for interaction
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);

        //Setting the anti-alias, stroke join and cap styles will make the user's drawings appear smoother.
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);

        drawingTool = DrawingTool.BRUSH;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (canvasBitmap == null) {
            canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            drawCanvas = new Canvas(canvasBitmap);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (drawingTool.name().equals(DrawingTool.BRUSH.name())) {
            drawPaint.setStrokeWidth(sizeBrush);
            drawPaint.setXfermode(null);
            canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
            canvas.drawPath(drawPath, drawPaint);
        } else if (drawingTool.name().equals(DrawingTool.ERASER.name())) {
            drawPaint.setStrokeWidth(sizeEraser);
            drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
            canvas.drawPath(drawPath, drawPaint);
        } else if (drawingTool.name().equals(DrawingTool.PIPETTE.name())) {
            canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        } else if (drawingTool.name().equals(DrawingTool.RECTANGLE.name())) {
            canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
            canvas.drawRect(beginX, beginY, endX, endY, drawPaint);
        } else if (drawingTool.name().equals(DrawingTool.OVAL.name())) {
            canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
            RectF oval = new RectF(beginX, beginY, endX, endY);
            canvas.drawOval(oval, drawPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        if (drawingTool.name().equals(DrawingTool.PIPETTE.name())) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    paintColor = getColorOfPixelOnTouch(event);
                    drawPaint.setColor(paintColor);
                    currentColorField.setBackgroundColor(paintColor);
                    break;
                default:
                    return false;
            }
        } else if (isFigureCreated()) {
            onTouchEventWhenDrawingFigures(event, touchX, touchY);
        } else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    drawPath.moveTo(touchX, touchY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    drawPath.lineTo(touchX, touchY);
                    break;
                case MotionEvent.ACTION_UP:
                    drawCanvas.drawPath(drawPath, drawPaint);
                    drawPath.reset();
                    break;
                default:
                    return false;
            }
            invalidate();
            return true;
        }
        return true;
    }

    private void onTouchEventWhenDrawingFigures(MotionEvent event, float touchX, float touchY) {
        Tool tool = new Tool();
        if (drawingTool.name().equals(DrawingTool.RECTANGLE.name())) {
            tool = new RectangleTool();
        } else if (drawingTool.name().equals(DrawingTool.OVAL.name())) {
            tool = new ElipseTool();
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                beginX = touchX;
                beginY = touchY;
                break;
            case MotionEvent.ACTION_MOVE:
                endX = touchX;
                endY = touchY;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                endX = touchX;
                endY = touchY;
                tool.executeAction(drawCanvas, beginX, beginY, endX, endY, drawPaint);
                drawPath.reset();
                invalidate();
                break;
        }
    }

    private int validateCoordinates(int c, int max) {
        if (c >= max) {
            c = max - 1;
        } else if (c <= 0) {
            c = 1;
        }
        return c;
    }

    private int getColorOfPixelOnTouch(MotionEvent event) {
        buildDrawingCache();
        Bitmap bitmap = getDrawingCache();
        int x = validateCoordinates((int) event.getX(), bitmap.getWidth());
        int y = validateCoordinates((int) event.getY(), bitmap.getHeight());
        int pixel = bitmap.getPixel(x, y);
        destroyDrawingCache();
        return Color.argb(Color.alpha(pixel), Color.red(pixel), Color.green(pixel), Color.blue(pixel));
    }

}

