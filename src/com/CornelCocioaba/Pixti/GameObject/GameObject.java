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
	public float mAngle;

	public GameObject() {
		this(0, 0);
	}

	public GameObject(int x, int y) {
		this.x = x;
		this.y = y;
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
