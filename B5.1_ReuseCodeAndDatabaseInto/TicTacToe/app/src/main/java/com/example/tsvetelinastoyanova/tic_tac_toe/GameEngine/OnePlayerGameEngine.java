package com.example.tsvetelinastoyanova.tic_tac_toe.gameengine;

public class OnePlayerGameEngine extends GameEngine {
    public OnePlayerGameEngine(){}

    public void simulateComputerMove() {
        boardViews.get(board.computerMove()).setOnTouchEvent(!firstPlayerSymbol);
    }
}
