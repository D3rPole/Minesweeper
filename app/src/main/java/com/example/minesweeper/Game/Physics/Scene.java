package com.example.minesweeper.Game.Physics;

import android.util.Log;

import com.example.minesweeper.Game.MineField;

public class Scene {
    public PhysicsObject[] objects;

    private float xDist;
    private float yDist;
    public Scene(MineField mineField, int uiWidth, int uiHeight){
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

    public void explodeAt(int x, int y){
        Vec2 pos = new Vec2(x * xDist + xDist / 2,y * yDist + yDist / 2);
        for(PhysicsObject object : objects){
            float dist = pos.distance(object.pos);
            if(dist == 0){
                object.explosionSource = true;
                continue;
            }
            object.addVel(object.pos.subtract(pos).multiply(100f / dist));
            object.addVel(Vec2.random(-10,10));
        }
    }

    public void update(float dTime){
        for(PhysicsObject object : objects){
            if(object.explosionSource) continue;
            object.update(dTime);
        }
    }
}
