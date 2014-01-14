package com.CornelCocioaba.Pixti.Physics;

public class BoundingBox {

	public float minX, minY;
	public float maxX, maxY;

	// public boolean overlaps(AABB other){
	//
	// }

	public boolean contains(BoundingBox other) {
		return !(maxX < other.minX || other.maxX < minX || maxY < other.minX || other.maxY < minY);
	}
}
