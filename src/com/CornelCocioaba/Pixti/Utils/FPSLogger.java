package com.CornelCocioaba.Pixti.Utils;

import com.CornelCocioaba.Pixti.Core.Time;
import com.CornelCocioaba.Pixti.GameObject.IUpdateable;

public class FPSLogger implements IUpdateable {
	private long mLastTime;
	private int nbFrames;
	private int mFPS;
	private float mFrequencyInv;

	
	public FPSLogger() {
		this(1);
	}
	
	/**
	 * @param frequency times per second
	 */
	public FPSLogger(float frequency){
		mLastTime = System.currentTimeMillis();
		nbFrames = 0;
		mFPS = 60;
		mFrequencyInv = 1.0f / frequency;
	}


	@Override
	public void Update() {
		nbFrames++;
		long currentTime = System.currentTimeMillis();
		if (currentTime - mLastTime >= mFrequencyInv * Time.MILLISECONDS_PER_SECOND) {
			mFPS = nbFrames;
			nbFrames = 0;
			mLastTime = currentTime;
			Debug.log(mFPS);
		}
	}

	public int getFPS() {
		return mFPS;
	}
}
