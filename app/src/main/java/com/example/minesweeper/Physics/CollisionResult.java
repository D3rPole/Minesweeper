package com.example.minesweeper.Physics;

class CollisionResult {
    public boolean isColliding;
    public Vec2 normal;
    public float penetrationDepth;

    public CollisionResult(boolean isColliding, Vec2 normal, float penetrationDepth) {
        this.isColliding = isColliding;
        this.normal = normal;
        this.penetrationDepth = penetrationDepth;
    }
}
