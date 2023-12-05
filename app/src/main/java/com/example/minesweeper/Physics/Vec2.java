package com.example.minesweeper.Physics;

public class Vec2 {
    // Ich hab die Klasse 1000x geschrieben, also jetzt darf mal ChatGPT ran :)
    public float x;
    public float y;


    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vec2(){
        x = 0;
        y = 0;
    }

    public Vec2 clone(){
        return new Vec2(x,y);
    }

    public Vec2 add(Vec2 other) {
        return new Vec2(this.x + other.x, this.y + other.y);
    }

    public Vec2 subtract(Vec2 other) {
        return new Vec2(this.x - other.x, this.y - other.y);
    }

    public float dotProduct(Vec2 other) {
        return this.x * other.x + this.y * other.y;
    }

    public Vec2 multiply(float scalar) {
        return new Vec2(this.x * scalar, this.y * scalar);
    }

    public float magnitude() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public float distance(Vec2 other) {
        return other.subtract(this).magnitude();
    }

    public Vec2 normalize() {
        float mag = magnitude();
        if (mag != 0) {
            return new Vec2(this.x / mag, this.y / mag);
        }
        return new Vec2(0, 0); // Avoid division by zero
    }

    public static Vec2 random(float from, float to) {
        float randomX = (float) (Math.random() * (to - from) + from);
        float randomY = (float) (Math.random() * (to - from) + from);
        return new Vec2(randomX, randomY);
    }
}