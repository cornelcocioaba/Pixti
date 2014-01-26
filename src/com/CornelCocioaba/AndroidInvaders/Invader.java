package com.CornelCocioaba.AndroidInvaders;

import com.CornelCocioaba.Pixti.GameObject.Sprite;
import com.CornelCocioaba.Pixti.Graphics.TextureRegion;
import com.CornelCocioaba.Pixti.Utils.Time;

public class Invader extends Sprite {

	public IOnFireEvent fireEvent;
	boolean canFire;
	float timer;

	public Invader(float x, float y, TextureRegion textureRegion) {
		super(x, y, textureRegion);
	}

	public void fire() {
		canFire = true;
		timer = 0;
	}

	@Override
	public void Update() {
		if (canFire) {
			timer += Time.deltaTime;
			if (timer > 4) {
				canFire = false;
				
				if (fireEvent != null)
					fireEvent.OnFireEvent(Invader.this);
			}
		}
	}
}
