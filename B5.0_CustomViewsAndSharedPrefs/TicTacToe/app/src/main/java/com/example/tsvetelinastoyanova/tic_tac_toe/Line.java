package com.example.tsvetelinastoyanova.tic_tac_toe;

public class Line {
    int startIndex;
    int middleIndex;
    int endIndex;
    Direction direction;
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

    public boolean isThereWinner() {
        return isThereWinner;
    }
}
