package com.example.minesweeper.Game;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;

import com.example.minesweeper.Game.Physics.Scene;

public class Game {
    MineField mineField;
    public UI ui;
    boolean useFlag = false;

    private Button flagButton;
    private Button tapButton;
    private Button indicator;
    private boolean gameEnd = false;

    private Scene physicsScene;

    boolean running;

    @SuppressLint("ClickableViewAccessibility")
    public Game(int width, int height, float probability, ImageView view){
        mineField = new MineField(width,height);
        float ratio = width / (float)height;
        if(ratio > 1){
            ui = new UI(1000, (int) (1000 * (1f / ratio)), view);
        }else{
            ui = new UI((int) (1000 * ratio),1000, view);
        }
        mineField.generate(probability);

        ui.view.setOnTouchListener((v, event) -> {
            if(gameEnd) return true;
            int action = event.getAction();
            if(action == MotionEvent.ACTION_DOWN){
                int stretchedTo = Math.min(ui.view.getWidth(), ui.view.getHeight());
                int maxDim = Math.max(ui.width, ui.height);
                int relX = (int) ((event.getX() - ui.view.getWidth() / 2) / stretchedTo * maxDim + ui.width / 2);
                int relY = (int) ((event.getY() - ui.view.getHeight() / 2) / stretchedTo * maxDim + ui.height / 2);

                if(relX < 0 || relY < 0 || relX > ui.width || relY > ui.height) return true;
                int x = relX / (ui.width/mineField.width);
                int y = relY / (ui.width/mineField.width);

                if(useFlag){
                    mineField.setFlag(x, y);
                    if(mineField.checkForWin()){
                        gameEnd = true;
                        mineField.showEverything();
                        ui.drawMineField(mineField);
                        ui.drawInMiddle("YOU WON!", Color.GREEN);
                        return true;
                    }
                }else {
                    if (mineField.lookAt(x, y) != -1) {
                        Log.i("a", "you live!");
                    } else {
                        Log.i("a", "you dead!");
                        mineField.showEverything();
                        ui.drawMineField(mineField);
                        ui.drawExploded(x,y,mineField);
                        gameEnd = true;
                        physicsScene = new Scene(mineField, ui.width, ui.height);
                        physicsScene.explodeAt(x, y);
                        Handler handler = new Handler();
                        running = true;
                        new Thread(() -> {
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
                            @Override
                            public void run() {
                                if(ui == null) return;
                                ui.drawPhysicsScene(physicsScene);
                                handler.postDelayed(this, 1000 / Config.simFPS); // Schedule next redraw
                            }
                        });
                        return true;
                    }
                }
                ui.drawMineField(mineField);
            }
            return true;
        });
        ui.drawMineField(mineField);
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
