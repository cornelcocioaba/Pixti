package com.CornelCocioaba.Pixti.Graphics;

import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_NEAREST;
import static android.opengl.GLES20.GL_REPEAT;
import static android.opengl.GLES20.GL_TEXTURE_2D;

public class TextureParams {
	public int mTextureType;
	public int mMinFilter;
	public int mMaxFilter;
	public int mWrapS;
	public int mWrapT;

	public TextureParams() {
		this(GL_TEXTURE_2D, GL_NEAREST, GL_LINEAR, GL_REPEAT, GL_REPEAT);
	}

	public TextureParams(int type, int min, int max, int wrap_s, int wrap_t) {
		mTextureType = type;
		mMinFilter = min;
		mMaxFilter = max;
		mWrapS = wrap_s;
		mWrapT = wrap_t;
	}
	
	public int getTextureType() {
		return mTextureType;
	}

	public void setTextureType(int textureType) {
		this.mTextureType = textureType;
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
