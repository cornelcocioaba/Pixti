package com.CornelCocioaba.Pixti.Graphics;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import com.CornelCocioaba.Pixti.Engine.BuildConfig;
import com.CornelCocioaba.Pixti.Utils.Debug;

public abstract class BaseGameRenderer implements Renderer {

	private boolean mSurfaceCreated;
	private int mWidth = -1;;
	private int mHeight = -1;

	public BaseGameRenderer() {
		mSurfaceCreated = false;
	}

	@Override
	public void onSurfaceCreated(GL10 notUsed, EGLConfig config) {
		if (BuildConfig.DEBUG) {
			Log.i(Debug.TAG, "Surface created.");
		}
		mSurfaceCreated = true;
	}

	@Override
	public void onSurfaceChanged(GL10 notUsed, int width, int height) {
		if (!mSurfaceCreated && width == mWidth && height == mHeight) {
			if (BuildConfig.DEBUG) {
				Log.i(Debug.TAG, "Surface changed but already handled.");
			}
			return;
		}
		if (BuildConfig.DEBUG) {
			// Android honeycomb has an option to keep the context.
			String msg = "Surface changed width:" + width + " height:" + height;
			if (mSurfaceCreated) {
				msg += " context lost.";
			} else {
				msg += ".";
			}
			Log.i(Debug.TAG, msg);
		}

		mWidth = width;
		mHeight = height;

		onSurfaceChanged(width, height);
		mSurfaceCreated = false;
	}

	@Override
	public void onDrawFrame(GL10 notUsed) {
		onDrawFrame();
	}

	public abstract void onSurfaceChanged(int width, int height);

	public abstract void onDrawFrame();
}
