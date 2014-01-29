package com.CornelCocioaba.Pixti.Graphics;

import com.CornelCocioaba.Pixti.Core.Engine;

public class GameRenderer extends BaseGameRenderer {

	private Engine mEngine;
	
	public GameRenderer(Engine engine) {
		this.mEngine = engine;
	}

	@Override
	public void onDrawFrame() {
		mEngine.onDrawFrame();
	}

	@Override
	public void onChangedSurface(int width, int height, boolean contextLost) {
		if(contextLost){
			mEngine.onCreated();
		}else{
			mEngine.onResized(width, height);
		}
	}
}
