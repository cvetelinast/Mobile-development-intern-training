package com.example.tsvetelinastoyanova.drawingapp.enums;

public enum DrawingTool {
    BRUSH(0), ERASER(1), FILL(2), PIPETTE(3), RECTANGLE(4), OVAL(5);

    int id;

    DrawingTool(int id) {
        this.id = id;
    }
}
