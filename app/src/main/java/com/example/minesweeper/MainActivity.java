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

    private final SensorEventListener accelerometerListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float xAxis = event.values[0];
            float yAxis = event.values[1];

            Config.gravity.x = -xAxis * Config.gravityConstant;
            Config.gravity.y = yAxis * Config.gravityConstant;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Handle accuracy changes if needed
        }
    };

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

        //Buttons
        Button startButton = findViewById(R.id.startGameButton);
        startButton.setOnClickListener(v -> {
            if(game != null){
                game.close();
            }
            game = new Game(  widthSpinner.getSelectedItemPosition() + 5,
                                   probabilities[difficultySpinner.getSelectedItemPosition()],
                                   findViewById(R.id.gameView));
            game.setButtons(findViewById(R.id.flagButton), findViewById(R.id.tapButton), findViewById(R.id.flagTapIndicator));
        });

        flagButton = findViewById(R.id.flagButton);
        tapButton = findViewById(R.id.tapButton);
        flagTapIndicator = findViewById(R.id.flagTapIndicator);
    }

    public void startFirstGame(){
        game = new Game(  widthSpinner.getSelectedItemPosition() + 5,
                probabilities[difficultySpinner.getSelectedItemPosition()],
                findViewById(R.id.gameView));
        game.setButtons(flagButton, tapButton, flagTapIndicator);
    }
}