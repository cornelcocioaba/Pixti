package com.CornelCocioaba.Pixti.GameObject;

import android.opengl.GLES20;

import com.CornelCocioaba.Pixti.Engine.Camera;

public class Scene extends GameObject {
	
	public Scene(int x, int y) {
		super(x, y);
	}

	@Override
	public void Draw(Camera cam) {
		GLES20.glClearColor(0.0f, 0.6f, 0.8f, 1.0f);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		super.Draw(cam);
	}
}
