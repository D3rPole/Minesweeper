package com.example.minesweeper.Physics;

import android.graphics.Rect;

import com.example.minesweeper.Game.Config;
import com.example.minesweeper.Game.MineField;

import java.util.HashSet;
import java.util.LinkedList;

public class PhysicsScene {
    public PhysicsObject[] objects;
    private final float xDist;
    private final float yDist;
    private final Rect simulationRect;
    private LinkedList<PhysicsObject>[][] grid;
    private final int gridWidth = 10;
    private final int gridHeight = 10;

    public PhysicsScene(MineField mineField, int uiWidth, int uiHeight){
        simulationRect = new Rect(-100,-100, (int) ((float) uiWidth + 100), (int) ((float) uiHeight + 100));
        xDist = (float) uiWidth / mineField.width;
        yDist = (float) uiHeight / mineField.height;
        objects = new PhysicsObject[mineField.height * mineField.width];
        for (int y = 0; y < mineField.height; y++) {
            for (int x = 0; x < mineField.width; x++) {
                PhysicsObject object = new PhysicsObject(new Vec2(xDist * x + xDist / 2,yDist * y + yDist / 2));
                object.flagged = mineField.flagged[x][y];
                object.lookedAt = mineField.lookedAt[x][y];
                object.isMine = mineField.field[x][y];
                object.neighbours = mineField.checkNeighbours(x,y);
                object.width = xDist - 1;
                object.height = yDist - 1;
                objects[y * mineField.width + x] = object;
            }
        }
    }

    private void updateGrid(){
        grid = new LinkedList[gridWidth][gridHeight];
        for(PhysicsObject object : objects){
            if(object.explosionSource) continue;
            int x =  (int) (gridWidth*(object.pos.x - simulationRect.left)/simulationRect.width());
            int y = (int) (gridHeight*(object.pos.y - simulationRect.top)/simulationRect.height());
            if(x < 0 || x >= gridWidth || y < 0 || y >= gridHeight) continue;
            if(grid[x][y] == null){
                grid[x][y] = new LinkedList<>();
            }
            grid[x][y].add(object);
        }
    }

    public void explodeAt(int x, int y, float dTime){
        Vec2 pos = new Vec2(x * xDist + xDist / 2,y * yDist + yDist / 2);
        for(PhysicsObject object : objects){
            float dist = pos.distance(object.pos);
            if(dist == 0){
                object.explosionSource = true;
                continue;
            }
            object.addVel(object.pos.subtract(pos).multiply(1000f / dist), dTime);
            object.addVel(Vec2.random(-30,30), dTime);
        }
    }

    public void update(float dTime){
        updateGrid();

        // movement
        for(PhysicsObject object : objects){
            if(object.explosionSource) continue;
            object.update(dTime, Config.gravity);
        }

        // collision with world boundaries
        for(PhysicsObject object : objects){
            object.clampInBoundingbox(simulationRect);
        }

        // collision to each-other
        for(PhysicsObject object : objects){
            if(object.explosionSource) continue;
            int x =  (int) (gridWidth*(object.pos.x - simulationRect.left)/simulationRect.width());
            int y = (int) (gridHeight*(object.pos.y - simulationRect.top)/simulationRect.height());
            HashSet<PhysicsObject> collideWith = new HashSet<>();
            for (int i = -1; i <= 1; i++) {
                if(x + i < 0 || x + i >= gridWidth) continue;
                for (int j = -1; j <= 1; j++) {
                    if(y + j < 0 || y + j >= gridHeight) continue;
                    if(grid[x+i][y+j] == null) continue;
                    if(grid[x+i][y+j].size() == 0) continue;
                    collideWith.addAll(grid[x+i][y+j]);
                }
            }
            for(PhysicsObject other : collideWith){
                if(other == object) continue;
                if(other.explosionSource) continue;
                object.collide(other);
            }
        }
    }
}
