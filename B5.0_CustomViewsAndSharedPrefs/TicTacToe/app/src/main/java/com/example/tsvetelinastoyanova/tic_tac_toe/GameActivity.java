package com.example.tsvetelinastoyanova.tic_tac_toe;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    public static boolean personSymbol;
    public static final Map<Boolean, Character> variants = new HashMap<>();

    private List<Box> boardViews = new ArrayList<>();
    private Board board;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initializeMapWithVariants();
        randomSymbolInit();
        initBoard();
    }

    public void startNewGame(View view) {
        Intent startNewGameIntent = new Intent(this, GameActivity.class);
        startNewGameIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(startNewGameIntent);
    }

    private void initializeMapWithVariants() {
        variants.put(true, 'O');
        variants.put(false, 'X');
    }

    private void randomSymbolInit() {
        TextView view = findViewById(R.id.symbol);
        Random random = new Random();
        personSymbol = random.nextBoolean();
        view.setText(variants.get(personSymbol).toString());
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

        board = new Board(boardViews.size());
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        for (int i = 0; i < boardViews.size(); i++) {
            int index = i;
            boardViews.get(i).setOnClickListener(v -> {
                Box box = (Box) v;
                personOnActionClick(box, index);
            });
        }
    }

    private void personOnActionClick(Box box, int index) {
        if (!box.isChecked()) {
            box.setOnTouchEvent(personSymbol);
            board.personMove(index);
            if (checkSituationAfterPersonMove()) {
                simulateComputerMove();
            }
        }
    }

    private boolean checkSituationAfterPersonMove() {
        Line line = board.checkIfPersonWin();
        if (line.isThereWinner()) {
            Log.d("tag", "person win");
            startLineCreatingIfThereIsWinner(line);
            openResultViews(getResources().getString(R.string.win));
            return false;
        } else if (!board.areMoreMoves()) {
            Log.d("tag", "equal");
            openResultViews(getResources().getString(R.string.equal));
            return false;
        }
        return true;
    }

    private void simulateComputerMove() {
        boardViews.get(board.computerMove()).setOnTouchEvent(!personSymbol);
        Line line = board.checkIfComputerWin();
        if (line.isThereWinner()) {
            Log.d("tag", "computer win");
            startLineCreatingIfThereIsWinner(line);
            openResultViews(getResources().getString(R.string.lose));
        }
    }

    private void startLineCreatingIfThereIsWinner(Line line) {
        Log.d("tag", "Execute startLineCreatingIfThereIsWinner() in GameActivity.");
        boardViews.get(line.startIndex).setIsThereWinner(line.direction);
        boardViews.get(line.middleIndex).setIsThereWinner(line.direction);
        boardViews.get(line.endIndex).setIsThereWinner(line.direction);
    }

    private void openResultViews(String result) {
        Log.d("tag", "Execute openResultViews() in GameActivity.");
        this.result = result;
        LinearLayout board = this.findViewById(R.id.board);
        LinearLayout symbolField = this.findViewById(R.id.symbol_field);

        board.animate().alpha(0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                board.setVisibility(LinearLayout.GONE);
                symbolField.setVisibility(LinearLayout.GONE);

                initPicture();
                initButton();
            }
        });
    }

    private void initPicture() {
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
}
