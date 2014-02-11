package com.CornelCocioaba.Pixti.Graphics.Font;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.CornelCocioaba.Pixti.GameObject.Sprite;
import com.CornelCocioaba.Pixti.Graphics.TextureRegion;
import com.CornelCocioaba.Pixti.Graphics.Font.programs.Program;

public class SpriteBatch {
	final static int VERTEX_SIZE = 5;
	final static int VERTICES_PER_SPRITE = 4;
	final static int INDICES_PER_SPRITE = 6;
	public static final String TAG = "SpriteBatch";

	Vertices vertices;
	float[] vertexBuffer;
	int bufferIndex;
	int maxSprites;
	int numSprites;
	private float[] mVPMatrix;
	private float[] uMVPMatrices = new float[Font.CHAR_BATCH_SIZE * 16];
	private int mMVPMatricesHandle;
	private float[] mMVPMatrix = new float[16];

	public SpriteBatch(int maxSprites, Program program) {
		this.vertexBuffer = new float[maxSprites * VERTICES_PER_SPRITE * VERTEX_SIZE];
		this.vertices = new Vertices(maxSprites * VERTICES_PER_SPRITE, maxSprites * INDICES_PER_SPRITE);
		this.bufferIndex = 0;
		this.maxSprites = maxSprites;
		this.numSprites = 0;

		short[] indices = new short[maxSprites * INDICES_PER_SPRITE];
		int len = indices.length;
		short j = 0;
		for (int i = 0; i < len; i += INDICES_PER_SPRITE, j += VERTICES_PER_SPRITE) {
			indices[i + 0] = (short) (j + 0);
			indices[i + 1] = (short) (j + 1);
			indices[i + 2] = (short) (j + 2);
			indices[i + 3] = (short) (j + 2);
			indices[i + 4] = (short) (j + 3);
			indices[i + 5] = (short) (j + 0);
		}
		vertices.setIndices(indices, 0, len);
		mMVPMatricesHandle = GLES20.glGetUniformLocation(program.getHandle(), "u_MVPMatrix");
	}

	public void beginBatch(float[] vpMatrix) {
		numSprites = 0;
		bufferIndex = 0;
		mVPMatrix = vpMatrix;
	}

	public void endBatch() {
		if (numSprites > 0) {

			GLES20.glUniformMatrix4fv(mMVPMatricesHandle, numSprites, false, uMVPMatrices, 0);
			GLES20.glEnableVertexAttribArray(mMVPMatricesHandle);

			vertices.setVertices(vertexBuffer, 0, bufferIndex);
			vertices.bind();
			vertices.draw(GLES20.GL_TRIANGLES, 0, numSprites * INDICES_PER_SPRITE);
			vertices.unbind();
		}
	}

	public void drawSprite(Sprite sprite) {
		final float[] mMVPMatrix = new float[16];
		Matrix.setIdentityM(mMVPMatrix, 0);

		this.drawSprite(sprite.getWorldX(), sprite.getWorldScaleY(), sprite.getWidth(), sprite.getHeight(),
				sprite.getTextureRegion(), mMVPMatrix);
	}

	public void drawSprite(float x, float y, float width, float height, TextureRegion region, float[] modelMatrix) {
		if (numSprites == maxSprites) {
			endBatch();
			numSprites = 0;
			bufferIndex = 0;
		}

		float halfWidth = width / 2.0f;
		float halfHeight = height / 2.0f;
		float x1 = x - halfWidth;
		float y1 = y - halfHeight;
		float x2 = x + halfWidth;
		float y2 = y + halfHeight;

		vertexBuffer[bufferIndex++] = x1;
		vertexBuffer[bufferIndex++] = y1;
		vertexBuffer[bufferIndex++] = region.u1;
		vertexBuffer[bufferIndex++] = region.v2;
		vertexBuffer[bufferIndex++] = numSprites;

		vertexBuffer[bufferIndex++] = x2;
		vertexBuffer[bufferIndex++] = y1;
		vertexBuffer[bufferIndex++] = region.u2;
		vertexBuffer[bufferIndex++] = region.v2;
		vertexBuffer[bufferIndex++] = numSprites;

		vertexBuffer[bufferIndex++] = x2;
		vertexBuffer[bufferIndex++] = y2;
		vertexBuffer[bufferIndex++] = region.u2;
		vertexBuffer[bufferIndex++] = region.v1;
		vertexBuffer[bufferIndex++] = numSprites;

		vertexBuffer[bufferIndex++] = x1;
		vertexBuffer[bufferIndex++] = y2;
		vertexBuffer[bufferIndex++] = region.u1;
		vertexBuffer[bufferIndex++] = region.v1;
		vertexBuffer[bufferIndex++] = numSprites;

		Matrix.multiplyMM(mMVPMatrix, 0, mVPMatrix, 0, modelMatrix, 0);

		for (int i = 0; i < 16; ++i) {
			uMVPMatrices[numSprites * 16 + i] = mMVPMatrix[i];
		}

		numSprites++;
	}
}
