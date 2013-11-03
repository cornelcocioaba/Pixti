package com.CornelCocioaba.Pixti.Engine;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_NO_ERROR;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetError;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glUniform4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.Matrix.orthoM;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.util.Log;

import com.CornelCocioaba.Pixti.OpenGL.ShaderHelper;
import com.CornelCocioaba.Pixti.Utils.BufferUtils;

public class TestRenderer implements Renderer {

	Triangle mTriangle;
	private final float[] mMVPMatrix = new float[16];
	private final float[] mProjMatrix = new float[16];
	private final float[] mVMatrix = new float[16];
	private final float[] mRotationMatrix = new float[16];

	public volatile float mAngle;

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {

		// Set the background frame color
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		mTriangle = new Triangle();
	}

	@Override
	public void onDrawFrame(GL10 unused) {

		// Draw background color
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		// Set the camera position (View matrix)
		Matrix.setLookAtM(mVMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

		// Calculate the projection and view transformation
		Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);

		Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, -1.0f);

		// Combine the rotation matrix with the projection and camera view
		Matrix.multiplyMM(mMVPMatrix, 0, mRotationMatrix, 0, mMVPMatrix, 0);

		// Draw triangle
		mTriangle.draw(mMVPMatrix);
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		GLES20.glViewport(0, 0, width, height);

		final float aspectRatio = width > height ? (float) width / (float) height : (float) height / (float) width;
		if (width > height) {
			orthoM(mProjMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
		} else {
			orthoM(mProjMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
		}

	}

	public static int loadShader(int type, String shaderCode) {

		int shader = glCreateShader(type);

		glShaderSource(shader, shaderCode);
		glCompileShader(shader);

		return shader;
	}

	public static void checkGlError(String glOperation) {
		int error;
		while ((error = glGetError()) != GL_NO_ERROR) {
			Log.e("TestRenderer", glOperation + ": glError " + error);
			throw new RuntimeException(glOperation + ": glError " + error);
		}
	}

	class Triangle {

		private static final String U_MATRIX = "u_Matrix";
		private static final String A_POSITION = "a_Position";
		private static final String U_COLOR = "u_Color";

		private final String vertexShaderCode = "uniform mat4 u_Matrix;" + "attribute vec4 a_Position;"
				+ "void main() {" + "  gl_Position = a_Position * u_Matrix;" + "}";

		private final String fragmentShaderCode = "precision mediump float;" + "uniform vec4 u_Color;"
				+ "void main() {" + "  gl_FragColor = u_Color;" + "}";

		private final FloatBuffer vertexBuffer;
		private final int mProgram;
		private int mPositionHandle;
		private int mColorHandle;
		private int mMVPMatrixHandle;

		// number of coordinates per vertex in this array
		static final int COORDS_PER_VERTEX = 3;
		float triangleCoords[] = { // in counterclockwise order:
		0.0f, 0.622008459f, 0.0f, // top
				-0.5f, -0.311004243f, 0.0f, // bottom left
				0.5f, -0.311004243f, 0.0f // bottom right
		};
		private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
		private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per
																// vertex

		// Set color with red, green, blue and alpha (opacity) values
		float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

		public Triangle() {
			vertexBuffer = BufferUtils.createFloatBuffer(triangleCoords);

			mProgram = ShaderHelper.buildProgram(vertexShaderCode, fragmentShaderCode);
		}

		public void draw(float[] mvpMatrix) {

			glUseProgram(mProgram);
			mPositionHandle = glGetAttribLocation(mProgram, A_POSITION);

			// Enable a handle to the triangle vertices
			glEnableVertexAttribArray(mPositionHandle);

			// Prepare the triangle coordinate data
			glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GL_FLOAT, false, vertexStride, vertexBuffer);

			// get handle to fragment shader's u_Color member
			mColorHandle = glGetUniformLocation(mProgram, U_COLOR);

			// Set color for drawing the triangle
			glUniform4fv(mColorHandle, 1, color, 0);
			// get handle to shape's transformation matrix
			mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, U_MATRIX);
			TestRenderer.checkGlError("glGetUniformLocation");

			// // Apply the projection and view transformation
			GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
			TestRenderer.checkGlError("glUniformMatrix4fv");

			// Draw the triangle
			glDrawArrays(GL_TRIANGLES, 0, vertexCount);

			// Disable vertex array
			GLES20.glDisableVertexAttribArray(mPositionHandle);
		}
	}

}
