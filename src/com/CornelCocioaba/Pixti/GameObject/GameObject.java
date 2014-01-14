package com.CornelCocioaba.Pixti.GameObject;

import java.util.ArrayList;

import com.CornelCocioaba.Pixti.Engine.Camera;

public class GameObject implements IUpdateable, IDrawable {

	public String name = "";
	public String[] tags;

	protected GameObject parent = null;
	protected ArrayList<GameObject> children = new ArrayList<GameObject>();

	public float x, y;
	public float width, height;
	public float scaleX = 1.0f, scaleY = 1.0f;
	public float angle;

	public GameObject() {
		this(0, 0);
	}

	public GameObject(float x, float y) {
		this.x = x;
		this.y = y;
		this.scaleX = 1.0f;
		this.scaleY = 1.0f;
		this.angle = 0.0f;
		name = getClass().getName();
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
