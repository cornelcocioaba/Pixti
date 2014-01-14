package com.CornelCocioaba.Pixti.Math;

/*
 * As described here
 * https://sites.google.com/site/t3hprogrammer/research/circle-circle-collision-tutorial
 * http://cgp.wikidot.com/circle-to-circle-collision-detection
 */
public class Circle {

	public float x, y;
	public float radius;

	/*
	 * Circle point test
	 */
	public boolean contains(float x, float y) {
		float dx = this.x - x;
		float dy = this.y - y;
		return dx * dx + dy * dy <= radius * radius;
	}

	/*
	 * Circle to circle collision detection
	 * Checks if circle is inside this circle
	 */
	public boolean contains(Circle circle) {

		float dx = x - circle.x;
		float dy = y - circle.y;

		return dx * dx + dy * dy + circle.radius * circle.radius < radius * radius;
	}

	/*
	 * Check if circle overlaps the given circle
	 */
	public boolean overlaps(Circle circle) {
		float dx = x - circle.x;
		float dy = y - circle.y;

		float radii = radius + circle.radius;

		return dx * dx + dy * dy < radii * radii;
	}
}
