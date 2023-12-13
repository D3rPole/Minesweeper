package com.example.minesweeper.Physics;

import android.graphics.Rect;

public class PhysicsObject {
    public Vec2 pos;
    public Vec2 prev_pos;

    public boolean explosionSource = false;
    public boolean lookedAt;
    public boolean flagged;
    public boolean isMine;
    public int neighbours;
    public float width;
    public float height;

    /**
     * Constructs a PhysicsObject with a specified position.
     *
     * @param pos The initial position of the object.
     */
    public PhysicsObject(Vec2 pos) {
        this.pos = pos;
        this.prev_pos = pos.clone();
    }

    /**
     * Updates the position of the object based on time and acceleration.
     *
     * @param dTime The time increment for the update.
     * @param acc   The acceleration vector.
     */
    public void update(float dTime, Vec2 acc){
        Vec2 tempPos = pos;
        pos = pos.add(pos.subtract(prev_pos)).add(acc.multiply(dTime*dTime));
        prev_pos = tempPos;
    }

    /**
     * Ensures the object stays within the specified bounding box.
     *
     * @param rect The bounding box within which the object should stay.
     */
    public void clampInBoundingbox(Rect rect){
        pos.x = clamp(pos.x,rect.left + width / 2,rect.right - width / 2);
        pos.y = clamp(pos.y,rect.top + height / 2,rect.bottom - height / 2);
    }

    public float clamp(float value, float min, float max){
        return Math.min(Math.max(value, min), max);
    }

    /**
     * Adds velocity to the object.
     *
     * @param vel   The velocity vector to be added.
     * @param dTime The time increment for velocity addition.
     */
    public void addVel(Vec2 vel,float dTime){
        prev_pos = prev_pos.subtract(vel.multiply(dTime));
    }

    /**
     * Handles collision between this object and another PhysicsObject.
     *
     * @param other The other PhysicsObject to check collision with.
     */
    public void collide(PhysicsObject other){
        CollisionResult collisionResult = this.checkCollision(other);
        if (collisionResult.isColliding){
            pos = pos.add(collisionResult.normal.multiply(collisionResult.penetrationDepth / 2));
            other.pos.subtract(collisionResult.normal.multiply(collisionResult.penetrationDepth / 2));
        }
    }

    /**
     * Checks for collision between this object and another PhysicsObject.
     *
     * @param other The other PhysicsObject to check collision with.
     * @return CollisionResult object containing collision information.
     */
    private CollisionResult checkCollision(PhysicsObject other) {
        // Calculate the bounds of 'this' object
        float thisLeft = pos.x;
        float thisRight = pos.x + width;
        float thisTop = pos.y;
        float thisBottom = pos.y + height;

        // Calculate the bounds of 'other' object
        float otherLeft = other.pos.x;
        float otherRight = other.pos.x + other.width;
        float otherTop = other.pos.y;
        float otherBottom = other.pos.y + other.height;

        // Calculate overlap along both axes
        float xOverlap = Math.min(thisRight, otherRight) - Math.max(thisLeft, otherLeft);
        float yOverlap = Math.min(thisBottom, otherBottom) - Math.max(thisTop, otherTop);
        boolean isColliding = xOverlap > 0 && yOverlap > 0;

        // Calculate collision normal
        Vec2 normal = new Vec2(0, 0);
        if (isColliding) {
            if (xOverlap < yOverlap) {
                if (thisLeft < otherLeft) {
                    normal = new Vec2(-1, 0);
                } else {
                    normal = new Vec2(1, 0);
                }
            } else {
                if (thisTop < otherTop) {
                    normal = new Vec2(0, -1);
                } else {
                    normal = new Vec2(0, 1);
                }
            }
        }

        // Calculate penetration depth
        float penetrationDepth = Math.min(xOverlap, yOverlap);

        // Return collision information
        return new CollisionResult(isColliding, normal, penetrationDepth);
    }



}
