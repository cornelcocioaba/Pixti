package com.CornelCocioaba.Pixti.GameObject;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.CornelCocioaba.Pixti.Engine.Camera;
import com.CornelCocioaba.Pixti.Graphics.Color;
import com.CornelCocioaba.Pixti.Graphics.ColorShaderProgram;

public class Rectangle extends RectangleShape {

	private final ColorShaderProgram mProgram;
	private final float[] mMVPMatrix = new float[16];

	
	public Rectangle(float x, float y, float width, float height) {
		this(x, y, width, height, Anchor.MIDDLE_CENTER);
	}
	public Rectangle(float x, float y, float width, float height, Anchor anchor) {
		super(x, y, width, height, Color.WHITE, anchor);
		mProgram = new ColorShaderProgram();
	}

	@Override
	public void Draw(Camera cam) {
		mProgram.useProgram();

		Matrix.setIdentityM(mMVPMatrix, 0);
		Matrix.translateM(mMVPMatrix, 0, x, y, 0);
		Matrix.rotateM(mMVPMatrix, 0, mAngle, 0, 0, -1.0f);
		Matrix.scaleM(mMVPMatrix, 0, scaleX, scaleY, 0);
		Matrix.multiplyMM(mMVPMatrix, 0, cam.mCombinedMatrix, 0, mMVPMatrix, 0);

		mProgram.setUniforms(mMVPMatrix);
		
		mProgram.setVertexBufferPointer(vertexBuffer);
		mProgram.setColorBufferPointer(colorBuffer);
		
		mProgram.enablePositionAttribute();
		mProgram.enableColorAttribute();
		
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);
		//GLES20.glDrawElements(GLES20.GL_TRIANGLES, triangles.length, GLES20.GL_UNSIGNED_SHORT, this.triangleBuffer);

		mProgram.disablePositionAttribute();
		mProgram.disableColorAttribute();
	}
}
