package com.CornelCocioaba.Pixti.Graphics;

import com.CornelCocioaba.Pixti.Engine.Engine;

public class GameRenderer extends BaseGameRenderer {

	private Engine mEngine;
	
	public GameRenderer(Engine engine) {
		this.mEngine = engine;
	}

	@Override
	public void onDrawFrame() {
		mEngine.OnDrawFrame();
	}

	@Override
	public void onChangedSurface(int width, int height, boolean contextLost) {
		if(contextLost){
			mEngine.onSurfaceCreated();
		}else{
			mEngine.onSurfaceResized(width, height);
		}
	}
}
