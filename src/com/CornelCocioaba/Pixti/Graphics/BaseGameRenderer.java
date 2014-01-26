package com.CornelCocioaba.Pixti.Graphics;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import com.CornelCocioaba.Pixti.Engine.BuildConfig;
import com.CornelCocioaba.Pixti.Utils.Debug;
import com.CornelCocioaba.Pixti.Utils.Time;

public abstract class BaseGameRenderer implements Renderer {

	private boolean mSurfaceCreated;
	private int mWidth;
	private int mHeight;
	private long mLastTime;
	private int nbFrames;
	private int mFPS;

	public BaseGameRenderer() {
		mSurfaceCreated = false;
		mLastTime = System.currentTimeMillis();
		nbFrames = 0;
		mFPS = 60;
		Time.Init();
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

		onChangedSurface(width, height, mSurfaceCreated);
		mSurfaceCreated = false;
	}

	@Override
	public void onDrawFrame(GL10 notUsed) {
		if (BuildConfig.DEBUG) {
			nbFrames++;
			long currentTime = System.currentTimeMillis();
			if (currentTime - mLastTime >= 1000) {
				mFPS = nbFrames;
				nbFrames = 0;
				mLastTime = currentTime;
			}
		}
		onDrawFrame();
	}

	public int getFPS() {
		return mFPS;
	}

	public abstract void onChangedSurface(int width, int height, boolean contextLost);

	public abstract void onDrawFrame();
}
