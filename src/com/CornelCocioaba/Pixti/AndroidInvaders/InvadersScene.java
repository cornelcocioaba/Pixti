package com.CornelCocioaba.Pixti.AndroidInvaders;

import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.view.MotionEvent;
import android.view.View;

import com.CornelCocioaba.Pixti.Pixti;
import com.CornelCocioaba.Pixti.AndroidInvaders.Ship.OnFireCallback;
import com.CornelCocioaba.Pixti.Engine.Camera;
import com.CornelCocioaba.Pixti.Engine.Time;
import com.CornelCocioaba.Pixti.GameObject.GameObject;
import com.CornelCocioaba.Pixti.GameObject.HUD;
import com.CornelCocioaba.Pixti.GameObject.RectangleShape;
import com.CornelCocioaba.Pixti.GameObject.Scene;
import com.CornelCocioaba.Pixti.GameObject.Sprite;
import com.CornelCocioaba.Pixti.OpenGL.Color;
import com.CornelCocioaba.Pixti.OpenGL.Texture;
import com.CornelCocioaba.Pixti.OpenGL.TextureRegion;
import com.CornelCocioaba.Pixti.Utils.Debug;
import com.android.texample2.Font;
import com.android.texample2.Text;

public class InvadersScene extends Scene {

	private SoundPool soundPool;
	private int fireSoundID;
	private int deathSoundID;
	private boolean loaded = false;
	
	private Ship ship;
	private TextureRegion shipTextureRegion;
	private TextureRegion blueEnemyTextureRegion;
	private TextureRegion purpleEnemyTextureRegion;
	private TextureRegion yellowEnemyTextureRegion;

	private Font defaultFont;
	private Text scoreText;

	private TextureRegion projectileRegion;

	private boolean initialized = false;
	
	public int score = 0;

	public InvadersScene(Context context) {
		super(context);
	}

	@Override
	public void onLoadResources() {
		Debug.logInfo("Loading Resources ...");

		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				loaded = true;
			}
		});
		
		try {
			fireSoundID = soundPool.load(this.mContext.getAssets().openFd("fire.ogg"), 1);
			deathSoundID = soundPool.load(this.mContext.getAssets().openFd("death.ogg"), 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Texture shipTexture = new Texture(mContext, "ship.png");
		shipTextureRegion = new TextureRegion(shipTexture);
		shipTexture.load();

		Texture enemy1Texture = new Texture(mContext, "EnemyBlue.png");
		blueEnemyTextureRegion = new TextureRegion(enemy1Texture);
		enemy1Texture.load();

		Texture enemy2Texture = new Texture(mContext, "EnemyPurple.png");
		purpleEnemyTextureRegion = new TextureRegion(enemy2Texture);
		enemy2Texture.load();

		Texture enemy3Texture = new Texture(mContext, "EnemyYellow.png");
		yellowEnemyTextureRegion = new TextureRegion(enemy3Texture);
		enemy3Texture.load();

		defaultFont = new Font(mContext.getAssets());
		defaultFont.load("Roboto-Regular.ttf", 20, 2, 2);

		Texture projectileTexture = new Texture(mContext, "projectile.png");
		projectileRegion = new TextureRegion(projectileTexture);
		projectileTexture.load();
	}

	@Override
	public void onCreate() {
		Debug.logInfo("Creating Scene ...");

		mMainCamera = new Camera(Pixti.width, Pixti.height);
		mHud = new HUD(Pixti.width, Pixti.height);

		mBackgroundColor = Color.BLACK;

		ship = new Ship(Pixti.width * 0.5f, shipTextureRegion.getHeight(), shipTextureRegion, new OnFireCallback() {
			
			@Override
			public void OnFireEvent() {
				if(loaded){
					soundPool.play(fireSoundID, 1, 1, 1, 0, 1);
				}
			}
		});
		this.addChild(ship);
		ship.setLimits(0, Pixti.width);
		Projectile projectilePrototype = new Projectile(0, 0, projectileRegion, null);
		ship.createPool(projectilePrototype);

		scoreText = new Text(500, 650, "Score: 0000", defaultFont);
		mHud.addChild(scoreText);

		// quadTree = new QuadTree(0, new RectangleShape(0, 0, width, height));

		int enemyCount = 10;
		float padding = 100;
		float interval = (Pixti.width - 2 * padding) / enemyCount;
		float start = interval * 0.5f + padding;

		for (int i = 0; i < enemyCount; i++) {
			Sprite s = new Sprite(start + i * interval, 600, blueEnemyTextureRegion);
			this.addChild(s);
			s.name = "Enemy";
		}
		for (int i = 0; i < enemyCount; i++) {
			Sprite s = new Sprite(start + i * interval, 535, purpleEnemyTextureRegion);
			this.addChild(s);
			s.name = "Enemy";
		}
		for (int i = 0; i < enemyCount; i++) {
			Sprite s = new Sprite(start + i * interval, 460, yellowEnemyTextureRegion);
			this.addChild(s);
			s.name = "Enemy";
		}
		
		initialized = true;
	}

	@Override
	public void onResize(int width, int height) {
		Debug.logInfo("Resizing Scene ... " + width + ", " + height);
		mMainCamera.setViewport(width, height);
	}
	
	@Override
	public void Update() {
		for (int i = ship.getChildCount() - 1; i >= 0; i--) {
			Projectile bullet = (Projectile) ship.getChild(i);

			for (int j = mRoot.getChildCount() - 1; j >= 0; j--) {
				GameObject enemy = mRoot.getChild(j);
				if (enemy.name.equals("Enemy")) {
					if (((RectangleShape) bullet).collidesWith((RectangleShape) enemy)) {
						bullet.recycle();
						mRoot.removeChild(enemy);
						score += 100;
						if(loaded){
							soundPool.play(deathSoundID, 1, 1, 1, 0, 1);
						}
					}
				}
			}
		}
		
		scoreText.setText("Score : " + score);
		
		super.Update();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (!initialized)
			return true;

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
