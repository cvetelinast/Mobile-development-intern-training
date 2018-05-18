package com.example.tsvetelinastoyanova.tic_tac_toe.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tsvetelinastoyanova.tic_tac_toe.Box;
import com.example.tsvetelinastoyanova.tic_tac_toe.Database.AppDatabase;
import com.example.tsvetelinastoyanova.tic_tac_toe.Database.Player;
import com.example.tsvetelinastoyanova.tic_tac_toe.GameEngine.GameEngine;
import com.example.tsvetelinastoyanova.tic_tac_toe.GameEngine.OnePlayerGameEngine;
import com.example.tsvetelinastoyanova.tic_tac_toe.GameEngine.TwoPlayersGameEngine;
import com.example.tsvetelinastoyanova.tic_tac_toe.R;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    GameEngine gameEngine;
    public boolean firstPlayerSymbol;
    private String firstPlayerName;
    private String secondPlayerName;
    private static boolean isFirstPlayerTurn;
    private boolean isTheGameWon;
    private boolean twoPlayersGame;

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
        showPlayersNames();
        setPlayerOneIsFirst();
        gameEngine.initializeMapWithVariants();
        showNamesAndSymbolsForPlayers();
        initBoard();
    }

    public static boolean getWhoseTurnIs(){
        return isFirstPlayerTurn;
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

    private void initializeGameEngine(Intent intent){
        twoPlayersGame = intent.getBooleanExtra(getString(R.string.two_players_game), false);
        if(twoPlayersGame){
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

   private void symbolsInit(){
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


    public void setOnClickListeners(List<Box> boardViews ) {
        for (int i = 0; i < boardViews.size(); i++) {
            int index = i;
            boardViews.get(i).setOnClickListener(v -> {
                Box box = (Box) v;
                checkSituationAfterMove(box, index);
            });
        }
    }

    private void checkSituationAfterMove(Box box, int index) {
        makeMove(box, index);
        checkShouldWeContinue();
        if(!twoPlayersGame && !isTheGameWon) {
            simulateMove();
            checkShouldWeContinue();
        }
    }

    private void checkShouldWeContinue() {
        if (gameEngine.isThereWinner()) {
            isTheGameWon = true;
            if (isFirstPlayerTurn) {
                openResultViews(firstPlayerName);
                databaseManipulation(firstPlayerName, secondPlayerName);
            } else {
                openResultViews(secondPlayerName);
                databaseManipulation(secondPlayerName, firstPlayerName);
            }
        } else if (!gameEngine.areMoreMoves()) {
            openResultViews(getResources().getString(R.string.equal));
        }
        changePlayerTurn();
        changeStyles();
    }

    private void makeMove(Box box, int index) {
        gameEngine.makeMoveIfBoxIsFree(box, index);
    }

    private void simulateMove() {
        gameEngine.simulateComputerMove();
    }

    private void openResultViews(String winner) {
        LinearLayout board = this.findViewById(R.id.board);

        board.animate().alpha(0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                board.setVisibility(LinearLayout.GONE);
                textViewFirst.setVisibility(LinearLayout.GONE);
                textViewSecond.setVisibility(LinearLayout.GONE);
                initButton();
                showWinnerText(winner);

            }
        });
    }

    private void showWinnerText(String winner) {
        TextView textView = findViewById(R.id.winner_text);
        textView.setText(winner + " wins!");
        textView.setVisibility(TextView.VISIBLE);
    }

    private void initButton() {
        Button newGameButton = findViewById(R.id.new_game_button);
        newGameButton.setVisibility(Button.VISIBLE);
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
