package com.example.tsvetelinastoyanova.tic_tac_toe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Board {

    private final Random randomGenerator = new Random();
    private Map<Integer, Character> boardWithSymbols = new HashMap<>();
    private List<Integer> freeCellIndexes = new ArrayList<>();
    private Line lineIfWinner;

    public Board(int size) {
        for (int i = 0; i < size; i++) {
            boardWithSymbols.put(i, 'N');
            freeCellIndexes.add(i);
        }
    }

    public boolean areMoreMoves() { // person always starts first
        return freeCellIndexes.size() > 0;
    }

    public void personMove(int i) {
        boardWithSymbols.put(i, GameActivity.variants.get(GameActivity.currentPlayerSymbol));
        freeCellIndexes.remove(Integer.valueOf(i));
    }

    public int computerMove() {
        int i = randomGenerator.nextInt(freeCellIndexes.size());
        final int computerChoiceIndex = freeCellIndexes.get(i);
        final char computerSymbol = GameActivity.variants.get(GameActivity.currentPlayerSymbol);
        boardWithSymbols.put(computerChoiceIndex, computerSymbol);
        freeCellIndexes.remove(i);
        return computerChoiceIndex;
    }

    public Line checkIfPersonWin(boolean playerSymbol) {
        if (checkIfWin(playerSymbol)) {
            return lineIfWinner;
        }
        return new Line();
    }

    public Line checkIfComputerWin() {
        if (checkIfWin(GameActivity.currentPlayerSymbol)) {
            return lineIfWinner;
        }
        return new Line();
    }

    private boolean checkIfWin(boolean symbol) {
        Character playerSymbol = GameActivity.variants.get(symbol);
        if (checkIfThereIsWinnerDiagonal(playerSymbol)) {
            return true;
        }
        for (int i = 0; i < 3; i += 1) {
            if (checkIfThereIsWinnerHorizontal(playerSymbol, i) || checkIfThereIsWinnerVertical(playerSymbol, i)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkIfThereIsWinnerHorizontal(char c, int i) {
        return checkIfThereIsWinnerAllDirections(c, 3 * i, 3 * i + 1, 3 * i + 2, Direction.HORIZONTAL);
    }

    private boolean checkIfThereIsWinnerVertical(char c, int i) {
        return checkIfThereIsWinnerAllDirections(c, i, i + 3, i + 6, Direction.VERTICAL);
    }

    private boolean checkIfThereIsWinnerDiagonal(char c) {
        if (checkIfThereIsWinnerAllDirections(c, 0, 4, 8, Direction.UPDOWNDIAGONAL)) {
            return true;
        } else {
            return (checkIfThereIsWinnerAllDirections(c, 2, 4, 6, Direction.DOWNUPDIAGONAL));
        }
    }

    private boolean checkIfThereIsWinnerAllDirections(char c, int firstIndex, int secondIndex, int thirdIndex, Direction direction) {
        if (boardWithSymbols.get(firstIndex).equals(c) && boardWithSymbols.get(secondIndex).equals(c) && boardWithSymbols.get(thirdIndex).equals(c)) {
            lineIfWinner = new Line(firstIndex, secondIndex, thirdIndex, direction);
            return true;
        }
        return false;
    }

}
