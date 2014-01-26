package com.CornelCocioaba.AndroidInvaders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.view.MotionEvent;
import android.view.View;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Quad;

import com.CornelCocioaba.Pixti.GameObject.GameObject;
import com.CornelCocioaba.Pixti.GameObject.HUD;
import com.CornelCocioaba.Pixti.GameObject.RectangleShape.Anchor;
import com.CornelCocioaba.Pixti.GameObject.Sprite;
import com.CornelCocioaba.Pixti.GameObject.Text;
import com.CornelCocioaba.Pixti.Graphics.Camera;
import com.CornelCocioaba.Pixti.Graphics.Color;
import com.CornelCocioaba.Pixti.Graphics.Texture;
import com.CornelCocioaba.Pixti.Graphics.TextureRegion;
import com.CornelCocioaba.Pixti.Graphics.Font.Font;
import com.CornelCocioaba.Pixti.System.Scene;
import com.CornelCocioaba.Pixti.Utils.Debug;
import com.CornelCocioaba.Pixti.Utils.Direction;
import com.CornelCocioaba.Pixti.Utils.Time;
import com.CornelCocioaba.Pixti.Utils.Pool.IPoolObjectFactory;
import com.CornelCocioaba.Pixti.Utils.Pool.Pool;

public class InvadersScene extends Scene implements IOnFireEvent {

	private static int GAME_WIDTH = 1200;
	private static int GAME_HEIGHT = 720;

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
	private Pool<Projectile> projectilePool;

	private boolean initialized = false;

	public int score = 0;
	public int lives = 3;
	public Sprite livesSprites[] = new Sprite[3];

	private ArrayList<Projectile> projectileList = new ArrayList<Projectile>();
	private ArrayList<Invader> invaderList = new ArrayList<Invader>();

	private GameObject swarmController;

	private final TweenManager tweenManager = new TweenManager();

	public InvadersScene(Context context) {
		super(context);
		Tween.registerAccessor(GameObject.class, new GameObjectAccessor());
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		Tween.setWaypointsLimit(20);
	}

	@SuppressWarnings("static-access")
	@Override
	public void onCreate() {
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

		Texture backgroundTexture = new Texture(mContext, "space.png");
		TextureRegion backgroundRegion = new TextureRegion(backgroundTexture);
		backgroundTexture.load();

		Texture buttonTexture = new Texture(mContext, "button.png");
		TextureRegion buttonRegion = new TextureRegion(buttonTexture);
		buttonTexture.load();

		Debug.logInfo("Creating Scene ...");

		// Camera and HUD
		mMainCamera = new Camera(GAME_WIDTH, GAME_HEIGHT);
		mHud = new HUD(GAME_WIDTH, GAME_HEIGHT);

		// Background
		Sprite backgroundSprite = new Sprite(0, 0, GAME_WIDTH, GAME_HEIGHT, backgroundRegion, Color.WHITE,
				Anchor.BOTTOM_LEFT);
		this.addChild(backgroundSprite);
//		mBackgroundColor = Color.CYAN;

		// button
		Sprite button1 = new Sprite(1100, 70, buttonRegion);
		Sprite button2 = new Sprite(70, 70, buttonRegion);
		mHud.addChild(button1);
		mHud.addChild(button2);
		button2.angle = 180;
		
		//lives
		lives = 3;
		updateLives();
		
		// pool
		IPoolObjectFactory<Projectile> projectileFactory = new IPoolObjectFactory<Projectile>() {

			@Override
			public Projectile createObject() {
				return new Projectile(0, 0, projectileRegion);
			}
		};

		projectilePool = new Pool<Projectile>(projectileFactory, 100);

		// Text
		scoreText = new Text(500, 650, "Score: 0000", defaultFont);
		mHud.addChild(scoreText);

		// Ship
		ship = new Ship(GAME_WIDTH * 0.5f, shipTextureRegion.getHeight(), shipTextureRegion, this);

		this.addChild(ship);
		ship.setLimits(0, GAME_WIDTH);

		// /Invaders
		swarmController = new GameObject();
		this.addChild(swarmController);

		Tween.set(swarmController, GameObjectAccessor.POSITION_X).target(-100)
				.to(swarmController, GameObjectAccessor.POSITION_X, 2).target(100).repeatYoyo(-1, 0)
				.start(tweenManager);
		createInvaders();

		initialized = true;
	}

	private void updateLives(){
		for(int i=0; i<lives; i++){
			livesSprites[i] = new Sprite(1100 - 50*i, 650, shipTextureRegion);
			livesSprites[i].setScale(0.5f);
			mHud.addChild(livesSprites[i]);
		}
	}
	
	@SuppressWarnings("static-access")
	private void removeLife() {
		if (lives > 0) {
			lives--;
			livesSprites[lives].removeSelf();
			ship.invulnerable = true;
			
			Tween.set(ship, SpriteAccessor.OPACITY).target(1f)
			.to(ship, SpriteAccessor.OPACITY, 1).target(0.5f)
			.repeatYoyo(5, 0)
			.setCallback(new TweenCallback() {
				@Override
				public void onEvent(int type, BaseTween<?> source) {
					ship.invulnerable = false;
				}
			})
			.start(tweenManager);
		}
	}
	
	private void createInvaders() {
		int enemyCount = 10;
		float padding = 100;
		float interval = (GAME_WIDTH - 2 * padding) / enemyCount;
		float start = interval * 0.5f + padding;

		for (int i = 0; i < enemyCount; i++) {
			if(i ==0 || i == enemyCount - 1) continue;
			
			Invader s = new Invader(start + i * interval, 600, blueEnemyTextureRegion);
			s.fireEvent = this;
			invaderList.add(s);
			swarmController.addChild(s);
		}

		for (int i = 0; i < enemyCount; i++) {
			Invader s = new Invader(start + i * interval, 535, purpleEnemyTextureRegion);
			s.fireEvent = this;
			invaderList.add(s);
			swarmController.addChild(s);
		}

		for (int i = 0; i < enemyCount; i++) {
			Invader s = new Invader(start + i * interval, 460, yellowEnemyTextureRegion);
			s.fireEvent = this;
			invaderList.add(s);
			swarmController.addChild(s);
		}
	}

	private Invader getLeftInvader() {

		Invader inv = null;

		for (int i = 0; i < invaderList.size(); i++) {
			if (inv == null) {
				inv = invaderList.get(i);
			} else {
				if (invaderList.get(i).x < inv.x) {
					inv = invaderList.get(i);
				}
			}
		}

		return inv;
	}

	private Invader getRightInvader() {

		Invader inv = null;

		for (int i = 0; i < invaderList.size(); i++) {
			if (inv == null) {
				inv = invaderList.get(i);
			} else {
				if (invaderList.get(i).x > inv.x) {
					inv = invaderList.get(i);
				}
			}
		}

		return inv;
	}

	@Override
	public void OnFireEvent(GameObject sender) {

		if (sender instanceof Ship) {
			Projectile pr = projectilePool.newObject();
			this.addChild(pr);
			pr.start(ship.x, ship.y + Ship.CENTER_GUN_OFFSET, Direction.UP);
			projectileList.add(pr);
		} else {
			Projectile pr = projectilePool.newObject();
			this.addChild(pr);
			pr.start(sender.getWorldX(), sender.getWorldY(), Direction.DOWN);
			projectileList.add(pr);
		}

		if (loaded) {
			soundPool.play(fireSoundID, 1, 1, 1, 0, 1);
		}
	}

	@Override
	public void onResize(int width, int height) {
		Debug.logInfo("Resizing Scene ... " + width + ", " + height);
		mMainCamera.setViewport(width, height);
	}

	Random random = new Random();
	float lastFly;

	@Override
	public void Update() {
		if (invaderList.isEmpty())
			createInvaders();

		tweenManager.update(Time.deltaTime);

		if (Time.time - lastFly > 10.0f) {
			lastFly = Time.time;

			boolean left = random.nextBoolean();
			Debug.log(left);
			
			if (left) {
				Invader invader = getLeftInvader();

				if (invader != null) {
					invader.fire();
					Tween.to(invader, GameObjectAccessor.POSITION_XY, 6)
							.waypoint(invader.getWorldX() - 100, invader.getWorldY() - 200)
							.waypoint(invader.getWorldX() + 300, invader.getWorldY() - 400)
							.target(invader.getWorldX() - 100, invader.getWorldY() - 800).ease(Quad.INOUT).delay(0.5f)
							.start(tweenManager);
				}
			} else {
				Invader invader = getRightInvader();

				if (invader != null) {
					invader.fire();
					Tween.to(invader, GameObjectAccessor.POSITION_XY, 6)
							.waypoint(invader.getWorldX() + 100, invader.getWorldY() - 200)
							.waypoint(invader.getWorldX() - 300, invader.getWorldY() - 400)
							.target(invader.getWorldX() + 100, invader.getWorldY() - 800).ease(Quad.INOUT).delay(0.5f)
							.start(tweenManager);
				}
			}
		}

		for (int i = projectileList.size() - 1; i >= 0; i--) {
			final Projectile projectile = projectileList.get(i);
			boolean projShouldDie = false;

			for (int j = invaderList.size() - 1; j >= 0; j--) {
				final Invader invader = invaderList.get(j);
				boolean invaderShouldDie = false;
				if (projectile.direction == Direction.UP && projectile.collidesWith(invader)) {

					projShouldDie = true;
					invaderShouldDie = true;

					score += 100;
					if (loaded) {
						soundPool.play(deathSoundID, 1, 1, 1, 0, 1);
					}
				}

				if (!ship.invulnerable && projectile.direction == Direction.DOWN && projectile.collidesWith(ship)) {
					projShouldDie = true;

					// die
					removeLife();
				}

				if (!ship.invulnerable && invader.collidesWith(ship)) {
					invaderShouldDie = true;

					// die
					removeLife();
				}

				if (invader.getWorldY() < 0) {
					invaderShouldDie = true;
				}

				if (invaderShouldDie) {
					invader.removeSelf();
					invaderList.remove(j);
				}

			}
			float y = projectile.getWorldY();
			if (y < 0 || y > GAME_HEIGHT) {
				projShouldDie = true;
			}

			if (projShouldDie) {
				projectile.removeSelf();
				projectileList.remove(i);

				projectilePool.free(projectile);
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

	@Override
	public void onPause() {
		Debug.log("Scene paused");

	}

	@Override
	public void onResume() {
		Debug.log("Scene resumed");

	}

	@Override
	public void onDestroy() {
		Debug.log("Scene destroyed");

	}
}
