package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.minesweeper.Game.Config;
import com.example.minesweeper.Game.Game;

public class MainActivity extends AppCompatActivity {
    private Spinner widthSpinner;
    private Spinner difficultySpinner;
    private Button flagButton;
    private Button tapButton;
    private Button flagTapIndicator;
    private Button mineCountIndicator;
    private Button flagCountIndicator;

    private final SensorEventListener accelerometerListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float xAxis = event.values[0];
            float yAxis = event.values[1];

            Config.gravity.x = -xAxis * Config.gravityConstant;
            Config.gravity.y = yAxis * Config.gravityConstant;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };

    // percent of fields that are mines, per difficulty
    float[] percent = new float[]{
            0.04f, // very easy
            0.08f,  // easy
            0.12f,  // mid
            0.16f,  // hard
            0.20f,  // expert
            0.24f   //expert+ ?
    };

    Game game;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // initiate accelerometer for physics simulation
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(accelerometerListener, accelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST);



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiateMenu();
        startFirstGame();
    }

    @SuppressLint("SetTextI18n")
    private void initiateMenu(){
        // values are given to the spinners
        widthSpinner = findViewById(R.id.widthSpinner);
        difficultySpinner = findViewById(R.id.diffSpinner);

        String[] options = new String[11];
        for (int i = 0; i < 11; i++) {
            options[i] = String.valueOf(i + 5);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        widthSpinner.setAdapter(adapter);
        widthSpinner.setSelection(5);

        String[] diffOptions = new String[]{"\uD83D\uDE01 VERY EASY",
                "\uD83D\uDE00 EASY",
                "\uD83D\uDE42 MID",
                "\uD83D\uDE10 HARD",
                "☹️ EXPERT",
                "\uD83D\uDE31\uD83D\uDE2D\uD83D\uDE2D\uD83D\uDE29\uD83D\uDE29"};

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, diffOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(adapter);
        difficultySpinner.setSelection(0);

        //Get all Buttons
        flagButton = findViewById(R.id.flagButton);
        tapButton = findViewById(R.id.tapButton);
        flagTapIndicator = findViewById(R.id.flagTapIndicator);
        mineCountIndicator = findViewById(R.id.mineCount);
        flagCountIndicator = findViewById(R.id.flagCounter);

        //Give start button its function
        Button startButton = findViewById(R.id.startGameButton);
        startButton.setOnClickListener(v -> {
            if(game != null){
                game.close();
            }
            // Create Game
            game = new Game(  widthSpinner.getSelectedItemPosition() + 5,
                                   percent[difficultySpinner.getSelectedItemPosition()],
                                   findViewById(R.id.gameView));
            // Give buttons to Game object
            game.setButtons(flagButton, tapButton, flagTapIndicator, mineCountIndicator, flagCountIndicator);
        });

    }

    public void startFirstGame(){
        game = new Game(  widthSpinner.getSelectedItemPosition() + 5,
                percent[difficultySpinner.getSelectedItemPosition()],
                findViewById(R.id.gameView));
        game.setButtons(flagButton, tapButton, flagTapIndicator, mineCountIndicator, flagCountIndicator);
    }
}