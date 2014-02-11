package com.CornelCocioaba.Pixti.Graphics;

import static android.opengl.GLES20.GL_CLAMP_TO_EDGE;
import static android.opengl.GLES20.GL_LINEAR;

public class TextureParams {
	public int mMinFilter;
	public int mMaxFilter;
	public int mWrapS;
	public int mWrapT;

	public TextureParams() {
		this(GL_LINEAR, GL_LINEAR, GL_CLAMP_TO_EDGE, GL_CLAMP_TO_EDGE);
	}

	public TextureParams(int min, int max, int wrap_s, int wrap_t) {
		mMinFilter = min;
		mMaxFilter = max;
		mWrapS = wrap_s;
		mWrapT = wrap_t;
	}
	
	public int getMinFilter() {
		return mMinFilter;
	}

	public void setMinFilter(int minFilter) {
		this.mMinFilter = minFilter;
	}

	public int getMaxFilter() {
		return mMaxFilter;
	}

	public void setMaxFilter(int maxFilter) {
		this.mMaxFilter = maxFilter;
	}

	public int getWrapS() {
		return mWrapS;
	}

	public void setWrapS(int wrapS) {
		this.mWrapS = wrapS;
	}

	public int getWrapT() {
		return mWrapT;
	}

	public void setWrapT(int wrapT) {
		this.mWrapT = wrapT;
	}
}
