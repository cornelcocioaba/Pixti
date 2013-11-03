package com.CornelCocioaba.Pixti.Engine;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.CornelCocioaba.Pixti.Math.Vector2;

//orthographic camera

public class Camera {

	private Vector2 position = new Vector2();

	private final float[] mProjectionMatrix = new float[16];
	private final float[] mViewMatrix = new float[16];
	public final float[] mCombinedMatrix = new float[16];

	private float mViewportWidth;
	private float mViewportHeight;

//	private float zoom;

	public Camera(float viewportWidth, float viewportHeight) {
		mViewportWidth = viewportWidth;
		mViewportHeight = viewportHeight;
		update();
	}

	/**
	 * Update camera matrices. Call this whenever the camera changes
	 */
	public void update() {
		GLES20.glViewport(0, 0, (int) mViewportWidth, (int) mViewportHeight);
		for (int i = 0; i < 16; i++) {

			mProjectionMatrix[i] = 0;
			mViewMatrix[i] = 0;
			mCombinedMatrix[i] = 0;
		}

		Matrix.orthoM(mProjectionMatrix, 0, 0f, mViewportWidth, 0f, mViewportHeight, 0, 50);

		Matrix.setLookAtM(mViewMatrix, 0, position.x, position.y, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

		Matrix.multiplyMM(mCombinedMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
	}

	public void setViewport(float width, float height) {
		mViewportWidth = width;
		mViewportHeight = height;
		update();
	}

	public float getViewportWidth() {
		return mViewportWidth;
	}

	public float getViewportHeight() {
		return mViewportHeight;
	}
}
