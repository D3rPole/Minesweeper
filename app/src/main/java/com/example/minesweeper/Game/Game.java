package com.example.minesweeper.Game;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;

import com.example.minesweeper.Physics.PhysicsScene;

public class Game {
    MineField mineField;
    public UI ui;
    boolean useFlag = false;

    private Button flagButton;
    private Button tapButton;
    private Button indicator;
    private boolean gameEnd = false;

    private PhysicsScene physicsScene;

    boolean running;

    @SuppressLint("ClickableViewAccessibility")
    public Game(int minefieldSize, float probability, ImageView view){
        mineField = new MineField(minefieldSize,minefieldSize);
        ui = new UI(1000, 1000, view);
        mineField.generateRandom(probability);

        ui.view.setOnTouchListener((v, event) -> {
            if(gameEnd) return true;
            int action = event.getAction();
            if(action == MotionEvent.ACTION_DOWN){
                runAction(event);
            }
            return true;
        });
        ui.drawMineField(mineField);
    }

    private void runAction(MotionEvent event){
        int[] converted = toMinefieldCoordinates(event.getX(), event.getY());
        if(converted.length == 0) return;
        int x = converted[0];
        int y = converted[1];

        if(useFlag){
            mineField.setFlag(x, y);
            if(mineField.checkForWin()){
                // Winning code
                gameEnd = true;
                mineField.revealAll();
                ui.drawMineField(mineField);
                ui.drawInMiddle("YOU WON!", Color.GREEN);
                return;
            }
        }else {
            if (mineField.lookAt(x, y) == -1) {
                // Losing code
                mineField.revealAll();
                gameEnd = true;

                // Create physicsScene
                physicsScene = new PhysicsScene(mineField, ui.width, ui.height);
                physicsScene.explodeAt(x, y, 1f / Config.simTPS);
                Handler handler = new Handler();
                running = true;
                new Thread(() -> {
                    // run physicsScene in new thread
                    while (running){
                        physicsScene.update(1f / Config.simTPS);
                        try {
                            Thread.sleep(1000 / Config.simTPS);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start();
                handler.post(new Runnable() {
                    // draw Physics scene in a separate renderThread
                    @Override
                    public void run() {
                        if(ui == null) return;
                        ui.drawPhysicsScene(physicsScene);
                        handler.postDelayed(this, 1000 / Config.simFPS); // Schedule next redraw
                    }
                });
                return;
            }
        }
        ui.drawMineField(mineField);
    }

    private int[] toMinefieldCoordinates(float x, float y){
        int stretchedTo = Math.min(ui.view.getWidth(), ui.view.getHeight());
        int maxDim = Math.max(ui.width, ui.height);
        int relX = (int) ((x - ui.view.getWidth() / 2) / stretchedTo * maxDim + ui.width / 2);
        int relY = (int) ((y - ui.view.getHeight() / 2) / stretchedTo * maxDim + ui.height / 2);

        if(relX < 0 || relY < 0 || relX > ui.width || relY > ui.height) return new int[0];
        return new int[]{relX / (ui.width/mineField.width), relY / (ui.width/mineField.width)};
    }

    public void setButtons(Button flag, Button tap, Button indicator){
        flagButton = flag;
        tapButton = tap;
        this.indicator = indicator;
        flagButton.setOnClickListener(v -> setFlag());
        tapButton.setOnClickListener(v -> setTap());
    }

    private void setFlag(){
        useFlag = true;
        indicator.setText("\uD83D\uDEA9");
    }

    private void setTap(){
        useFlag = false;
        indicator.setText("\uD83D\uDC47");
    }

    @SuppressLint("ClickableViewAccessibility")
    public void close(){
        ui.view.setOnTouchListener(null);
        flagButton.setOnClickListener(null);
        tapButton.setOnClickListener(null);
        ui = null;
        mineField = null;
        running = false;
    }
}
