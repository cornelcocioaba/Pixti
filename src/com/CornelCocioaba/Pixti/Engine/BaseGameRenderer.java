package com.CornelCocioaba.Pixti.Engine;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import com.CornelCocioaba.Pixti.Utils.Debug;

public abstract class BaseGameRenderer implements Renderer {

	private boolean mFirstDraw;
	private boolean mSurfaceCreated;
	private int mWidth;
	private int mHeight;
	private long mLastTime;
	private int mFPS;

	public BaseGameRenderer() {
		mFirstDraw = true;
		mSurfaceCreated = false;
		mWidth = -1;
		mHeight = -1;
		mLastTime = System.currentTimeMillis();
		mFPS = 0;
	}

	@Override
	public void onSurfaceCreated(GL10 notUsed, EGLConfig config) {
		if (BuildConfig.DEBUG) {
			Log.i(Debug.TAG, "Surface created.");
		}
		mSurfaceCreated = true;
		mWidth = -1;
		mHeight = -1;
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

		onCreate(mWidth, mHeight, mSurfaceCreated);
		mSurfaceCreated = false;
	}

	@Override
	public void onDrawFrame(GL10 notUsed) {
		onDrawFrame(mFirstDraw);

		if (BuildConfig.DEBUG) {
			mFPS++;
			long currentTime = System.currentTimeMillis();
			if (currentTime - mLastTime >= 1000) {
				mFPS = 0;
				mLastTime = currentTime;
			}
		}

		if (mFirstDraw) {
			mFirstDraw = false;
		}
	}

	public int getFPS() {
		return mFPS;
	}

	public abstract void onCreate(int width, int height, boolean contextLost);

	public abstract void onDrawFrame(boolean firstDraw);
}
