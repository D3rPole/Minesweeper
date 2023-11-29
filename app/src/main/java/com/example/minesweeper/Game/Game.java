package com.example.minesweeper.Game;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;

public class Game {
    MineField mineField;
    public UI ui;
    boolean useFlag = false;

    private Button flagButton;
    private Button tapButton;

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
            int action = event.getAction();
            if(action == MotionEvent.ACTION_DOWN){
                int stretchedTo = Math.min(ui.view.getWidth(), ui.view.getHeight());
                int maxDim = Math.max(ui.width, ui.height);
                int relX = (int) ((event.getX() - ui.view.getWidth() / 2) / stretchedTo * maxDim + ui.width / 2);
                int relY = (int) ((event.getY() - ui.view.getHeight() / 2) / stretchedTo * maxDim + ui.height / 2);
                Log.i("a", relX + " " + relY);
                if(relX < 0 || relY < 0 || relX > ui.width || relY > ui.height) return true;
                int x = relX / (ui.width/mineField.width);
                int y = relY / (ui.width/mineField.width);

                if(useFlag){
                    mineField.setFlag(x, y);
                }else {
                    if (mineField.lookAt(x, y) != -1) {
                        Log.i("a", "you live!");
                    } else {
                        Log.i("a", "you dead!");
                    }
                }
                ui.drawMineField(mineField);
            }
            return true;
        });
        ui.drawMineField(mineField);
    }

    public void setButtons(Button flag, Button tap){
        flagButton = flag;
        tapButton = tap;
        flagButton.setOnClickListener(v -> setFlag());
        tapButton.setOnClickListener(v -> setTap());
    }

    private void setFlag(){
        useFlag = true;
    }

    private void setTap(){
        useFlag = false;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void close(){
        ui.view.setOnTouchListener(null);
        flagButton.setOnClickListener(null);
        tapButton.setOnClickListener(null);
        ui = null;
        mineField = null;
    }
}
