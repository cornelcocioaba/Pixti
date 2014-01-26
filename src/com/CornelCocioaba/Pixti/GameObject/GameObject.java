package com.CornelCocioaba.Pixti.GameObject;

import java.util.ArrayList;

import com.CornelCocioaba.Pixti.Graphics.Camera;

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

	public GameObject getParent(){
		return parent;
	}
	
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
	public float getWorldX() {
		float worldX = x;
		if (parent != null)
			worldX += parent.getWorldX();

		return worldX;
	}
	
	public float getWorldY() {
		float worldY = y;
		if (parent != null)
			worldY += parent.getWorldY();

		return worldY;
	}
	
	public float getWorldScaleX(){
		float worldScaleX = scaleX;
		if (parent != null)
			worldScaleX *= parent.getWorldScaleX();

		return worldScaleX;
	}
	
	public float getWorldScaleY(){
		float worldScaleY = scaleY;
		if (parent != null)
			worldScaleY *= parent.getWorldScaleY();

		return worldScaleY;
	}
	
	public float getWorldRotation(){
		float worldRotation = angle;
		if (parent != null)
			worldRotation += parent.getWorldRotation();

		return worldRotation;
	}
	

	public int getChildCount() {
		return children.size();
	}

	public GameObject getChild(int i) {
		return children.get(i);
	}

	public ArrayList<GameObject> getChildren() {
		return children;
	}
	
	public void setScale(float scale){
		this.scaleX = scale;
		this.scaleY = scale;
	}

	public void addChild(GameObject go) {
		go.parent = this;
		children.add(go);
	}

	public void removeChild(GameObject go) {
		go.parent = null;
		children.remove(go);
	}

	public void removeSelf() {
		if (parent != null)
			parent.removeChild(this);
	}

	public void removeChildren() {
		if (children != null)
			children.clear();
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
		for (int i = 0; i < children.size(); i++) {
			children.get(i).Update();
		}
	}
}
