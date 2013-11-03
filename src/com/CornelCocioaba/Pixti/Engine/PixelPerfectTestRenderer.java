package com.CornelCocioaba.Pixti.Engine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import com.CornelCocioaba.Pixti.OpenGL.ShaderHelper;
import com.CornelCocioaba.Pixti.Utils.BufferUtils;

public class PixelPerfectTestRenderer implements Renderer {
	private final float[] mProjectionMatrix = new float[16];
	private final float[] mViewMatrix = new float[16];
	private final float[] mProjectionAndViewMatrix = new float[16];

	public static float vertices[];
	public static short indices[];
	public static float uvs[];
	public FloatBuffer vertexBuffer;
	public ShortBuffer drawListBuffer;
	public FloatBuffer uvBuffer;

	float mScreenWidth = 1280;
	float mScreenHeight = 768;

	Context mContext;
	long mLastTime;
	int mProgram;
	int mColorProgram;
	
	public static final String vs_SolidColor =
			"uniform mat4 uMVPMatrix;"+
			"attribute vec4 vPosition;"+
			"void main(){"+
			"	gl_Position = uMVPMatrix * vPosition;" + 
			"}";
	
	public static final String fs_SolidColor =
			"precision mediump float;"+
			"void main(){"+
			"gl_FragColor = vec4(0.5,0,0,1);"+
			"}";
	
	public static final String vs_Image =
			"uniform mat4 uMVPMatrix;" +
	
			"attribute vec4 vPosition;" +
			"attribute vec2 a_texCoord;" +
			
			"varying vec2 v_texCoord;" +
			
			"void main(){" +
			"		v_texCoord = a_texCoord;" +
			"		gl_Position = uMVPMatrix * vPosition;" +
			"}";
	
	public static final String fs_Image =
			"precision mediump float;" +
	
			"uniform sampler2D s_texture;" +
			"varying vec2 v_texCoord;" +
			"void main(){" +
			"	gl_FragColor = texture2D(s_texture, v_texCoord);" +
			"}";
	
	public PixelPerfectTestRenderer(Context context) {
		mContext = context;
		mLastTime = System.currentTimeMillis() + 100;
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		long now = System.currentTimeMillis();

		if (mLastTime > now)
			return;

		long elapsed = now - mLastTime;

		Render(mProjectionAndViewMatrix);

		mLastTime = now;
	}

	private void Render(float[] m){
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		
		int positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
		GLES20.glEnableVertexAttribArray(positionHandle);
		GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
		
		int texCoordHandle = GLES20.glGetAttribLocation(mProgram, "a_texCoord");
		GLES20.glEnableVertexAttribArray(texCoordHandle);
		GLES20.glVertexAttribPointer(texCoordHandle, 2, GLES20.GL_FLOAT, false, 0, uvBuffer);
		
		int mvpHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		GLES20.glUniformMatrix4fv(mvpHandle, 1, false, m, 0);
		
		int samplerHandle = GLES20.glGetUniformLocation(mProgram, "s_Texture");
		GLES20.glUniform1i(samplerHandle, 0);
		
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
//		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
		
		GLES20.glDisableVertexAttribArray(positionHandle);
		GLES20.glDisableVertexAttribArray(texCoordHandle);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		mScreenWidth = width;
		mScreenHeight = height;

		GLES20.glViewport(0, 0, (int) mScreenWidth, (int) mScreenHeight);

		for (int i = 0; i < 16; i++) {
			mProjectionMatrix[i] = 0;
			mViewMatrix[i] = 0;
			mProjectionAndViewMatrix[i] = 0;
		}
		
		Matrix.orthoM(mProjectionMatrix, 0, 0f, mScreenWidth, 0f, mScreenHeight, 0, 50);
		
		Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
		
		Matrix.multiplyMM(mProjectionAndViewMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
//		SetupTriangle();
//		SetupSquare();
		SetupImage();
		GLES20.glClearColor(0.0f, 0.0f, 0.5f, 1.0f);
		
//		mColorProgram = ShaderHelper.buildProgram(vs_SolidColor, fs_SolidColor);
		
		mProgram = ShaderHelper.buildProgram(vs_Image, fs_Image);
		GLES20.glUseProgram(mProgram);
	}
	
	public void SetupTriangle(){
		vertices = new float[]{
				10.0f, 200f, 0.0f,
				10.0f, 100f, 0.0f,
				100f, 100f, 0.0f
		};
		
		indices = new short[]{0,1,2};
		
		ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
		bb.order(ByteOrder.nativeOrder());
		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2);
		dlb.order(ByteOrder.nativeOrder());
		drawListBuffer = dlb.asShortBuffer();
		drawListBuffer.put(indices);
		drawListBuffer.position(0);
	}
	
	public void SetupSquare(){
		vertices = new float[]{
				0.0f, 0.0f, 0.0f,
				0.0f, 360f, 0.0f,
				640f, 360f, 0.0f,
				640f, 0.0f, 0.0f
		};
		
		indices = new short[]{0, 1, 2 , 0, 2, 3};
		
		ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
		bb.order(ByteOrder.nativeOrder());
		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2);
		dlb.order(ByteOrder.nativeOrder());
		drawListBuffer = dlb.asShortBuffer();
		drawListBuffer.put(indices);
		drawListBuffer.position(0);
	}
	
	public void SetupImage(){
		
		vertices = new float[]{
				0.0f, 512f, 0.0f,
				0.0f, 0.0f, 0.0f,
				512f, 0.0f, 0.0f,
				512f, 512f, 0.0f
		};
		
		indices = new short[]{0, 1, 2 , 0, 2, 3};
		
		uvs = new float[]{
				0.0f, 0.0f,
				0.0f, 1.0f,
				1.0f, 1.0f,
				1.0f, 0.0f
		};
		vertexBuffer = BufferUtils.createFloatBuffer(vertices);
		drawListBuffer = BufferUtils.createShortBuffer(indices);
		uvBuffer = BufferUtils.createFloatBuffer(uvs);
		
		int[] texturenames = new int[1];
		GLES20.glGenTextures(1, texturenames, 0);
		
		if(texturenames [0] == 0){
			Log.w("PPRenderer" , "Could not generate texture object");
		}
		
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[0]);
		
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
		
		Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.raw.ic_launcher);
		
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0 , bitmap, 0);
		
		bitmap.recycle();
		
	}

}
