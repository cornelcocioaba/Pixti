package com.CornelCocioaba.Pixti.OpenGL;

import static android.opengl.GLES20.glGetUniformLocation;

import com.CornelCocioaba.Pixti.GameObject.Shape;

public class Triangle extends Shape {


	private int mProgramId;
	private int mPositionHandle;
	private int mColorHandle;
	private int uColorLocation;

	private static final String U_COLOR = "u_Color";

	public Triangle(float[] vertices, short[] triangles, float[] colors) {
		
		super(vertices, new short[]{0,1,2}, colors);
	}
	
	
	public void init() {
		uColorLocation = glGetUniformLocation(mProgramId, U_COLOR);
	}

}
