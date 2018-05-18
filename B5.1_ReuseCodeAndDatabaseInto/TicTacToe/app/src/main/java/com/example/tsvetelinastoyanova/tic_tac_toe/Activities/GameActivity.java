package com.example.tsvetelinastoyanova.tic_tac_toe.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tsvetelinastoyanova.tic_tac_toe.Box;
import com.example.tsvetelinastoyanova.tic_tac_toe.gameengine.GameEngine;
import com.example.tsvetelinastoyanova.tic_tac_toe.gameengine.OnePlayerGameEngine;
import com.example.tsvetelinastoyanova.tic_tac_toe.gameengine.TwoPlayersGameEngine;
import com.example.tsvetelinastoyanova.tic_tac_toe.heplerclasses.DatabaseConnector;
import com.example.tsvetelinastoyanova.tic_tac_toe.R;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    public boolean firstPlayerSymbol;

    private GameEngine gameEngine;
    private String firstPlayerName;
    private String secondPlayerName;
    private static boolean isFirstPlayerTurn;
    private boolean isTheGameOver;
    private boolean isGameForTwoPlayers;

    private TextView textViewFirst;
    private TextView textViewSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent intent = getIntent();
        initializePlayersNames(intent);
        initializeGameEngine(intent);

        symbolsInit();
        prepareScreen();
        gameEngine.initializeMapWithVariants();
        showNamesAndSymbolsForPlayers();
        initBoard();
    }

    public static boolean getWhoseTurnIs() {
        return isFirstPlayerTurn;
    }

    public void startNewGame(View view) {
        Intent startNewGameIntent = new Intent(this, GameActivity.class);
        startNewGameIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startNewGameIntent.putExtra(getString(R.string.first_player_name), firstPlayerName);
        startNewGameIntent.putExtra(getString(R.string.second_player_name), secondPlayerName);
        startActivity(startNewGameIntent);
    }

    private void prepareScreen() {
        showPlayersNames();
        setPlayerOneIsFirst();
    }

    private void initializePlayersNames(Intent intent) {
        firstPlayerName = intent.getStringExtra(getString(R.string.first_player_name));
        secondPlayerName = intent.getStringExtra(getString(R.string.second_player_name));
    }

    private void initializeGameEngine(Intent intent) {
        isGameForTwoPlayers = intent.getBooleanExtra(getString(R.string.two_players_game), false);
        if (isGameForTwoPlayers) {
            gameEngine = new TwoPlayersGameEngine();
        } else {
            gameEngine = new OnePlayerGameEngine();
        }
    }

    private void showPlayersNames() {
        textViewFirst = findViewById(R.id.first_player_name_symbol);
        textViewSecond = findViewById(R.id.second_player_name_symbol);
    }

    private void setPlayerOneIsFirst() {
        isFirstPlayerTurn = true;
        setTextStyles(Typeface.BOLD, Typeface.NORMAL);
    }

    private void setTextStyles(int styleFirst, int styleSecond) {
        textViewFirst.setTypeface(null, styleFirst);
        textViewSecond.setTypeface(null, styleSecond);
    }

    private void symbolsInit() {
        firstPlayerSymbol = gameEngine.randomSymbolInit();
    }

    private void showNamesAndSymbolsForPlayers() {
        textViewFirst.setText(firstPlayerName + ": " + gameEngine.getSymbolFromBooleanValue(firstPlayerSymbol));
        textViewSecond.setText(secondPlayerName + ": " + gameEngine.getSymbolFromBooleanValue(!firstPlayerSymbol));
        textViewFirst.setTypeface(null, Typeface.BOLD);
    }

    private void initBoard() {
        List<Box> boardViews = new ArrayList<>();
        boardViews.add(findViewById(R.id.one));
        boardViews.add(findViewById(R.id.two));
        boardViews.add(findViewById(R.id.three));
        boardViews.add(findViewById(R.id.four));
        boardViews.add(findViewById(R.id.five));
        boardViews.add(findViewById(R.id.six));
        boardViews.add(findViewById(R.id.seven));
        boardViews.add(findViewById(R.id.eight));
        boardViews.add(findViewById(R.id.nine));

        gameEngine.addBoardViews(boardViews);
        gameEngine.initNewBoard();
        setOnClickListeners(boardViews);
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
        gameEngine.changePlayerTurn();
    }

    public void setOnClickListeners(List<Box> boardViews) {
        for (int i = 0; i < boardViews.size(); i++) {
            int index = i;
            boardViews.get(i).setOnClickListener(v -> {
                Box box = (Box) v;
                checkSituationAfterMove(box, index);
            });
        }
    }

    private void checkSituationAfterMove(Box box, int index) {
        if (tryToMakeValidMove(box, index)) {
            checkShouldWeContinue();
            if (!isGameForTwoPlayers && isTheGameOver) {
                simulateMove();
                checkShouldWeContinue();
            }
        }
    }

    private boolean tryToMakeValidMove(Box box, int index) {
        return gameEngine.isMoveSuccessful(box, index);
    }

    private void simulateMove() {
        gameEngine.simulateComputerMove();
    }

    private void checkShouldWeContinue() {
        if (gameEngine.isThereWinner()) {
            isTheGameOver = true;
            if (isFirstPlayerTurn) {
                openResultViews(firstPlayerName + " wins!");
                databaseConnection(firstPlayerName, secondPlayerName);
            } else {
                openResultViews(secondPlayerName + " wins!");
                databaseConnection(secondPlayerName, firstPlayerName);
            }
        } else if (!gameEngine.areMoreMoves()) {
            isTheGameOver = true;
            openResultViews(getResources().getString(R.string.equal));
        }
        changePlayerTurn();
        changeStyles();
    }

    private void databaseConnection(String winner, String loser) {
        DatabaseConnector databaseConnector = new DatabaseConnector(winner, loser);
        databaseConnector.databaseManipulation(getApplicationContext());
    }

    private void openResultViews(String winnerText) {
        LinearLayout board = this.findViewById(R.id.board);

        board.animate().alpha(0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                board.setVisibility(LinearLayout.GONE);
                setNamesInvisible();
                initAndShowButton();
                initAndShowWinnerText(winnerText);

            }
        });
    }

    private void setNamesInvisible() {
        textViewFirst.setVisibility(LinearLayout.GONE);
        textViewSecond.setVisibility(LinearLayout.GONE);
    }

    private void initAndShowButton() {
        Button newGameButton = findViewById(R.id.new_game_button);
        newGameButton.setVisibility(Button.VISIBLE);
    }

    private void initAndShowWinnerText(String winnerText) {
        TextView textView = findViewById(R.id.winner_text);
        textView.setText(winnerText);
        textView.setVisibility(TextView.VISIBLE);
    }
}
