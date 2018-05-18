package com.example.tsvetelinastoyanova.tic_tac_toe.GameEngine;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.arch.persistence.room.Room;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tsvetelinastoyanova.tic_tac_toe.Board;
import com.example.tsvetelinastoyanova.tic_tac_toe.Box;
import com.example.tsvetelinastoyanova.tic_tac_toe.Database.AppDatabase;
import com.example.tsvetelinastoyanova.tic_tac_toe.Database.Player;
import com.example.tsvetelinastoyanova.tic_tac_toe.HeplerClasses.Line;
import com.example.tsvetelinastoyanova.tic_tac_toe.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class GameEngine {


    public boolean firstPlayerSymbol;
    private static final Map<Boolean, Character> variants = new HashMap<>();

    //private boolean secondPlayerSymbol;
    public boolean currentPlayerSymbol;

    protected List<Box> boardViews = new ArrayList<>();
    protected Board board;
    private boolean isFirstPlayerTurn;

    public GameEngine(){}

    public static Character getSymbolFromBooleanValue(boolean booleanValueOfSymbol){
        return variants.get(booleanValueOfSymbol);
    }

    public void initializeMapWithVariants() {
        variants.put(true, 'O');
        variants.put(false, 'X');
    }

    public boolean randomSymbolInit() {
        Random random = new Random();
        firstPlayerSymbol = random.nextBoolean();
        //secondPlayerSymbol = !firstPlayerSymbol;
        currentPlayerSymbol = firstPlayerSymbol;
        return firstPlayerSymbol;
    }

    public void addBoardViews(List<Box> boardViews) {
        this.boardViews.addAll(boardViews);
    }

    public void initNewBoard(){
        this.board = new Board(boardViews.size());
    }

    public void changePlayerTurn() {
        isFirstPlayerTurn = !isFirstPlayerTurn;
        currentPlayerSymbol = !currentPlayerSymbol;
    }

    public boolean isThereWinner() {
        Line line = board.checkIfPersonWinAndGetLineWithIndices(/*currentPlayerSymbol*/);
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
        Log.d("tag", "Execute startLineCreatingIfThereIsWinner() in GameActivity.");
        boardViews.get(line.getStartIndex()).setIsThereWinner(line.getDirection());
        boardViews.get(line.getMiddleIndex()).setIsThereWinner(line.getDirection());
        boardViews.get(line.getEndIndex()).setIsThereWinner(line.getDirection());
    }

    public void makeMoveIfBoxIsFree(Box box, int index) {
        if (!isBoxChecked(box)) {
            box.setOnTouchEvent(currentPlayerSymbol);
            personMoveAtIndex(index);
        }
    }

    private boolean isBoxChecked(Box box){
        return box.isChecked();
    }

    private void personMoveAtIndex(int index){
        board.personMove(index);
    }

    public abstract void simulateComputerMove();

}
