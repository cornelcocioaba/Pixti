package com.CornelCocioaba.Pixti.Graphics.Font;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;

public class Vertices {
	final static int POSITION_CNT_2D = 2;
	final static int POSITION_CNT_3D = 3;
	final static int COLOR_CNT = 4;
	final static int TEXCOORD_CNT = 2;
	final static int NORMAL_CNT = 3;
	private static final int MVP_MATRIX_INDEX_CNT = 1;

	final static int INDEX_SIZE = Short.SIZE / 8;

	private static final String TAG = "Vertices";
	public final int positionCnt;
	public final int vertexStride;
	public final int vertexSize;
	final IntBuffer vertices;
	final ShortBuffer indices;
	public int numVertices;
	public int numIndices;
	final int[] tmpBuffer;
	private int mTextureCoordinateHandle;
	private int mPositionHandle;
	private int mMVPIndexHandle;

	public Vertices(int maxVertices, int maxIndices) {
		this.positionCnt = POSITION_CNT_2D;
		this.vertexStride = this.positionCnt + TEXCOORD_CNT + MVP_MATRIX_INDEX_CNT;
		this.vertexSize = this.vertexStride * 4;

		ByteBuffer buffer = ByteBuffer.allocateDirect(maxVertices * vertexSize);
		buffer.order(ByteOrder.nativeOrder());
		this.vertices = buffer.asIntBuffer();

		if (maxIndices > 0) {
			buffer = ByteBuffer.allocateDirect(maxIndices * INDEX_SIZE);
			buffer.order(ByteOrder.nativeOrder());
			this.indices = buffer.asShortBuffer();
		} else
			indices = null;

		numVertices = 0;
		numIndices = 0;

		this.tmpBuffer = new int[maxVertices * vertexSize / 4];
		mTextureCoordinateHandle = AttribVariable.A_TexCoordinate.getHandle();
		mMVPIndexHandle = AttribVariable.A_MVPMatrixIndex.getHandle();
		mPositionHandle = AttribVariable.A_Position.getHandle();
	}

	public void setVertices(float[] vertices, int offset, int length) {
		this.vertices.clear();
		int last = offset + length;
		for (int i = offset, j = 0; i < last; i++, j++)
			tmpBuffer[j] = Float.floatToRawIntBits(vertices[i]);
		this.vertices.put(tmpBuffer, 0, length);
		this.vertices.flip();
		this.numVertices = length / this.vertexStride;
	}

	public void setIndices(short[] indices, int offset, int length) {
		this.indices.clear();
		this.indices.put(indices, offset, length);
		this.indices.flip();
		this.numIndices = length;
	}

	public void bind() {
		vertices.position(0);
		GLES20.glVertexAttribPointer(mPositionHandle, positionCnt, GLES20.GL_FLOAT, false, vertexSize, vertices);
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		vertices.position(positionCnt);
		GLES20.glVertexAttribPointer(mTextureCoordinateHandle, TEXCOORD_CNT, GLES20.GL_FLOAT, false, vertexSize,
				vertices);
		GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
		vertices.position(positionCnt + TEXCOORD_CNT);
		GLES20.glVertexAttribPointer(mMVPIndexHandle, MVP_MATRIX_INDEX_CNT, GLES20.GL_FLOAT, false, vertexSize,
				vertices);
		GLES20.glEnableVertexAttribArray(mMVPIndexHandle);
	}

	public void draw(int primitiveType, int offset, int numVertices) {
		if (indices != null) {
			indices.position(offset);
			GLES20.glDrawElements(primitiveType, numVertices, GLES20.GL_UNSIGNED_SHORT, indices);
		} else {
			GLES20.glDrawArrays(primitiveType, offset, numVertices);
		}
	}

	public void unbind() {
		GLES20.glDisableVertexAttribArray(mTextureCoordinateHandle);
	}
}
