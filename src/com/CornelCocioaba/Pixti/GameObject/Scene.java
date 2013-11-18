package com.CornelCocioaba.Pixti.GameObject;

import android.opengl.GLES20;
import aurelienribon.tweenengine.Tween;

import com.CornelCocioaba.Pixti.Engine.Camera;
import com.CornelCocioaba.Pixti.OpenGL.Color;

public class Scene extends GameObject {
	
	public Color mBackgroundColor;
	
	public Scene() {
		super(0,0);
		Tween.registerAccessor(GameObject.class, new GameObjectAccessor());
		mBackgroundColor = new Color(0.0f, 0.6f, 0.8f, 1.0f);
	}

	@Override
	public void Draw(Camera cam) {
		GLES20.glClearColor(mBackgroundColor.getRed(), mBackgroundColor.getGreen(), mBackgroundColor.getBlue(), mBackgroundColor.getAlpha());
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		super.Draw(cam);
	}
}
