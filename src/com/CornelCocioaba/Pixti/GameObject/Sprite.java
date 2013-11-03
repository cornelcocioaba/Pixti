package com.CornelCocioaba.Pixti.GameObject;

import java.nio.FloatBuffer;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.CornelCocioaba.Pixti.Engine.Camera;
import com.CornelCocioaba.Pixti.OpenGL.Color;
import com.CornelCocioaba.Pixti.OpenGL.Texture;
import com.CornelCocioaba.Pixti.OpenGL.TextureShaderProgram;
import com.CornelCocioaba.Pixti.Utils.BufferUtils;

public class Sprite extends AbstractRectangle{

	private final TextureShaderProgram mProgram;
	private final float[] mMVPMatrix = new float[16];
	private Texture texture;
	private int textureId;
	
	float[] uvs = new float[]{
			0.0f, 1.0f,
			0.0f, 0.0f,
			1.0f, 0.0f,
			1.0f, 1.0f,
	};
	private final FloatBuffer uvBuffer;
	
	public Sprite(Context context, float x, float y, float width, float height, String path) {
		this(context, x, y, width, height, Color.WHITE, Anchor.MIDDLE_CENTER, path);
	}

	public Sprite(Context context, float x, float y, float width, float height, Color color, Anchor anchor, String path) {
		super(x, y, width, height, color, anchor);
		
		uvBuffer = BufferUtils.createFloatBuffer(uvs);
		mProgram = new TextureShaderProgram();
		texture = new Texture(context, path);
		textureId = texture.load();
	}
	
	@Override
	public void Draw(Camera cam) {
		mProgram.useProgram();

		Matrix.setIdentityM(mMVPMatrix, 0);
		Matrix.translateM(mMVPMatrix, 0, x, y, 0);
		Matrix.rotateM(mMVPMatrix, 0, mAngle, 0, 0, -1.0f);
		Matrix.scaleM(mMVPMatrix, 0, scaleX, scaleY, 0);
		Matrix.multiplyMM(mMVPMatrix, 0, cam.mCombinedMatrix, 0, mMVPMatrix, 0);

		mProgram.setUniforms(mMVPMatrix, textureId);
		mProgram.setVertexBufferPointer(vertexBuffer);
		mProgram.enablePositionAttribute();
		mProgram.enableTextureCoordinatesAttribute();
		mProgram.setTextureCoordinatesPointer(uvBuffer);
		
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, triangles.length, GLES20.GL_UNSIGNED_SHORT, this.triangleBuffer);
		//GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);
		
		GLES20.glDisable(GLES20.GL_BLEND);
		
		mProgram.disablePositionAttribute();
		mProgram.disableTextureCoordinatesAttribute();
	}
	
}
