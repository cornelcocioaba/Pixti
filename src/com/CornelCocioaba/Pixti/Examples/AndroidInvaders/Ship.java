package com.CornelCocioaba.Pixti.Examples.AndroidInvaders;

import com.CornelCocioaba.Pixti.Engine.Time;
import com.CornelCocioaba.Pixti.GameObject.Sprite;
import com.CornelCocioaba.Pixti.Graphics.TextureRegion;

public class Ship extends Sprite {

	private static int CENTER_GUN_OFFSET = 41;
	public static final String NAME = "Andromeda";
	public static final String PROJECTILE_NAME = "ShipProjectile";

	public float speed = 400f;

	private float direction = 0f;
	private float leftLimit;
	private float rightLimit;

	private ProjectilePool projectilePool;

	private float lastFireTime;
	
	OnFireCallback mOnFireCallback;

	public Ship(float x, float y, TextureRegion textureRegion) {
		this(x, y, textureRegion, null);
	}

	public Ship(float x, float y, TextureRegion textureRegion, OnFireCallback callback) {
		super(x, y, textureRegion);
		
		this.name = Ship.NAME;
		this.mOnFireCallback = callback;
	}

	public void setLimits(float left, float right) {
		this.leftLimit = left;
		this.rightLimit = right;
	}

	public void createPool(Projectile prototype) {
		projectilePool = new ProjectilePool(prototype, 10);
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
			mOnFireCallback.OnFireEvent();
		}
		
		Projectile pr = projectilePool.obtainPoolItem();
		pr.name = PROJECTILE_NAME;
		pr.start(this.x, this.y + CENTER_GUN_OFFSET);
		this.addChild(pr);
	}

	@Override
	public void Update() {

		x += speed * direction * Time.deltaTime;
		x = Clamp(x, leftLimit, rightLimit);

		if (Time.time - lastFireTime > 0.7f) {
			lastFireTime = Time.time;
			fire();
		}

		super.Update();
	}

	float Clamp(float value, float min, float max) {
		if (value < min)
			return min;

		if (value > max)
			return max;

		return value;
	}

	public interface OnFireCallback {
		public void OnFireEvent();
	}

}
