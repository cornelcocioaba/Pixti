package com.android.texample2;

import android.opengl.GLES20;

import com.CornelCocioaba.Pixti.Engine.Camera;
import com.CornelCocioaba.Pixti.GameObject.GameObject;

public class Text extends GameObject {

	private String mText;
	private Font mFont;
	
	public Text(float x, float y, String text, Font font) {
		super(x, y);
		
		mText = text;
		mFont = font;
	}

	public void setText(String text){
		mText = text;
	}
	
	public String getText(){
		return mText;
	}
	
	@Override
	public void Draw(Camera cam) {
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		mFont.begin(cam.mCombinedMatrix);
		mFont.draw(mText, x, y);
		mFont.end();
		GLES20.glDisable(GLES20.GL_BLEND);
	}
}
