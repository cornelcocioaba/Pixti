package com.CornelCocioaba.AndroidInvaders;

import com.CornelCocioaba.Pixti.Core.Time;
import com.CornelCocioaba.Pixti.GameObject.Sprite;
import com.CornelCocioaba.Pixti.Graphics.TextureRegion;
import com.CornelCocioaba.Pixti.Utils.Math.Mathf;

public class Ship extends Sprite {

	public static int CENTER_GUN_OFFSET = 41;
	public static final String NAME = "Andromeda";
	public static final String PROJECTILE_NAME = "ShipProjectile";

	public float speed = 400f;

	private float direction = 0f;
	private float leftLimit;
	private float rightLimit;

	private float lastFireTime;
	
	IOnFireEvent mOnFireCallback;
	public boolean invulnerable;

	public Ship(float x, float y, TextureRegion textureRegion) {
		this(x, y, textureRegion, null);
	}

	public Ship(float x, float y, TextureRegion textureRegion, IOnFireEvent callback) {
		super(x, y, textureRegion);
		
		this.name = Ship.NAME;
		this.mOnFireCallback = callback;
		this.invulnerable = false;
	}

	public void setLimits(float left, float right) {
		this.leftLimit = left;
		this.rightLimit = right;
	}


	public void moveLeft() {
		direction = -1;
	}

	public void moveRight() {
		direction = 1;
	}

	public void stop() {
		direction = 0;
	}

	private void fire() {
		
		if(mOnFireCallback != null){
			mOnFireCallback.OnFireEvent(this);
		}
	}

	@Override
	public void Update() {

		x += speed * direction * Time.deltaTime;
		x = Mathf.Clamp(x, leftLimit, rightLimit);

		if (Time.time - lastFireTime > 0.7f) {
			lastFireTime = Time.time;
			fire();
		}
	}

}
