package com.CornelCocioaba.Pixti.GameObject;

import com.CornelCocioaba.Pixti.Graphics.Camera;

public class HUD extends GameObject {

	private Camera hudCamera;
	
	public HUD(int width, int height){
		hudCamera = new Camera(width, height);
	}
	
	@Override
	public void Draw(Camera unused) {
		super.Draw(hudCamera);
	}
}
