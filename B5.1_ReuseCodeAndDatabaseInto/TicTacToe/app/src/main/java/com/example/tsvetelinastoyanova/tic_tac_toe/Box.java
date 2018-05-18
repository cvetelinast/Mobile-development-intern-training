package com.example.tsvetelinastoyanova.tic_tac_toe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.tsvetelinastoyanova.tic_tac_toe.heplerclasses.BoxAttributes;
import com.example.tsvetelinastoyanova.tic_tac_toe.heplerclasses.Direction;

public class Box extends View {

    BoxAttributes boxAttributes;

    private boolean symbolForCurrentBox;
    private boolean checked;
    private boolean isThereWinner;
    private Direction direction;

    public Box(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);
        initBox();

        if (checked) {
            drawFigure(c);
        }
        if (isThereWinner) {
            Log.d("tag", "Execute onDraw() in Box when there is winner.");
            drawLineForWinner(c);
        }
    }

    public boolean isChecked() {
        return checked;
    }

    public void setOnTouchEvent(boolean s) {
        symbolForCurrentBox = s;
        checked = true;
        invalidate();
    }

    public void setIsThereWinner(Direction direction) {
        Log.d("tag", "Execute setIsThereWinner() in Box.");
        this.direction = direction;
        this.isThereWinner = true;
        requestLayout();
        invalidate();
    }

    private void drawFigure(Canvas c) {
        if (symbolForCurrentBox) {
            drawCircle(c);
        } else {
            drawCross(c);
        }
    }

    private void drawCross(Canvas canvas) {
        int height = boxAttributes.getHeight();
        int width = boxAttributes.getWidth();
        int margin = boxAttributes.getMargin();
        Paint paint = boxAttributes.getPaint();
        canvas.drawLine(margin, margin, width - margin, height - margin, paint);
        canvas.drawLine(margin, height - margin, width - margin, margin, paint);
    }

    private void drawCircle(Canvas canvas) {
        canvas.drawCircle(boxAttributes.getWidth() / 2, boxAttributes.getHeight() / 2, boxAttributes.getMargin(), boxAttributes.getPaint());
    }

    private void drawLineForWinner(Canvas canvas) {
        Log.d("tag", "Execute drawLineForWinner() in Box.");
        int height = boxAttributes.getHeight();
        int width = boxAttributes.getWidth();
        Paint paint = boxAttributes.getPaint();

        switch (direction.getId()) {
            case 0:
                canvas.drawLine(0, width / 2, height, width / 2, paint);
                break;
            case 1:
                canvas.drawLine(height / 2, 0, height / 2, height, paint);
                break;
            case 2:
                canvas.drawLine(0, 0, height, width, paint);
                break;
            case 3:
                canvas.drawLine(0, height, width, 0, paint);
                break;
        }
    }

    private void initBox() {
        int width = this.getMeasuredWidth();
        boxAttributes = new BoxAttributes(width, this.getMeasuredHeight(), width / 4);
    }
}
