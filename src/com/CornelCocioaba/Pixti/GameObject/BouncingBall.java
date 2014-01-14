package com.CornelCocioaba.Pixti.GameObject;

public class BouncingBall extends CircleShape {

	//world box
	public float minX, maxX, minY = 0, maxY = 720;
	
	public float gravity = -900f;
	public float damping = 0.65f;
	
	private float dx, dy;
	
	float delta = 1/60f;
	
	public BouncingBall(float x, float y, float radius) {
		super(x, y, radius);
	}
	
	@Override
	public void Update() {
		// dv = a * dt;
        // v1 = v0 + dv;
        // dx = v0 * dt + dv * dt / 2
		
		
		if(y < minY + radius){
			y = minY + radius;
			dy *= damping;
			dy = -dy;
		}
		else{
			dy += gravity * delta;
			
			y += dy *delta + delta *delta *gravity * 0.5f;
		}
		
		super.Update();
	}

}
