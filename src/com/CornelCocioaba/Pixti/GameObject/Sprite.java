package com.CornelCocioaba.Pixti.GameObject;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.view.MotionEvent;

import com.CornelCocioaba.Pixti.Graphics.Camera;
import com.CornelCocioaba.Pixti.Graphics.Color;
import com.CornelCocioaba.Pixti.Graphics.TextureRegion;
import com.CornelCocioaba.Pixti.Graphics.TextureShaderProgram;

public class Sprite extends RectangleShape {

	protected final TextureShaderProgram mProgram;
	protected final float[] mMVPMatrix = new float[16];
	protected TextureRegion textureRegion;

	public Sprite(float x, float y,TextureRegion textureRegion) {
		this(x, y, textureRegion.getWidth(), textureRegion.getHeight(), textureRegion);
	}

	public Sprite(float x, float y, float width, float height, TextureRegion textureRegion) {
		this(x, y, width, height, textureRegion, Color.WHITE, Anchor.MIDDLE_CENTER);
	}

	public Sprite(float x, float y, float width, float height, TextureRegion textureRegion, Color color, Anchor anchor) {
		super(x, y, width, height, color, anchor);

		mProgram = new TextureShaderProgram();
		this.textureRegion = textureRegion;
	}

	public TextureRegion getTextureRegion() {
		return textureRegion;
	}

	@Override
	public void Draw(Camera cam) {
		mProgram.useProgram();

		Matrix.setIdentityM(mMVPMatrix, 0);
		Matrix.translateM(mMVPMatrix, 0, getWorldX(), getWorldY(), 0);
		Matrix.rotateM(mMVPMatrix, 0, getWorldRotation(), 0, 0, -1.0f);
		Matrix.scaleM(mMVPMatrix, 0, getWorldScaleX(), getWorldScaleY(), 0);
		Matrix.multiplyMM(mMVPMatrix, 0, cam.mCombinedMatrix, 0, mMVPMatrix, 0);

		mProgram.setTextureId(mMVPMatrix, textureRegion.getTexture().textureId);
		mProgram.setVertexBufferPointer(vertexBuffer);
		mProgram.setColorBufferPointer(colorBuffer);
		mProgram.setTextureCoordinatesPointer(textureRegion.getUvBuffer());
		
		mProgram.enablePositionAttribute();
		mProgram.enableColorAttribute();
		mProgram.enableTextureCoordinatesAttribute();
		

		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		// GLES20.glDrawElements(GLES20.GL_TRIANGLES, triangles.length, GLES20.GL_UNSIGNED_SHORT, this.triangleBuffer);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);

		GLES20.glDisable(GLES20.GL_BLEND);

		mProgram.disablePositionAttribute();
		mProgram.disableColorAttribute();
		mProgram.disableTextureCoordinatesAttribute();
		
		super.Draw(cam);
	}

	public boolean onTouchEvent(MotionEvent event) {
		this.x = event.getX();
		this.y = 720 - event.getY();
		return true;
	}

}
