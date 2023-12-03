package com.example.minesweeper.Game.Physics;

public class Scene {
    PhysicsObject[] objects;

    public Scene(){
        objects = new PhysicsObject[5];
        for (int i = 0; i < 5; i++) {
            objects[i] = new PhysicsObject(new Vec2(500,200 + i * 50));
        }
    }
}
