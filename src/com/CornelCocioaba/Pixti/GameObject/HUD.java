package com.CornelCocioaba.Pixti.GameObject;

import com.CornelCocioaba.Pixti.Engine.Camera;

public class HUD extends GameObject {

	private Camera hudCamera;
	
	public HUD(float width, float height){
		hudCamera = new Camera(width, height);
	}
	
	@Override
	public void Draw(Camera unused) {
		super.Draw(hudCamera);
	}
}
