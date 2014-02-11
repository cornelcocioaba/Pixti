package com.CornelCocioaba.Pixti.Core;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.CornelCocioaba.Pixti.Graphics.GameRenderer;

public class GameSurfaceView extends GLSurfaceView {

	public GameSurfaceView(Context context, GameRenderer renderer) {
		super(context);

		setEGLContextClientVersion(2);
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		setRenderer(renderer);
	}
	
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//
//		final int specWidth = MeasureSpec.getSize(widthMeasureSpec);
//        final int specHeight = MeasureSpec.getSize(heightMeasureSpec);
//
//		setMeasuredDimension(specWidth, specHeight);
//	}
}
