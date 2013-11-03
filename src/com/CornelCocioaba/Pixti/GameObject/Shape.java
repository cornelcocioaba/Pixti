package com.CornelCocioaba.Pixti.GameObject;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import com.CornelCocioaba.Pixti.Utils.BufferUtils;


public abstract class Shape extends GameObject{
	
	protected float[] mVertices;
	protected short[] mTriangles;
	protected float[] mVertexColors;
	
	protected FloatBuffer vertexBuffer;
	protected ShortBuffer triangleBuffer;
	protected FloatBuffer colorBuffer;
	
	public Shape(float[] vertices, short[] triangles, float[] colors){
		setVertices(vertices);
		setTriangles(triangles);
		
		mVertexColors = colors;
		
		vertexBuffer = BufferUtils.createFloatBuffer(vertices);
		triangleBuffer = BufferUtils.createShortBuffer(triangles);
		colorBuffer = BufferUtils.createFloatBuffer(colors);
	}
	
	private void setVertices(float[] vertices){
		mVertices = vertices;
	}
	
	private void setTriangles(short[] triangles){
		mTriangles = triangles;
	}
	
	public void setColors(float[] colors){
		mVertexColors = colors;
		colorBuffer.put(mVertexColors);
		colorBuffer.position(0);
	}
}
