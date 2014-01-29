package com.CornelCocioaba.AndroidInvaders;

import com.CornelCocioaba.Pixti.Core.Time;
import com.CornelCocioaba.Pixti.GameObject.Sprite;
import com.CornelCocioaba.Pixti.Graphics.TextureRegion;
import com.CornelCocioaba.Pixti.Utils.Direction;

public class Projectile extends Sprite {

	private boolean fire;
	public float velocity = 400f;
	public Direction direction;

	public Projectile(float x, float y, TextureRegion textureRegion) {
		super(x, y, textureRegion);
	}

	public void start(float x, float y, Direction direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		fire = true;
	}

	@Override
	public void Update() {
		if (fire) {
			if (direction == Direction.UP)
				y += velocity * Time.deltaTime;
			else
				y -= velocity * Time.deltaTime;
				
		}
	}
}
