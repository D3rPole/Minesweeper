package com.example.minesweeper.Game.Physics;

import com.example.minesweeper.Game.Config;

public class PhysicsObject {
    public Vec2 pos;
    private Vec2 vel;

    public boolean explosionSource = false;
    public boolean lookedAt;
    public boolean flagged;
    public boolean isMine;
    public int neighbours;
    public float width;
    public float height;

    public PhysicsObject(Vec2 pos) {
        this.pos = pos;
        this.vel = new Vec2(0, 0); // Initial velocity is zero
    }

    public void update(float deltaTime) {
        // Apply gravity to velocity using Config.gravity
        Vec2 gravityForce = Config.gravity.multiply(deltaTime);
        vel = vel.add(gravityForce);

        // Update position using velocity and deltaTime
        Vec2 deltaPos = vel.multiply(deltaTime);
        pos = pos.add(deltaPos);
    }

    public void addVel(Vec2 vel){
        this.vel = this.vel.add(vel);
    }
}
