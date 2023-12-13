package com.example.minesweeper.Physics;

public class Vec2 {
    // Ich hab die Klasse 1000x geschrieben, also jetzt darf mal ChatGPT ran :)
    public float x;
    public float y;

    /**
     * Constructs a Vec2 with specified x and y components.
     *
     * @param x The x-component of the vector.
     * @param y The y-component of the vector.
     */
    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a Vec2 with default x and y components as 0.
     */
    public Vec2(){
        x = 0;
        y = 0;
    }

    /**
     * Clones the Vec2, creating an exact copy of the vector.
     *
     * @return A new Vec2 with identical x and y components.
     */
    public Vec2 clone(){
        return new Vec2(x,y);
    }

    /**
     * Adds another vector to this vector.
     *
     * @param other The vector to be added.
     * @return A new Vec2 representing the result of addition.
     */
    public Vec2 add(Vec2 other) {
        return new Vec2(this.x + other.x, this.y + other.y);
    }

    /**
     * Subtracts another vector from this vector.
     *
     * @param other The vector to be subtracted.
     * @return A new Vec2 representing the result of subtraction.
     */
    public Vec2 subtract(Vec2 other) {
        return new Vec2(this.x - other.x, this.y - other.y);
    }

    /**
     * Calculates the dot product of this vector with another vector.
     *
     * @param other The other vector for the dot product.
     * @return The dot product value as a float.
     */
    public float dotProduct(Vec2 other) {
        return this.x * other.x + this.y * other.y;
    }

    /**
     * Multiplies this vector by a scalar value.
     *
     * @param scalar The scalar value to multiply with.
     * @return A new Vec2 representing the result of multiplication.
     */
    public Vec2 multiply(float scalar) {
        return new Vec2(this.x * scalar, this.y * scalar);
    }

    /**
     * Calculates the magnitude (length) of this vector.
     *
     * @return The magnitude of the vector as a float.
     */
    public float magnitude() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y);
    }

    /**
     * Calculates the distance between this vector and another vector.
     *
     * @param other The other vector to calculate the distance to.
     * @return The distance between the two vectors as a float.
     */
    public float distance(Vec2 other) {
        return other.subtract(this).magnitude();
    }

    /**
     * Normalizes this vector, maintaining its direction and setting its magnitude to 1.
     *
     * @return A new Vec2 representing the normalized vector.
     */
    public Vec2 normalize() {
        float mag = magnitude();
        if (mag != 0) {
            return new Vec2(this.x / mag, this.y / mag);
        }
        return new Vec2(0, 0); // Avoid division by zero
    }

    /**
     * Generates a random Vec2 within a specified range.
     *
     * @param from The minimum value for x and y components.
     * @param to   The maximum value for x and y components.
     * @return A new Vec2 with random x and y components within the specified range.
     */
    public static Vec2 random(float from, float to) {
        float randomX = (float) (Math.random() * (to - from) + from);
        float randomY = (float) (Math.random() * (to - from) + from);
        return new Vec2(randomX, randomY);
    }
}
