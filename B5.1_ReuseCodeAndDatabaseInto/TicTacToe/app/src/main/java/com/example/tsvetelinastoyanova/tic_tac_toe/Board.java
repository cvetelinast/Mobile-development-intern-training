package com.example.tsvetelinastoyanova.tic_tac_toe;

import com.example.tsvetelinastoyanova.tic_tac_toe.activities.GameActivity;
import com.example.tsvetelinastoyanova.tic_tac_toe.gameengine.GameEngine;
import com.example.tsvetelinastoyanova.tic_tac_toe.heplerclasses.Direction;
import com.example.tsvetelinastoyanova.tic_tac_toe.heplerclasses.Line;

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
        boardWithSymbols.put(i, GameEngine.getSymbolFromBooleanValue(GameActivity.getWhoseTurnIs()));
        freeCellIndexes.remove(Integer.valueOf(i));
    }

    public int computerMove() {
        int i = chooseBestMove(GameEngine.getSymbolFromBooleanValue(GameActivity.getWhoseTurnIs()));
        if (i == 9) {
            i = chooseBestMove(GameEngine.getSymbolFromBooleanValue(!GameActivity.getWhoseTurnIs())); // counter attack
            if (i == 9) {
                i = randomGenerator.nextInt(freeCellIndexes.size());
            }
        }
        final int computerChoiceIndex = freeCellIndexes.get(i);
        final char computerSymbol = GameEngine.getSymbolFromBooleanValue(GameActivity.getWhoseTurnIs());
        boardWithSymbols.put(computerChoiceIndex, computerSymbol);
        freeCellIndexes.remove(i);
        return computerChoiceIndex;
    }

    private int chooseBestMove(Character symbol) {
        for (int i = 0; i < freeCellIndexes.size(); i++) {
            int computerChoiceIndex = freeCellIndexes.get(i);
            boardWithSymbols.put(computerChoiceIndex, symbol);
            if (checkIfWin(symbol)) {
                boardWithSymbols.put(computerChoiceIndex, 'N');
                return i;
            }
            boardWithSymbols.put(computerChoiceIndex, 'N');
        }
        return 9;
    }

    public Line checkIfSomeoneWinAndGetLineWithIndices() {
        if (checkIfWin(GameEngine.getSymbolFromBooleanValue(GameActivity.getWhoseTurnIs()))) {
            return lineIfWinner;
        }
        return new Line();
    }

    private boolean checkIfWin(Character playerSymbol) {
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
