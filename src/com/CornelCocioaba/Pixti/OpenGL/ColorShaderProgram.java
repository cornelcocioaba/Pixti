package com.CornelCocioaba.Pixti.OpenGL;

import static android.opengl.GLES20.glDisableVertexAttribArray;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;

import java.nio.FloatBuffer;

import android.opengl.GLES20;

public class ColorShaderProgram extends ShaderProgram {

	private static final String DEFAULT_COLOR_VERTEX_SHADER = "uniform mat4 " + U_MATRIX + ";" 
			+ "attribute vec4 "	+ A_POSITION + ";" 
			+ "attribute vec4 " + A_COLOR + ";"
			+ "varying vec4 " + V_COLOR + ";"
			+ "void main() { " 
			+ 	V_COLOR + " = " + A_COLOR + ";"
			+ 	"gl_Position = " + U_MATRIX + " * " + A_POSITION + ";"
			+ "}";

	private static final String DEFAULT_COLOR_FRAGMENT_SHADER = 
			"precision mediump float; " 
			+ "varying vec4 " + V_COLOR + ";"
			+ "void main() {"
			+ " gl_FragColor = " + V_COLOR + ";" 
			+ "}";

	// Uniform locations
	public final int uMatrixLocation;

	// Attribute locations
	public final int aPositionLocation;
	public final int aColorLocation;

	public ColorShaderProgram() {
		this(DEFAULT_COLOR_VERTEX_SHADER, DEFAULT_COLOR_FRAGMENT_SHADER);
	}

	public ColorShaderProgram(String vertexShader, String fragmentShader) {
		super(vertexShader, fragmentShader);

		// Retrieve uniform locations for the shader program.
		uMatrixLocation = glGetUniformLocation(program, U_MATRIX);

		// Retrieve attribute locations for the shader program.
		aPositionLocation = glGetAttribLocation(program, A_POSITION);
		aColorLocation = glGetAttribLocation(program, A_COLOR);
	}

	public void setUniforms(float[] matrix) {
		// Pass the matrix into the shader program.
		glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
	}

	public void setVertexBufferPointer(FloatBuffer vertexBuffer) {
		GLES20.glVertexAttribPointer(aPositionLocation, GLConstants.COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0,
				vertexBuffer);
	}
	
	public void setColorBufferPointer(FloatBuffer colorBuffer) {
		GLES20.glVertexAttribPointer(aColorLocation, GLConstants.COLORS_PER_VERTEX, GLES20.GL_FLOAT, false, 0,
				colorBuffer);
	}

	public void enablePositionAttribute() {
		glEnableVertexAttribArray(aPositionLocation);
	}
	
	public void disablePositionAttribute(){
		glDisableVertexAttribArray(aPositionLocation);
	}

	public void enableColorAttribute() {
		glEnableVertexAttribArray(aColorLocation);
	}
	
	public void disableColorAttribute(){
		glDisableVertexAttribArray(aColorLocation);
	}
}
