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
    private Button flagCounter;
    private boolean gameEnd = false;

    private PhysicsScene physicsScene;

    boolean running;

    /**
     * Initializes the game with a minefield, UI elements, and randomly placed mines.
     *
     * @param minefieldSize The size of the minefield (width and height).
     * @param percentage    The percentage of mine placement.
     * @param view          The ImageView used for the game UI.
     */
    @SuppressLint("ClickableViewAccessibility")
    public Game(int minefieldSize, float percentage, ImageView view){
        mineField = new MineField(minefieldSize,minefieldSize);
        ui = new UI(1000, 1000, view);
        mineField.placeRandomMines((int) (minefieldSize*minefieldSize * percentage + 0.1f));

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

    /**
     * Executes an action based on the touch event on the game UI.
     *
     * @param event The MotionEvent triggering the action.
     */
    private void runAction(MotionEvent event){
        // When touch happens
        int[] converted = toMinefieldCoordinates(event.getX(), event.getY());
        if(converted.length == 0) return;
        int x = converted[0];
        int y = converted[1];

        if(useFlag){
            // When flag tool in use
            mineField.setFlag(x, y);
            if(mineField.checkForWin()){
                // Winning code
                gameEnd = true;
                mineField.revealAll();
                updateFlagCounter();
                ui.drawMineField(mineField);
                ui.drawInMiddle("YOU WON!", Color.GREEN);
                return;
            }
        }else {
            // When tap tool in use
            if (mineField.lookAt(x, y) == -1) { // look at x,y. If its a mine you lose
                /* YOU LOST */
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
        updateFlagCounter();
        ui.drawMineField(mineField);
    }

    /**
     * Converts screen touch coordinates to minefield coordinates for interaction.
     *
     * @param x The x-coordinate of the touch.
     * @param y The y-coordinate of the touch.
     * @return An array containing the converted minefield coordinates [x, y].
     */
    private int[] toMinefieldCoordinates(float x, float y){
        int stretchedTo = Math.min(ui.view.getWidth(), ui.view.getHeight());
        int maxDim = Math.max(ui.width, ui.height);
        int relX = (int) ((x - ui.view.getWidth() / 2) / stretchedTo * maxDim + ui.width / 2);
        int relY = (int) ((y - ui.view.getHeight() / 2) / stretchedTo * maxDim + ui.height / 2);

        if(relX < 0 || relY < 0 || relX > ui.width || relY > ui.height) return new int[0];
        return new int[]{relX / (ui.width/mineField.width), relY / (ui.width/mineField.width)};
    }

    /**
     * Sets up UI buttons and updates mine count and flag count display.
     *
     * @param flag        The button for flag placement.
     * @param tap         The button for tile tapping.
     * @param indicator   The button indicating the current mode (flag or tap).
     * @param mineCount   The button displaying the total number of mines.
     * @param flagCounter The button displaying the number of flags placed.
     */
    @SuppressLint("SetTextI18n")
    public void setButtons(Button flag, Button tap, Button indicator, Button mineCount, Button flagCounter){
        flagButton = flag;
        tapButton = tap;
        mineCount.setText("\uD83D\uDCA3: " + mineField.mineCount);
        this.flagCounter = flagCounter;
        this.indicator = indicator;
        updateFlagCounter();
        flagButton.setOnClickListener(v -> setFlag());
        tapButton.setOnClickListener(v -> setTap());
    }

    /**
     * Updates the displayed count of flags based on the number of flags placed.
     */
    @SuppressLint("SetTextI18n")
    private void updateFlagCounter(){
        flagCounter.setText("\uD83D\uDEA9: " + mineField.flagCount);
    }

    /**
     * Sets the game mode to flag placement, enabling flag placement on the minefield.
     */
    private void setFlag(){
        useFlag = true;
        indicator.setText("\uD83D\uDEA9");
    }

    /**
     * Sets the game mode to tile tapping, allowing tile reveal on the minefield.
     */
    private void setTap(){
        useFlag = false;
        indicator.setText("\uD83D\uDC47");
    }

    /**
     * Cleans up game resources and UI elements for game closure or reset.
     */
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
