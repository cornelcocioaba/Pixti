package com.CornelCocioaba.Pixti.GameObject;

import java.util.ArrayList;

import com.CornelCocioaba.Pixti.Engine.Camera;

public class GameObject implements IUpdateable, IDrawable {

	public String name = "";
	public String[] tags;

	protected GameObject parent = null;
	protected ArrayList<GameObject> children = new ArrayList<GameObject>();

	protected float x, y;

	public GameObject() {
		this(0, 0);
	}

	public GameObject(int x, int y) {
		this.x = x;
		this.y = y;
		name = getClass().getName();
	}

	// getters/setters
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setX(float pX) {
		x = pX;
	}

	public void setY(float pY) {
		y = pY;
	}

	public void addChild(GameObject obj) {
		children.add(obj);
	}

	public void removeChild(GameObject obj) {
		children.remove(obj);
	}

	@Override
	public void Draw(Camera cam) {
		final int size = children.size();
		for (int i = 0; i < size; i++) {
			children.get(i).Draw(cam);
		}
	}

	@Override
	public void Update() {
		final int size = children.size();
		for (int i = 0; i < size; i++) {
			children.get(i).Update();
		}
	}
}
