package com.example.minesweeper.Game;

import android.util.Log;

import java.util.Random;

public class MineField {
    boolean[][] field;
    boolean[][] lookedAt;
    boolean[][] flagged;
    int width;
    int height;
    int mineCount = 0;

    MineField(int width, int height){
        field = new boolean[width][height];
        lookedAt = new boolean[width][height];
        flagged = new boolean[width][height];
        this.width = width;
        this.height = height;
    }

    public void generate(float p){
        Random random = new Random();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                field[i][j] = random.nextFloat() < p;
                if(field[i][j]) mineCount++;
                lookedAt[i][j] = false;
                flagged[i][j] = false;
            }
        }
    }

    public void showEverything(){
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                lookedAt[i][j] = true;
            }
        }
    }

    public int lookAt(int x, int y){
        if(x < 0 || y < 0 || x >= width || y >= height || lookedAt[x][y]) return 0;
        lookedAt[x][y] = true;
        flagged[x][y] = false;
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
            flagged[x][y] = false;
            return;
        }
        flagged[x][y] = !flagged[x][y];
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
