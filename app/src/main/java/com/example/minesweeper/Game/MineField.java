package com.example.minesweeper.Game;

import android.util.Log;

import java.util.Random;

public class MineField {
    public boolean[][] field;
    public boolean[][] lookedAt;
    public boolean[][] flagged;
    public int width;
    public int height;
    int mineCount = 0;
    int flagCount = 0;
    boolean firstLook = true;

    MineField(int width, int height){
        field = new boolean[width][height];
        lookedAt = new boolean[width][height];
        flagged = new boolean[width][height];
        this.width = width;
        this.height = height;
    }

    public void placeRandomMines(int amount){
        Log.i("",""+amount);
        for (int i = 0; i < amount; i++) {
            placeRandomMine();
        }
    }

    private void placeRandomMine(){
        Random random = new Random();
        int x,y;
        do{
            x = random.nextInt(width);
            y = random.nextInt(width);
        }while(field[x][y]);
        mineCount++;
        field[x][y] = true;
    }

    public void revealAll(){
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                lookedAt[i][j] = true;
            }
        }
    }

    public int lookAt(int x, int y){
        if(x < 0 || y < 0 || x >= width || y >= height || lookedAt[x][y]) return 0;
        if(firstLook){
            if(field[x][y]){
                // Erster klick darf keine Miene sein. Wenn es eine ist, wird diese verschoben.
                placeRandomMine();
                field[x][y] = false;
            }
            firstLook = false;
        }
        lookedAt[x][y] = true;
        if(flagged[x][y]){
            flagged[x][y] = false;
            flagCount--;
        }
        if(field[x][y]){
            return -1;
        }
        int neighbours = checkNeighbours(x,y);
        if(checkNeighbours(x,y) == 0){
            lookAt(x+1, y);
            lookAt(x-1, y);
            lookAt(x, y+1);
            lookAt(x, y-1);
        }
        return neighbours;
    }

    public void setFlag(int x, int y){
        if(lookedAt[x][y]){
            if(flagged[x][y]){
                flagged[x][y] = false;
                flagCount--;
            }
            return;
        }
        if(flagged[x][y]){
            flagged[x][y] = false;
            flagCount--;
        }else{
            flagged[x][y] = true;
            flagCount++;
        }
    }

    boolean checkForWin(){
        int rightGuessCount = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                boolean isMine = field[i][j];
                boolean isFlagged = flagged[i][j];
                if(isMine && isFlagged){
                    rightGuessCount++;
                }else if(isFlagged){
                    rightGuessCount--;
                }
            }
        }
        return rightGuessCount == mineCount;
    }

    public int checkNeighbours(int x, int y){
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            if(x+i < 0 || x+i >= width) continue;
            for (int j = -1; j <= 1; j++) {
                if(y+j < 0 || y+j >= height) continue;
                if(field[x+i][y+j]){
                    count++;
                }
            }
        }
        return count;
    }
}
