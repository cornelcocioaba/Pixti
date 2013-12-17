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
	
	protected int vertexCount;
	
	public void setVertices(float[] vertices){
		mVertices = vertices;
		updateVertexBuffer();
	}
	
	public void updateVertexBuffer(){
		vertexBuffer = BufferUtils.createFloatBuffer(mVertices);
	}
	
	public void setTriangles(short[] triangles){
		mTriangles = triangles;
		updateTriangleBuffer();
	}
	
	public void updateTriangleBuffer(){
		triangleBuffer = BufferUtils.createShortBuffer(mTriangles);		
	}
	
	public void setColors(float[] colors){
		mVertexColors = colors;
		updateColorBuffer();
	}
	
	public void updateColorBuffer(){
		colorBuffer = BufferUtils.createFloatBuffer(mVertexColors);
	}
}
