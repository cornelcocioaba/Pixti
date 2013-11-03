package com.CornelCocioaba.Pixti.OpenGL;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;

import com.CornelCocioaba.Pixti.Utils.BufferUtils;

public class Quad {

	public static final int VERTEX_COUNT = 4;
	public static final int COORDS_PER_VERTEX = 3;

	public final short[] indices = { 0, 1, 2, 0, 2, 3 };

	private float[] vertices;

	public FloatBuffer vertexBuffer;
	public ShortBuffer drawOrderBuffer;

	public Quad(float width, float height) {
		this(0, 0, width, height);
	}

	public Quad(float x, float y, float width, float height) {
		vertices = new float[] { 
				x, y, 0.0f,
				x, y + height, 0.0f, 
				x + width, y + height, 0.0f, 
				x + width, y, 0.0f };

		vertexBuffer = BufferUtils.createFloatBuffer(vertices);

		drawOrderBuffer = BufferUtils.createShortBuffer(indices);
	}

	public void draw() {
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, drawOrderBuffer);
	}
}
