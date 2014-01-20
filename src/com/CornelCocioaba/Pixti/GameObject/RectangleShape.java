package com.CornelCocioaba.Pixti.GameObject;

import com.CornelCocioaba.Pixti.Graphics.Color;
import com.CornelCocioaba.Pixti.Graphics.GLConstants;

public class RectangleShape extends Shape {

	public float width, height;
	public float scaleX, scaleY;
	public float mAngle;
	private Color mColor;

	protected static short triangles[] = new short[] { 0, 1, 2, 0, 2, 3 };
	
	public RectangleShape(float x, float y, float width, float height){
		this(x, y, width, height, Color.WHITE);
	}
	
	public RectangleShape(float x, float y, float width, float height, Color color){
		this(x, y, width, height, color, Anchor.BOTTOM_LEFT);
	}
	
	public RectangleShape(float x, float y, float width, float height, Color color, Anchor anchor){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.scaleX = 1;
		this.scaleY = 1;
		this.mAngle = 0;
		this.mColor = color;
		this.setVertices(this.getVerts(width, height, anchor));
		this.setColor(color);
		this.setTriangles(triangles);
	}
	
	public float getWidth(){
		return width;
	}
	
	public float getHeight(){
		return height;
	}
	
	public float[] getVerts(float width, float height) {
		return new float[] { 0, 0, 0, height, width, height, width, 0 };
	}
	
	public float[] getVerts(float width, float height, Anchor anchor) {
		float[] vertices;
		switch(anchor){
		case BOTTOM_LEFT:
			vertices = new float[] { 0, 0, 0, height, width, height, width, 0 };
			break;
		case BOTTOM_CENTER:
			vertices = new float[] { -width * 0.5f, 0, -width * 0.5f, height, width * 0.5f, height, width * 0.5f, 0 };
			break;
		case BOTTOM_RIGHT:
			vertices = new float[] { -width, 0, -width, height, 0, height, 0, 0 };
			break;
		case MIDDLE_LEFT:
			vertices = new float[] { 0, -height * 0.5f, 0, height * 0.5f , width, height * 0.5f, width, -height * 0.5f };
			break;
		case MIDDLE_CENTER:
			vertices = new float[] { -width * 0.5f, -height * 0.5f, -width * 0.5f, height * 0.5f , width * 0.5f, height * 0.5f, width * 0.5f, -height * 0.5f };
			break;
		case MIDDLE_RIGHT:
			vertices = new float[] { -width, -height * 0.5f, -width, height * 0.5f , 0, height * 0.5f, 0, -height * 0.5f };
			break;
		case TOP_LEFT:
			vertices = new float[] { 0, -height, 0, 0, width, 0, width, -height };
			break;
		case TOP_CENTER:
			vertices = new float[] { -width * 0.5f, -height, -width * 0.5f, 0, width * 0.5f, 0, width * 0.5f, -height };
			break;
		case TOP_RIGHT:
			vertices = new float[] { -width, -height, -width, 0, 0, 0, 0, -height };
			break;
		default:
			vertices = new float[] { 0, 0, 0, height, width, height, width, 0 };
		}
		return vertices;
	}
	
	public void setColor(Color color){
		mColor = color;
		setColors(vertexColors(mColor));
	}
	
	public void setScale(float scale){
		this.scaleX = scale;
		this.scaleY = scale;
	}
	
	public void setGradientColor(Color from, Color to){
		float[] colors = new float[]{
				from.getRed(), from.getGreen(), from.getBlue(), from.getAlpha(),
				from.getRed(), from.getGreen(), from.getBlue(), from.getAlpha(),
				to.getRed(), to.getGreen(), to.getBlue(), to.getAlpha(),
				to.getRed(), to.getGreen(), to.getBlue(), to.getAlpha()
		};
		setColors(colors);
	}
	
	public void setColors(Color bottomLeft, Color topLeft, Color topRight, Color bottomRight){
		float[] colors = new float[ 4 * GLConstants.COLORS_PER_VERTEX];
		System.arraycopy(bottomLeft.getColorAsFloatArray(), 0,colors, 0,  4);
		System.arraycopy(topLeft.getColorAsFloatArray(), 0, colors, 4, 4);
		System.arraycopy(topRight.getColorAsFloatArray(), 0,colors, 8,  4);
		System.arraycopy(bottomRight.getColorAsFloatArray(), 0, colors, 12, 4);
		setColors(colors);
	}

	float[] vertexColors(Color color){
		return new float[] { 
				color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha(),
				color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha(), 
				color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha(), 
				color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() 
				};
	}

	public enum Anchor {
		
		BOTTOM_LEFT, 
		BOTTOM_CENTER, 
		BOTTOM_RIGHT,
		MIDDLE_LEFT, 
		MIDDLE_CENTER, 
		MIDDLE_RIGHT, 
		TOP_LEFT, 
		TOP_CENTER, 
		TOP_RIGHT
	}
	
	public boolean collidesWith(RectangleShape other){
		
		//only works for middle center anchor
		
		float minX = x - width * 0.5f;
		float otherMinX = other.x - other.width * 0.5f;
		
		float maxX = x + width * 0.5f;
		float otherMaxX = other.x + other.width * 0.5f;
		
		float minY = y - height * 0.5f;
		float otherMinY = other.y - height * 0.5f;
		
		float maxY =  y + height * 0.5f;;
		float otherMaxY = other.y + height * 0.5f;
		
		
		return !(maxX < otherMinX || otherMaxX < minX || maxY < otherMinY || otherMaxY < minY);
	}
	

}
