package com.CornelCocioaba.Pixti.GameObject;

import com.CornelCocioaba.Pixti.OpenGL.Color;

public abstract class UniformColoredShape extends Shape {

	protected Color color;

	public void setColor(Color pColor) {
		this.color = pColor;
		updateColor();
	}

	protected void updateColor() {
		this.mVertexColors = new float[vertexCount * 4];
		for (int i = 0; i < vertexCount * 4; i += 4) {
			this.mVertexColors[i] = color.getRed();
			this.mVertexColors[i+1] = color.getGreen();
			this.mVertexColors[i+2] = color.getBlue();
			this.mVertexColors[i+3] = color.getAlpha();
		}
		
		this.updateColorBuffer();
	}
}
