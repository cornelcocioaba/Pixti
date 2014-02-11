package com.CornelCocioaba.Pixti.Graphics;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.CornelCocioaba.Pixti.Core.Engine;

public class GameRenderer extends BaseGameRenderer {

	private Engine mEngine;

	public GameRenderer(Engine engine) {
		this.mEngine = engine;
	}

	@Override
	public void onSurfaceCreated(GL10 notUsed, EGLConfig config) {
		super.onSurfaceCreated(notUsed, config);
		mEngine.onCreated();
	}

	@Override
	public void onDrawFrame() {
		mEngine.onDrawFrame();
	}

	@Override
	public void onSurfaceChanged(int width, int height) {
		mEngine.onResized(width, height);
	}
}
