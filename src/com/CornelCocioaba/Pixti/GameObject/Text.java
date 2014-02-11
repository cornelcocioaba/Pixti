package com.CornelCocioaba.Pixti.GameObject;

import android.opengl.GLES20;

import com.CornelCocioaba.Pixti.Graphics.Camera;
import com.CornelCocioaba.Pixti.Graphics.Color;
import com.CornelCocioaba.Pixti.Graphics.Font.Font;

public class Text extends GameObject {

	private String mText;
	private Font mFont;
	private Color mColor;

	public Text(float x, float y, String text, Font font) {
		this(x, y, text, Color.WHITE, font);
	}

	public Text(float x, float y, String text, Color color, Font font) {
		super(x, y);

		mText = text;
		mColor = color;
		mFont = font;
	}

	public String getText() {
		return mText;
	}

	public void setText(String text) {
		mText = text;
	}

	public Color getColor() {
		return mColor;
	}

	public void setColor(Color c) {
		this.mColor = c;
	}

	@Override
	public void Draw(Camera cam) {
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		mFont.begin(mColor, cam.mCombinedMatrix);
		mFont.draw(mText, x, y);
		mFont.end();
		GLES20.glDisable(GLES20.GL_BLEND);
	}
}
