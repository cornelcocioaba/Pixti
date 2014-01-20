package com.CornelCocioaba.Pixti.Graphics;

public class Color {

	public static Color WHITE = new Color(1.0f, 1.0f, 1.0f, 1.0f);
	public static Color BLACK = new Color(0.0f, 0.0f, 0.0f, 1.0f);
	public static Color RED = new Color(1.0f, 0.0f, 0.0f, 1.0f);
	public static Color GREEN = new Color(0.0f, 1.0f, 0.0f, 1.0f);
	public static Color BLUE = new Color(0.0f, 0.0f, 1.0f, 1.0f);
	public static Color YELLOW = new Color(1.0f, 1.0f, 0.0f, 0.0f);

	private float mRed;
	private float mGreen;
	private float mBlue;
	private float mAlpha;

	public Color(float red, float green, float blue, float alpha) {
		mRed = red;
		mGreen = green;
		mBlue = blue;
		mAlpha = alpha;
	}

	public float getRed() {
		return mRed;
	}

	public float getGreen() {
		return mGreen;
	}

	public float getBlue() {
		return mBlue;
	}

	public float getAlpha() {
		return mAlpha;
	}

	public void setRed(float mRed) {
		this.mRed = mRed;
	}

	public void setGreen(float mGreen) {
		this.mGreen = mGreen;
	}

	public void setBlue(float mBlue) {
		this.mBlue = mBlue;
	}

	public void setAlpha(float mAlpha) {
		this.mAlpha = mAlpha;
	}

	public float[] getColorAsFloatArray(){
		return new float[] { mRed, mGreen, mBlue, mAlpha };
	}
	
	public static float[] getColorAsFloatArray(Color color) {
		return color.getColorAsFloatArray();
	}
	
}
