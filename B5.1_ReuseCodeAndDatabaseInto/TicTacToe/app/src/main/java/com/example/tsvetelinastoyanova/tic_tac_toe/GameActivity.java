package com.example.tsvetelinastoyanova.tic_tac_toe;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tsvetelinastoyanova.tic_tac_toe.Database.AppDatabase;
import com.example.tsvetelinastoyanova.tic_tac_toe.Database.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    // GameEngine gameEngine;

    public boolean firstPlayerSymbol;
    public static final Map<Boolean, Character> variants = new HashMap<>();

    private boolean secondPlayerSymbol;
    public static boolean currentPlayerSymbol;

    private List<Box> boardViews = new ArrayList<>();
    private Board board;
    private String result;

    private String firstPlayerName;
    private String secondPlayerName;
    private boolean isFirstPlayerTurn;

    private TextView textViewFirst;
    private TextView textViewSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent intent = getIntent();
        initializePlayersNames(intent);

        randomSymbolInit();

        showPlayersNames();
        setIsPlayerOneFirst();

        initializeMapWithVariants();

        showNamesAndSymbolsForPlayers();
        initBoard();
    }

    public void startNewGame(View view) {
        Intent startNewGameIntent = new Intent(this, GameActivity.class);
        startNewGameIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startNewGameIntent.putExtra(getString(R.string.first_player_name), firstPlayerName);
        startNewGameIntent.putExtra(getString(R.string.second_player_name), secondPlayerName);
        startActivity(startNewGameIntent);
    }

    private void initializePlayersNames(Intent intent) {
        firstPlayerName = intent.getStringExtra(getString(R.string.first_player_name));
        secondPlayerName = intent.getStringExtra(getString(R.string.second_player_name));
    }

    private void showPlayersNames() {
        textViewFirst = findViewById(R.id.first_player_name_symbol);
        textViewSecond = findViewById(R.id.second_player_name_symbol);
    }

    private void setIsPlayerOneFirst() {
        isFirstPlayerTurn = true;
        setTextStyles(Typeface.BOLD, Typeface.NORMAL);
    }

    private void setTextStyles(int styleFirst, int styleSecond) {
        textViewFirst.setTypeface(null, styleFirst);
        textViewSecond.setTypeface(null, styleSecond);
    }

    private void initializeMapWithVariants() {
        variants.put(true, 'O');
        variants.put(false, 'X');
    }

    private void randomSymbolInit() {
        Random random = new Random();
        firstPlayerSymbol = random.nextBoolean();
        secondPlayerSymbol = !firstPlayerSymbol;
        currentPlayerSymbol = firstPlayerSymbol;
    }

    private void showNamesAndSymbolsForPlayers() {
        textViewFirst.setText(firstPlayerName + ": " + variants.get(firstPlayerSymbol).toString());//gameEngine.getSymbol(firstPlayerSymbol).toString());
        textViewSecond.setText(secondPlayerName + ": " + variants.get(secondPlayerSymbol).toString());// gameEngine.getSymbol(!firstPlayerSymbol).toString());
        textViewFirst.setTypeface(null, Typeface.BOLD);
    }

    private void initBoard() {
        boardViews.add(findViewById(R.id.one));
        boardViews.add(findViewById(R.id.two));
        boardViews.add(findViewById(R.id.three));
        boardViews.add(findViewById(R.id.four));
        boardViews.add(findViewById(R.id.five));
        boardViews.add(findViewById(R.id.six));
        boardViews.add(findViewById(R.id.seven));
        boardViews.add(findViewById(R.id.eight));
        boardViews.add(findViewById(R.id.nine));

        // gameEngine.makeBoard(boardViews);
        board = new Board(boardViews.size());
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        for (int i = 0; i < boardViews.size(); i++) {
            int index = i;
            boardViews.get(i).setOnClickListener(v -> {
                Box box = (Box) v;
                checkSituationAfterMove(box, index);
            });
        }
    }

    private void changeStyles() {
        if (isFirstPlayerTurn) {
            setTextStyles(Typeface.BOLD, Typeface.NORMAL);
        } else {
            setTextStyles(Typeface.NORMAL, Typeface.BOLD);
        }
    }

    private void changePlayerTurn() {
        isFirstPlayerTurn = !isFirstPlayerTurn;
        currentPlayerSymbol = !currentPlayerSymbol;
    }

    private void checkSituationAfterMove(Box box, int index) {
        if (!box.isChecked()) {
            box.setOnTouchEvent(currentPlayerSymbol);
            board.personMove(index);
            boolean isWinner = isThereWinner();
            if (!isWinner && areMoreMoves()) {// gameEngine.areMoreMoves()) {
                board.personMove(index);
                changePlayerTurn();
                changeStyles();
                // simulateComputerMove();
            } else if (isWinner) {
                Line line = board.checkIfPersonWin(currentPlayerSymbol);
                startLineCreatingIfThereIsWinner(line);

                if (isFirstPlayerTurn) {
                    openResultViews(/*getResources().getString(R.string.win)*/ firstPlayerName);
                    databaseManipulation(firstPlayerName, secondPlayerName);
                } else {
                    openResultViews(/*getResources().getString(R.string.win)*/ secondPlayerName);
                    databaseManipulation(secondPlayerName, firstPlayerName);
                }
            }
        }
    }


    private boolean isThereWinner() {
        Line line = board.checkIfPersonWin(currentPlayerSymbol);
        if (line.isThereWinner()) {
            Log.d("tag", "person win");
            startLineCreatingIfThereIsWinner(line);
            openResultViews(getResources().getString(R.string.win));
            return true;
        }
        return false;
    }

    private boolean areMoreMoves() {
        if (!board.areMoreMoves()) {
            Log.d("tag", "equal");
            openResultViews(getResources().getString(R.string.equal));
            return false;
        }
        return true;
    }

   /* private void simulateComputerMove() {
        boardViews.get(board.computerMove()).setOnTouchEvent(!firstPlayerSymbol);
        Line line = board.checkIfComputerWin();
        if (line.isThereWinner()) {
            Log.d("tag", "computer win");
            startLineCreatingIfThereIsWinner(line);
            openResultViews(getResources().getString(R.string.lose));
        }
    }*/

    private void startLineCreatingIfThereIsWinner(Line line) {
        Log.d("tag", "Execute startLineCreatingIfThereIsWinner() in GameActivity.");
        boardViews.get(line.startIndex).setIsThereWinner(line.direction);
        boardViews.get(line.middleIndex).setIsThereWinner(line.direction);
        boardViews.get(line.endIndex).setIsThereWinner(line.direction);
    }

    private void openResultViews(/*String result*/String winner) {
        Log.d("tag", "Execute openResultViews() in GameActivity.");
        //  this.result = result;
        LinearLayout board = this.findViewById(R.id.board);

        board.animate().alpha(0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                board.setVisibility(LinearLayout.GONE);
                textViewFirst.setVisibility(LinearLayout.GONE);
                textViewSecond.setVisibility(LinearLayout.GONE);
                // initPicture();
                initButton();
                showWinnerText(winner);

            }
        });
    }

    /*private void initPicture() {
        ImageView imageview = findViewById(R.id.image_view);
        imageview.setVisibility(ImageView.VISIBLE);
        if (result.equals(getResources().getString(R.string.win))) {
            imageview.setImageDrawable(getResources().getDrawable(R.drawable.win));
            changeStatistics(result);
        } else if (result.equals(getResources().getString(R.string.lose))) {
            imageview.setImageDrawable(getResources().getDrawable(R.drawable.lose));
            changeStatistics(result);
        } else {
            imageview.setImageDrawable(getResources().getDrawable(R.drawable.equal));
        }
    }*/

    private void showWinnerText(String winner) {
        TextView textView = findViewById(R.id.winner_text);
        textView.setText(winner + " wins!");
        textView.setVisibility(TextView.VISIBLE);
    }


    private void initButton() {
        Button newGameButton = findViewById(R.id.new_game_button);
        newGameButton.setVisibility(Button.VISIBLE);
    }

    private void changeStatistics(String s) {
        SharedPreferences preferences = getSharedPreferences("WinsNLoses", 0);
        SharedPreferences.Editor editor = preferences.edit();
        int currentValue = preferences.getInt(s, 0);
        editor.putInt(s, currentValue + 1);
        editor.apply();
    }

    private void databaseManipulation(String winner, String loser) {
        new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "players").build();
            if (db.userDao().getUserWithThisName(winner) == 0) {
                db.userDao().insertPlayer(new Player(winner, 0, 0));
            }
            db.userDao().incrementNumWinsOfPlayer(winner);

            if (db.userDao().getUserWithThisName(loser) == 0) {
                db.userDao().insertPlayer(new Player(loser, 0, 0));
            }
            db.userDao().decrementNumLosesOfPlayer(loser);
        }
        ).start();
    }
}
