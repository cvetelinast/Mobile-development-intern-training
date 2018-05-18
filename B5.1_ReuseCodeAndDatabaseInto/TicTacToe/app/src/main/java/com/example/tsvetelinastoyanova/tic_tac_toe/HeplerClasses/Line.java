package com.example.tsvetelinastoyanova.tic_tac_toe.heplerclasses;

public class Line {
    private int startIndex;
    private int middleIndex;
    private int endIndex;
    private Direction direction;
    private boolean isThereWinner;

    public Line() {
        this.startIndex = 0;
        this.middleIndex = 0;
        this.endIndex = 0;
        this.direction = Direction.HORIZONTAL;
        this.isThereWinner = false;
    }

    public Line(int startIndex, int middleIndex, int endIndex, Direction direction) {
        this.startIndex = startIndex;
        this.middleIndex = middleIndex;
        this.endIndex = endIndex;
        this.direction = direction;
        this.isThereWinner = true;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getMiddleIndex() {
        return middleIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean isThereWinner() {
        return isThereWinner;
    }
}
