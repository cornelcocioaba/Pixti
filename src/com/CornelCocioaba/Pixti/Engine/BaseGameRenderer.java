package com.CornelCocioaba.Pixti.Engine;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import com.CornelCocioaba.Pixti.Pixti;
import com.CornelCocioaba.Pixti.Utils.Debug;

public abstract class BaseGameRenderer implements Renderer {

	private boolean mSurfaceCreated;
	private long mLastTime;
	private int nbFrames;
	private int mFPS;

	volatile boolean created = false;
	
	public BaseGameRenderer() {
		mSurfaceCreated = false;
		Pixti.width = -1;
		Pixti.height = -1;
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
		Pixti.width = 1000;
		Pixti.height = 1000;
		
	}

	@Override
	public void onSurfaceChanged(GL10 notUsed, int width, int height) {
		if (!mSurfaceCreated && width == Pixti.width && height == Pixti.height) {
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

		Pixti.width = width;
		Pixti.height = height;
		
		if(!created){
			onSurfaceCreated();
			onCreate();
			created = true;
		}

		onChangedSurface(width, height);
		mSurfaceCreated = false;
	}

	@Override
	public void onDrawFrame(GL10 notUsed) {

		Time.Update();
		
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

	public abstract void onSurfaceCreated();
	
	public abstract void onCreate();
	
	public abstract void onChangedSurface(int width, int height);

	public abstract void onDrawFrame();
}
