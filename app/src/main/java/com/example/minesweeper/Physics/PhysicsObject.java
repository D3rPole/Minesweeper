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

    public PhysicsObject(Vec2 pos) {
        this.pos = pos;
        this.prev_pos = pos.clone();
    }

    public void update(float dTime, Vec2 acc){
        Vec2 tempPos = pos;
        pos = pos.add(pos.subtract(prev_pos)).add(acc.multiply(dTime*dTime));
        prev_pos = tempPos;
    }

    public void clampInBoundingbox(Rect rect){
        if(pos.x < rect.left + width){
            pos.x = rect.left + width;
        }
        if(pos.x > rect.right - width){
            pos.x = rect.right - width;
        }
        if(pos.y < rect.top + height){
            pos.y = rect.top + height;
        }
        if(pos.y > rect.bottom - height){
            pos.y = rect.bottom - height;
        }
    }

    public void addVel(Vec2 vel,float dTime){
        prev_pos = prev_pos.subtract(vel.multiply(dTime));
    }
    public void collide(PhysicsObject other){
        CollisionResult collisionResult = this.getCollisionInfo(other);
        if (collisionResult.isColliding){
            pos = pos.add(collisionResult.normal.multiply(collisionResult.penetrationDepth / 2));
            other.pos.subtract(collisionResult.normal.multiply(collisionResult.penetrationDepth / 2));
        }
    }

    private CollisionResult getCollisionInfo(PhysicsObject other) {
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

        // Calculate collision normal
        Vec2 normal = new Vec2(0, 0);
        if (xOverlap > 0 && yOverlap > 0) {
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
        boolean isColliding = xOverlap > 0 && yOverlap > 0;
        return new CollisionResult(isColliding, normal, penetrationDepth);
    }


}
