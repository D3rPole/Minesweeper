package com.example.minesweeper.Physics;

class CollisionResult {
    public boolean isColliding;
    public Vec2 normal;
    public float penetrationDepth;

    /**
     * Constructs a CollisionResult object with collision information.
     *
     * @param isColliding      Indicates if a collision occurred.
     * @param normal           The normal vector of the collision.
     * @param penetrationDepth The depth of penetration during collision.
     */
    public CollisionResult(boolean isColliding, Vec2 normal, float penetrationDepth) {
        this.isColliding = isColliding;
        this.normal = normal;
        this.penetrationDepth = penetrationDepth;
    }
}
