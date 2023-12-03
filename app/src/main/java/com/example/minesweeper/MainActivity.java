package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.minesweeper.Game.Game;

public class MainActivity extends AppCompatActivity {
    Spinner width;
    Spinner height;
    Spinner diff;
    Button start;

    float[] probabilities = new float[]{
            0.05f, // very easy
            0.1f,  // easy
            0.2f,  // mid
            0.3f,  // hard
            0.4f,  // expert
            0.5f   //expert+ ?
    };

    Game game;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //game = new Game(findViewById(R.id.gameView));
        initiateMenu();
        startFirstGame();
    }

    @SuppressLint("SetTextI18n")
    private void initiateMenu(){
        width = findViewById(R.id.widthSpinner);
        height = findViewById(R.id.heightSpinner);
        diff = findViewById(R.id.diffSpinner);

        String[] options = new String[16];
        for (int i = 0; i < 16; i++) {
            options[i] = String.valueOf(i + 5);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        width.setAdapter(adapter);
        width.setSelection(5);
        height.setAdapter(adapter);
        height.setSelection(5);

        String[] diffOptions = new String[]{"\uD83D\uDE01 VERY EASY",
                "\uD83D\uDE00 EASY",
                "\uD83D\uDE42 MID",
                "\uD83D\uDE10 HARD",
                "☹️ EXPERT",
                "\uD83D\uDE31\uD83D\uDE2D\uD83D\uDE2D\uD83D\uDE29\uD83D\uDE29"};
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, diffOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diff.setAdapter(adapter);
        diff.setSelection(0);

        //Buttons
        start = findViewById(R.id.startGameButton);
        start.setOnClickListener(v -> {
            if(game != null){
                game.close();
            }
            game = new Game(  width.getSelectedItemPosition() + 5,
                             height.getSelectedItemPosition() + 5,
                                   probabilities[diff.getSelectedItemPosition()],
                                   findViewById(R.id.gameView));
            game.setButtons(findViewById(R.id.flagButton), findViewById(R.id.tapButton), findViewById(R.id.flagTapIndicator));
        });
    }

    public void startFirstGame(){
        game = new Game(  width.getSelectedItemPosition() + 5,
                height.getSelectedItemPosition() + 5,
                probabilities[diff.getSelectedItemPosition()],
                findViewById(R.id.gameView));
        game.setButtons(findViewById(R.id.flagButton), findViewById(R.id.tapButton), findViewById(R.id.flagTapIndicator));
    }
}