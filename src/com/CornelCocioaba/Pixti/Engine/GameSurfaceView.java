package com.CornelCocioaba.Pixti.Engine;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class GameSurfaceView extends GLSurfaceView {

	// private GameRenderer mRenderer;
//	private PixelPerfectTestRenderer mRenderer;
	private Renderer mRenderer;

	public GameSurfaceView(Context context) {
		super(context);

		setEGLContextClientVersion(2);
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);

		// this.mRenderer = new GameRenderer();
		this.mRenderer = new GameRenderer(context);

		setRenderer(this.mRenderer);
	}

}
