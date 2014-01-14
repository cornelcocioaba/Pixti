package com.CornelCocioaba.Pixti.Engine;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class GameSurfaceView extends GLSurfaceView {

	private GameRenderer mRenderer;

	public GameSurfaceView(Context context) {
		super(context);

		setEGLContextClientVersion(2);
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);

		this.mRenderer = new GameRenderer(context);
		this.setOnTouchListener(mRenderer.scene);

		setRenderer(this.mRenderer);
	}
}
