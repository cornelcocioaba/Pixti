package com.CornelCocioaba.Pixti.GameObject;

import com.CornelCocioaba.Pixti.OpenGL.TextureRegion;

public class AnimatedSprite extends Sprite {

	protected TextureRegion[] textureRegions;
	protected int currentTexture;
	protected double duration, lastChange;

	public AnimatedSprite(int x, int y, double duration, TextureRegion... regions) {
		super(regions[0], x, y);

		this.textureRegions = regions;
		this.currentTexture = 0;
		this.duration = duration;
	}

	@Override
	public void Update() {

		super.Update();

		if (System.currentTimeMillis() - lastChange >= duration) {
			textureRegion = textureRegions[currentTexture];

			lastChange = System.currentTimeMillis();

			currentTexture++;
			if (currentTexture >= textureRegions.length)
				currentTexture = 0;
		}
	}
}
