package com.CornelCocioaba.Pixti.AndroidInvaders;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import com.CornelCocioaba.Pixti.Engine.Camera;
import com.CornelCocioaba.Pixti.GameObject.HUD;
import com.CornelCocioaba.Pixti.GameObject.Scene;
import com.CornelCocioaba.Pixti.OpenGL.Texture;
import com.CornelCocioaba.Pixti.OpenGL.TextureRegion;
import com.CornelCocioaba.Pixti.Utils.Debug;
import com.android.texample2.Font;
import com.android.texample2.Text;

public class InvadersScene extends Scene {

	private Ship ship;
	private TextureRegion shipTextureRegion;
	private Font defaultFont;
	
	private TextureRegion projectileRegion;

	
	private boolean initialized = false;

	public InvadersScene(Context context) {
		super(context);
	}

	@Override
	public void onLoadResources() {
		Debug.logInfo("Loading Resources ...");
		
		Texture shipTexture = new Texture(mContext, "ship.png");
		shipTextureRegion = new TextureRegion(shipTexture);
		shipTexture.load();
		
		defaultFont = new Font(mContext.getAssets());
		defaultFont.load("Roboto-Regular.ttf", 20, 2, 2);
		
		Texture projectileTexture = new Texture(mContext, "projectile.png");
		projectileRegion = new TextureRegion(projectileTexture);
		projectileTexture.load();
	}
	
	@Override
	public void onCreate(int width, int height) {
		Debug.logInfo("Creating Scene ...");
		
		mMainCamera = new Camera(width, height);
		mHud = new HUD(width, height);

		ship = new Ship(width * 0.5f, shipTextureRegion.getHeight(), shipTextureRegion);
		this.addChild(ship);
		ship.setLimits(0, width);
		Projectile projectilePrototype = new Projectile(0, 0, projectileRegion, null);
		ship.createPool(projectilePrototype);
		
		Text score = new Text(500, 650, "Score: 0000", defaultFont);
		mHud.addChild(score);

		initialized = true;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (!initialized) return true;

		int pointerIndex = event.getActionIndex();

		int pointerId = event.getPointerId(pointerIndex);

		int maskedAction = event.getActionMasked();

		switch (maskedAction) {

		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN: {
			if (event.getX(pointerIndex) < mMainCamera.getViewportWidth() * 0.5f) {
				ship.moveLeft();
			} else {
				ship.moveRight();
			}
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			break;
		}
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
		case MotionEvent.ACTION_CANCEL: {
			int count = event.getPointerCount();
			if (event.getPointerCount() > 1) {
				for (int i = 0; i < count; i++) {
					if (event.getPointerId(i) == pointerId)
						continue;
					else {
						if (event.getX(i) < mMainCamera.getViewportWidth() * 0.5f) {
							ship.moveLeft();
						} else {
							ship.moveRight();
						}
					}
				}
			} else {
				ship.stop();
			}
			break;
		}
		}

		return true;
	}
}
