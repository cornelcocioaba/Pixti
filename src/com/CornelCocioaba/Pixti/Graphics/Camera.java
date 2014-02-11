package com.CornelCocioaba.Pixti.Graphics;

import android.opengl.GLES20;
import android.opengl.Matrix;

//orthographic camera

public class Camera {

	public float x, y;
	private float zoom;

	private final float[] mProjectionMatrix = new float[16];
	private final float[] mViewMatrix = new float[16];
	public final float[] mCombinedMatrix = new float[16];

	private int mViewportWidth;
	private int mViewportHeight;

	public Camera(int viewportWidth, int viewportHeight) {
		x = 0;
		y = 0;
		zoom = 1f;
		mViewportWidth = viewportWidth;
		mViewportHeight = viewportHeight;
		update();
	}

	/**
	 * Update camera matrices. Call this whenever the camera changes
	 */
	private void update() {
		GLES20.glViewport(0, 0, (int) mViewportWidth, (int) mViewportHeight);

		Matrix.orthoM(mProjectionMatrix, 0, 0f, zoom * mViewportWidth, 0f, zoom * mViewportHeight, 0, 50);

		Matrix.setLookAtM(mViewMatrix, 0, x, y, 1f, // eye position
				x, y, -1f, // center position
				0f, 1.0f, 0.0f // up
		);

		Matrix.multiplyMM(mCombinedMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
	}

	public void setViewport(int width, int height) {
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

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;

		update();
	}

	public void translate(float deltaX, float deltaY) {
		x += deltaX;
		y += deltaY;

		update();
	}
}
