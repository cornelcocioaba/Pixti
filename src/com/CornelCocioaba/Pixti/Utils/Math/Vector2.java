package com.CornelCocioaba.Pixti.Utils.Math;

public class Vector2 {

	public float x;
	public float y;

	public Vector2() {
		this(0, 0);
	}

	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2(Vector2 v) {
		this.x = v.x;
		this.y = v.y;
	}

	public Vector2 substract(Vector2 v) {
		x -= v.x;
		y -= v.y;
		return this;
	}

	public Vector2 add(Vector2 v) {
		x += v.x;
		y += v.y;
		return this;
	}

	public float dot(Vector2 v) {
		return this.x * v.x + this.y * v.y;
	}

	public Vector2 mul(float b) {
		this.x *= b;
		this.y *= b;
		return this;
	}

	public float magnitude() {
		return (float) Math.sqrt(x * x + y * y);
	}

	public float sqrMagnitude() {
		return x * x + y * y;
	}

	public Vector2 normalize() {
		float magnitude = magnitude();
		if (magnitude != 0) {
			x /= magnitude;
			y /= magnitude;
		}
		return this;
	}
}
