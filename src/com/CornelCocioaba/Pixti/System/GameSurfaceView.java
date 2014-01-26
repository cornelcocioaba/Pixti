package com.CornelCocioaba.Pixti.System;

import com.CornelCocioaba.Pixti.Graphics.GameRenderer;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class GameSurfaceView extends GLSurfaceView {

	public GameSurfaceView(Context context) {
		super(context);

		setEGLContextClientVersion(2);
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);
	}
	
	public void setRenderer(GameRenderer renderer){
		super.setRenderer(renderer);
	}
}
