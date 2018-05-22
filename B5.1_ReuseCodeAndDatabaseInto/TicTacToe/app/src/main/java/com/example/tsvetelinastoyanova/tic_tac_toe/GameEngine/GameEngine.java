package com.example.tsvetelinastoyanova.tic_tac_toe.gameengine;

import com.example.tsvetelinastoyanova.tic_tac_toe.Board;
import com.example.tsvetelinastoyanova.tic_tac_toe.Box;
import com.example.tsvetelinastoyanova.tic_tac_toe.heplerclasses.Line;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameEngine {

    public boolean firstPlayerSymbol;
    private static final Map<Boolean, Character> variants = new HashMap<>();

    public boolean currentPlayerSymbol;

    protected List<Box> boardViews = new ArrayList<>();
    protected Board board;
    private boolean isFirstPlayerTurn;

    public static Character getSymbolFromBooleanValue(boolean booleanValueOfSymbol) {
        return variants.get(booleanValueOfSymbol);
    }

    public void initializeMapWithVariants() {
        variants.put(true, 'O');
        variants.put(false, 'X');
    }

    public boolean randomSymbolInit() {
        Random random = new Random();
        firstPlayerSymbol = random.nextBoolean();
        currentPlayerSymbol = firstPlayerSymbol;
        return firstPlayerSymbol;
    }

    public void addBoardViews(List<Box> boardViews) {
        this.boardViews.addAll(boardViews);
    }

    public void initNewBoard() {
        this.board = new Board(boardViews.size());
    }

    public void changePlayerTurn() {
        isFirstPlayerTurn = !isFirstPlayerTurn;
        currentPlayerSymbol = !currentPlayerSymbol;
    }

    public boolean isThereWinner() {
        Line line = board.checkIfSomeoneWinAndGetLineWithIndices();
        if (line.isThereWinner()) {
            startLineCreatingIfThereIsWinner(line);
            return true;
        }
        return false;
    }

    public boolean areMoreMoves() {
        if (!board.areMoreMoves()) {
            return false;
        }
        return true;
    }

    private void startLineCreatingIfThereIsWinner(Line line) {
        boardViews.get(line.getStartIndex()).setIsThereWinner(line.getDirection());
        boardViews.get(line.getMiddleIndex()).setIsThereWinner(line.getDirection());
        boardViews.get(line.getEndIndex()).setIsThereWinner(line.getDirection());
    }

    public boolean isMoveSuccessful(Box box, int index) {
        if (!isBoxChecked(box)) {
            box.setOnTouchEvent(currentPlayerSymbol);
            personMoveAtIndex(index);
            return true;
        }
        return false;
    }

    private boolean isBoxChecked(Box box) {
        return box.isChecked();
    }

    private void personMoveAtIndex(int index) {
        board.personMove(index);
    }

    public void simulateComputerMove(){}

}
