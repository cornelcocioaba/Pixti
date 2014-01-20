package com.CornelCocioaba.Pixti.GameObject;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.CornelCocioaba.Pixti.Engine.Camera;
import com.CornelCocioaba.Pixti.Graphics.Color;
import com.CornelCocioaba.Pixti.Graphics.ColorShaderProgram;
import com.CornelCocioaba.Pixti.Utils.Math.MathConstants;

public class CircleShape extends UniformColoredShape {

	private static final int DEFAULT_CIRCLE_RESOLUTION = 30;

	private final ColorShaderProgram mProgram;
	private final float[] mMVPMatrix = new float[16];

	public float radius;
	private int resolution; // vertex count

	public CircleShape(float x, float y, float radius) {
		this(x, y, radius, DEFAULT_CIRCLE_RESOLUTION, Color.WHITE);
	}

	public CircleShape(float x, float y, float radius, int resolution, Color color) {
		this.x = x;
		this.y = y;
		this.scaleX = 1;
		this.scaleY = 1;
		this.radius = radius;
		this.resolution = resolution;
		this.color = color;
		vertexCount = 2 * (resolution );
		updateVertices();
		// updateTriangles();
		updateColor();

		mProgram = new ColorShaderProgram();
	}

	@Override
	public void Draw(Camera cam) {
		mProgram.useProgram();

		Matrix.setIdentityM(mMVPMatrix, 0);
		Matrix.translateM(mMVPMatrix, 0, x, y, 0);
		Matrix.rotateM(mMVPMatrix, 0, angle, 0, 0, -1.0f);
		Matrix.scaleM(mMVPMatrix, 0, scaleX, scaleY, 0);
		Matrix.multiplyMM(mMVPMatrix, 0, cam.mCombinedMatrix, 0, mMVPMatrix, 0);

		mProgram.setUniforms(mMVPMatrix);

		mProgram.setVertexBufferPointer(vertexBuffer);
		mProgram.setColorBufferPointer(colorBuffer);

		mProgram.enablePositionAttribute();
		mProgram.enableColorAttribute();

		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, resolution);

		mProgram.disablePositionAttribute();
		mProgram.disableColorAttribute();
	}

	public void updateVertices() {

		this.mVertices = new float[vertexCount];

		this.mVertices[0] = 0;
		this.mVertices[1] = 0;

		for (int i = 0; i < resolution; i++) {
			float theta = MathConstants.TWO_PI * (float) i / (float) resolution;

			float tempX = radius * (float) Math.cos(theta);
			float tempY = radius * (float) Math.sin(theta);

			this.mVertices[i * 2] = tempX;
			this.mVertices[i * 2 + 1] = tempY;
		}
		updateVertexBuffer();
	}

	public void setRadius(float pRadius) {
		this.radius = pRadius;

		updateVertices();
	}
}
