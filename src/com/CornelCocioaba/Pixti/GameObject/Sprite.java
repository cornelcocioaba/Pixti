package com.CornelCocioaba.Pixti.GameObject;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.view.MotionEvent;

import com.CornelCocioaba.Pixti.Engine.Camera;
import com.CornelCocioaba.Pixti.OpenGL.Color;
import com.CornelCocioaba.Pixti.OpenGL.TextureRegion;
import com.CornelCocioaba.Pixti.OpenGL.TextureShaderProgram;

public class Sprite extends AbstractRectangle {

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
		Matrix.translateM(mMVPMatrix, 0, x, y, 0);
		Matrix.rotateM(mMVPMatrix, 0, mAngle, 0, 0, -1.0f);
		Matrix.scaleM(mMVPMatrix, 0, scaleX, scaleY, 0);
		Matrix.multiplyMM(mMVPMatrix, 0, cam.mCombinedMatrix, 0, mMVPMatrix, 0);

		mProgram.setTextureId(mMVPMatrix, textureRegion.getTexture().textureId);
		mProgram.setVertexBufferPointer(vertexBuffer);
		mProgram.enablePositionAttribute();
		mProgram.enableTextureCoordinatesAttribute();
		mProgram.setTextureCoordinatesPointer(textureRegion.getUvBuffer());

		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		// GLES20.glDrawElements(GLES20.GL_TRIANGLES, triangles.length, GLES20.GL_UNSIGNED_SHORT, this.triangleBuffer);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);

		GLES20.glDisable(GLES20.GL_BLEND);

		mProgram.disablePositionAttribute();
		mProgram.disableTextureCoordinatesAttribute();
	}

	public boolean onTouchEvent(MotionEvent event) {
		this.x = event.getX();
		this.y = 720 - event.getY();
		return true;
	}

}
