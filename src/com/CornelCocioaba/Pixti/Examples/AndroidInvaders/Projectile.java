package com.CornelCocioaba.Pixti.Examples.AndroidInvaders;

import com.CornelCocioaba.Pixti.Engine.Time;
import com.CornelCocioaba.Pixti.GameObject.Sprite;
import com.CornelCocioaba.Pixti.Graphics.TextureRegion;

public class Projectile extends Sprite {

	private boolean fire;
	public float velocity = 400f;
	private ProjectilePool pool;
	
	public Projectile(float x, float y, TextureRegion textureRegion, ProjectilePool projectilePool) {
		super(x, y, textureRegion);
		this.pool = projectilePool;
	}

	public void start(float x, float y) {
		this.x = x;
		this.y = y;
		
		fire = true;
	}
	
	public void recycle(){
		pool.recyclePoolItem(this);
	}
	
	@Override
	public void Update() {
		if(fire){
			y += velocity * Time.deltaTime;
		}
		if(y > 720) this.recycle();
	}
}
