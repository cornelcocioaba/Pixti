package com.CornelCocioaba.Pixti.GameObject;

import com.CornelCocioaba.Pixti.Graphics.TextureRegion;

public class AnimatedSprite extends Sprite {

	protected TextureRegion[] textureRegions;
	protected int currentTexture;
	protected double duration, lastChange;

	public AnimatedSprite(float x, float y, double duration, TextureRegion... regions) {
		super( x, y,regions[0]);

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
