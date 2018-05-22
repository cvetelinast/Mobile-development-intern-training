package com.example.tsvetelinastoyanova.tic_tac_toe.gameengine;

public class SinglePlayerGameEngine extends GameEngine {

    public void simulateComputerMove() {
        boardViews.get(board.computerMove()).setOnTouchEvent(!firstPlayerSymbol);
    }
}
