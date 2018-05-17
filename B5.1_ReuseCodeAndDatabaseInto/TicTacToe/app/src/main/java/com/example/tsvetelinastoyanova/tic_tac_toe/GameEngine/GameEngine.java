package com.example.tsvetelinastoyanova.tic_tac_toe.GameEngine;

import android.graphics.Typeface;
import android.util.Log;

import com.example.tsvetelinastoyanova.tic_tac_toe.Board;
import com.example.tsvetelinastoyanova.tic_tac_toe.Box;
import com.example.tsvetelinastoyanova.tic_tac_toe.Line;
import com.example.tsvetelinastoyanova.tic_tac_toe.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class GameEngine {

   /* public boolean firstPlayerSymbol;
    public static final Map<Boolean, Character> variants = new HashMap<>();

    private boolean secondPlayerSymbol;
    public static boolean currentPlayerSymbol;
    private List<Box> boardViews = new ArrayList<>();
    private Board board;
    private String result;

    private String firstPlayerName;
    private String secondPlayerName;
    private boolean isFirstPlayerTurn;

    GameEngine(String firstPlayerName, String secondPlayerName, boolean firstPlayerSymbol){
        this.firstPlayerName = firstPlayerName;
        this.secondPlayerName = secondPlayerName;
        this.firstPlayerSymbol = firstPlayerSymbol;
        initializeMapWithVariants();
        secondPlayerSymbolInit();

       // setNamesAndSymbolsForPlayers();
      //  initBoard();
    }

    public Character getSymbol(boolean symbol){
        return variants.get(symbol);
    }

   *//* public void makeBoard(List<Box> views){
        boardViews = views;
        board = new Board(boardViews.size());
        setOnClickListeners();
    }*//*

    private void initializeMapWithVariants() {
        variants.put(true, 'O');
        variants.put(false, 'X');
    }

    private void secondPlayerSymbolInit() {
       // Random random = new Random();
       // firstPlayerSymbol = random.nextBoolean();
        secondPlayerSymbol = !firstPlayerSymbol;
        currentPlayerSymbol = firstPlayerSymbol;
    }

    *//*private void setNamesAndSymbolsForPlayers() {
        textViewFirst.setText(firstPlayerName + ": " + variants.get(firstPlayerSymbol).toString());
        textViewSecond.setText(secondPlayerName + ": " + variants.get(secondPlayerSymbol).toString());
        textViewFirst.setTypeface(null, Typeface.BOLD);
    }*//*

    *//*private void initBoard() {
        boardViews.add(findViewById(R.id.one));
        boardViews.add(findViewById(R.id.two));
        boardViews.add(findViewById(R.id.three));
        boardViews.add(findViewById(R.id.four));
        boardViews.add(findViewById(R.id.five));
        boardViews.add(findViewById(R.id.six));
        boardViews.add(findViewById(R.id.seven));
        boardViews.add(findViewById(R.id.eight));
        boardViews.add(findViewById(R.id.nine));

        board = new Board(boardViews.size());
        setOnClickListeners();
    }*//*

   *//* private void setOnClickListeners() {
        for (int i = 0; i < boardViews.size(); i++) {
            int index = i;
            boardViews.get(i).setOnClickListener(v -> {
                Box box = (Box) v;
                personOnActionClick(box, index);
            });
        }
    }*//*

    public boolean personOnActionClick(Box box, int index) {
        if (!box.isChecked()) {
            box.setOnTouchEvent(currentPlayerSymbol);
            board.personMove(index);
            return true;
        }
        return false;
    }

    *//*public void checkIfGameShouldContinue(Box box, int index){
           // boolean isWinner = isThereWinner();
            if (!isWinner && areMoreMoves()) {
                //board.personMove(index);
                changePlayerTurn();
                changeStyles();
                // simulateComputerMove();
            } else if (isWinner) {
                if (isFirstPlayerTurn) {
                    databaseManipulation(firstPlayerName, secondPlayerName);
                } else {
                    databaseManipulation(secondPlayerName, firstPlayerName);
                }
            }
        }
    }*//*

   *//* public boolean isThereWinner() {
        Line line = board.checkIfPersonWin(currentPlayerSymbol);
        if (line.isThereWinner()) {
            Log.d("tag", "person win");
            *//**//*startLineCreatingIfThereIsWinner(line);
            openResultViews(getResources().getString(R.string.win));*//**//*
            return true;
        }
        return false;
    }*//*

    *//*public boolean areMoreMoves() {
        if (!board.areMoreMoves()) {
            Log.d("tag", "equal");
            openResultViews(getResources().getString(R.string.equal));
            return false;
        }
        return true;
    }*/

}
