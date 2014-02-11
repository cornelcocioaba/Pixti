package com.CornelCocioaba.AndroidInvaders;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Quad;

import com.CornelCocioaba.Pixti.Audio.Audio;
import com.CornelCocioaba.Pixti.Audio.Sound;
import com.CornelCocioaba.Pixti.Core.Engine;
import com.CornelCocioaba.Pixti.Core.Scene;
import com.CornelCocioaba.Pixti.Core.Time;
import com.CornelCocioaba.Pixti.GameObject.AnimatedSprite;
import com.CornelCocioaba.Pixti.GameObject.AnimatedSprite.IAnimationListener;
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
import com.CornelCocioaba.Pixti.Input.KeyEvent;
import com.CornelCocioaba.Pixti.Utils.Debug;
import com.CornelCocioaba.Pixti.Utils.Direction;
import com.CornelCocioaba.Pixti.Utils.Pool.IPoolObjectFactory;
import com.CornelCocioaba.Pixti.Utils.Pool.Pool;

public class InvadersScene extends Scene implements IOnFireEvent, OnTouchListener, IAnimationListener {

	enum GameState {
		RUNNING, PAUSE, OVER
	}

	private GameState state = GameState.RUNNING;

	private Ship ship;
	private TextureRegion shipTextureRegion;
	private TextureRegion shipLifeTextureRegion;
	private TextureRegion blueEnemyTextureRegion;
	private TextureRegion greenEnemyTextureRegion;
	private TextureRegion blackEnemyTextureRegion;
	private TextureRegion pauseTextureRegion;
	private TextureRegion resumeTextureRegion;

	private Sprite pauseSprite;
	private Sprite resumeSprite;
	private Text resumeText;
	
	private Font defaultFont;
	private Font bigFont;
	private Text scoreText;

	private TextureRegion projectileRegion;
	private Pool<Projectile> projectilePool;
	private Pool<AnimatedSprite> explosionPool;

	private boolean initialized = false;

	public int score = 0;
	public int lives = 3;
	public Sprite livesSprites[] = new Sprite[3];

	private ArrayList<Projectile> projectileList = new ArrayList<Projectile>();
	private ArrayList<Invader> invaderList = new ArrayList<Invader>();

	private GameObject swarmController;

	private final TweenManager tweenManager = new TweenManager();
	private Audio mAudio;
	private Sound fireSound;
	private Sound fireSound2;
	private Sound deathSound;

	private int wave = 0;

	public InvadersScene(Engine engine) {
		super(engine);
		Tween.registerAccessor(GameObject.class, new GameObjectAccessor());
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		Tween.setWaypointsLimit(20);
	}

	@Override 
	public void onCreate() {
		mEngine.getSurfaceView().setOnTouchListener(this);
		mAudio = new Audio(mContext);
		fireSound = mAudio.createSound("sfx_laser1.ogg");
		fireSound2 = mAudio.createSound("sfx_laser2.ogg");
		deathSound = mAudio.createSound("death.ogg");

		Texture spritesheet = mEngine.getTexture("atlas.png");
		Texture ui = mEngine.getTexture("UI.png");

		shipTextureRegion = new TextureRegion(spritesheet, 12, 2, 112, 75);
		shipLifeTextureRegion = new TextureRegion(ui, 134, 134, 37, 26);
		blueEnemyTextureRegion = new TextureRegion(spritesheet, 64, 79, 60, 49);
		greenEnemyTextureRegion = new TextureRegion(spritesheet, 2, 79, 60, 48);
		blackEnemyTextureRegion = new TextureRegion(spritesheet, 2, 130, 60, 54);
		projectileRegion = new TextureRegion(spritesheet, 2, 2, 8, 24);
		pauseTextureRegion = new TextureRegion(ui, 52, 84, 48, 48);
		resumeTextureRegion = new TextureRegion(ui, 2, 134, 48, 48);

		TextureRegion buttonRegion = new TextureRegion(ui, 52, 134, 80, 80);

		Texture backgroundTexture = mEngine.getTexture("space.png");
		TextureRegion backgroundRegion = new TextureRegion(backgroundTexture);

		defaultFont = new Font(mContext.getAssets());
		defaultFont.load("kenvector_future.ttf", 20, 2, 2);
		
		bigFont = new Font(mEngine.getContext().getAssets());
		bigFont.load("kenvector_future.ttf", 72, 2, 2);

		mMainCamera = new Camera(Engine.GAME_WIDTH, Engine.GAME_HEIGHT);
		mHud = new HUD(Engine.GAME_WIDTH, Engine.GAME_HEIGHT);

		Debug.logInfo("Creating Scene ...");
		// Camera and HUD

		// Background
		Sprite backgroundSprite = new Sprite(0, 0, Engine.GAME_WIDTH, Engine.GAME_HEIGHT, backgroundRegion,
				Color.WHITE, Anchor.BOTTOM_LEFT);
		this.addChild(backgroundSprite);

		if (Settings.touchOn) {
			// button
			Sprite button1 = new Sprite(1100, 100, buttonRegion);
			Sprite button2 = new Sprite(70, 100, buttonRegion);
			mHud.addChild(button1);
			mHud.addChild(button2);
			button2.angle = 180;
		}

		// lives
		lives = 3;
		updateLives();

		// pool
		IPoolObjectFactory<Projectile> projectileFactory = new IPoolObjectFactory<Projectile>() {

			@Override
			public Projectile createObject() {
				return new Projectile(0, 0, projectileRegion);
			}
		};

		projectilePool = new Pool<Projectile>(projectileFactory, 20);

		// Text
		scoreText = new Text(10, 650, "Score: 0000", defaultFont);
		mHud.addChild(scoreText);

		// Ship
		ship = new Ship(Engine.GAME_WIDTH * 0.5f, shipTextureRegion.getHeight(), shipTextureRegion, this);

		this.addChild(ship);
		ship.setLimits(0, Engine.GAME_WIDTH);
		
		final TextureRegion[] explosionTiles = TextureRegion.CreateTextureRegionsFromTiledTexture(mEngine.getTexture("exp2_0.png"), 4, 4);
		
		IPoolObjectFactory<AnimatedSprite> explosionFactory = new IPoolObjectFactory<AnimatedSprite>() {
			
			@Override
			public AnimatedSprite createObject() {
				AnimatedSprite a = new AnimatedSprite(0, 0, 0.05f, explosionTiles);
				a.setAnimationListener(InvadersScene.this);
				return a;
			}
		};
		explosionPool = new Pool<AnimatedSprite>(explosionFactory, 20);

		// /Invaders
		swarmController = new GameObject();
		this.addChild(swarmController);

		Tween.set(swarmController, GameObjectAccessor.POSITION_X).target(-100);
		Tween.to(swarmController, GameObjectAccessor.POSITION_X, 2).target(100).repeatYoyo(-1, 0)
				.start(tweenManager);
		createInvaders();

		pauseSprite = new Sprite(1150, 650, pauseTextureRegion);
		this.addChild(pauseSprite);
		
		resumeSprite = new Sprite( 600, 360, resumeTextureRegion);
		resumeText = new Text(630, 350, "Resume", defaultFont);
		
		initialized = true;
	}

	private void updateLives() {
		for (int i = 0; i < lives; i++) {
			livesSprites[i] = new Sprite(1050 - 50 * i, 650, shipLifeTextureRegion);
			mHud.addChild(livesSprites[i]);
		}
	}
	private void removeLife() {
		lives--;
		if (lives > 0) {
			livesSprites[lives].removeSelf();
			ship.invulnerable = true;
			
			AnimatedSprite explosion = explosionPool.newObject();
			explosion.x = ship.getWorldX();
			explosion.y = ship.getWorldY();
			this.addChild(explosion);
			
			explosion.replay();
			ship.setColor(Color.TRANSPARENT);
			
			ship.canMove = false;
			Tween.to(ship, SpriteAccessor.OPACITY, 0.5f).target(1f)
					.repeatYoyo(10, 0).setCallback(new TweenCallback() {
						@Override
						public void onEvent(int type, BaseTween<?> source) {
							if(type == TweenCallback.BEGIN){
								ship.canMove = true;
							}
							if (type == TweenCallback.COMPLETE) {
								ship.invulnerable = false;
							}
						}
					})
					.setCallbackTriggers(TweenCallback.BEGIN | TweenCallback.COMPLETE)
					.delay(1f).start(tweenManager);
		} else {
			livesSprites[0].removeSelf();
			ship.alive = false;
			ship.removeSelf();
			changeState(GameState.OVER);
		}
	}

	private void createInvaders() {
		wave++;
		Time.timeScale = 1 + (wave - 1) / 10;
		int enemyCount = 10;
		float padding = 100;
		float interval = (Engine.GAME_WIDTH - 2 * padding) / enemyCount;
		float start = interval * 0.5f + padding;

		for (int i = 0; i < enemyCount; i++) {
			if (i == 0 || i == enemyCount - 1)
				continue;

			Invader s = new Invader(start + i * interval, 600, blueEnemyTextureRegion);
			s.fireEvent = this;
			invaderList.add(s);
			swarmController.addChild(s);
		}

		for (int i = 0; i < enemyCount; i++) {
			Invader s = new Invader(start + i * interval, 535, greenEnemyTextureRegion);
			s.fireEvent = this;
			invaderList.add(s);
			swarmController.addChild(s);
		}

		for (int i = 0; i < enemyCount; i++) {
			Invader s = new Invader(start + i * interval, 460, blackEnemyTextureRegion);
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

			if (Settings.soundOn)
				fireSound.play();
		} else {
			Projectile pr = projectilePool.newObject();
			this.addChild(pr);
			pr.start(sender.getWorldX(), sender.getWorldY(), Direction.DOWN);
			projectileList.add(pr);

			if (Settings.soundOn)
				fireSound2.play();
		}

	}

	@Override
	public void onResize(int width, int height) {
		Debug.logInfo("Resizing Scene ... " + width + ", " + height);
		mMainCamera.setViewport(width, height);
	}

	Random random = new Random();
	float lastFly;
	float lastFire;

	@Override
	public void Update() {
		switch (state) {
			case RUNNING:
				updateRunning();
				break;
			case PAUSE:
				updatePause();
				break;
			case OVER:
				updateOver();
				break;
			default:
				break;
		}
		
		List<KeyEvent> keyEvents = mEngine.getKeyboardHandler().getKeyEvents();
		for(int i=keyEvents.size() -1; i>=0; i--){
			KeyEvent event = keyEvents.get(i);
			
			if(event.keyCode == KeyEvent.KEYCODE_BACK && event.type == KeyEvent.KEY_UP){
				if(state == GameState.PAUSE)
					changeState(GameState.RUNNING);
				else
					changeState(GameState.PAUSE);
			}
		}
	}
	
	private void updateRunning(){
		super.Update();
		tweenManager.update(Time.deltaTime);
		
		if (!Settings.touchOn) {
			if (mEngine.getAccelerometerHandler().getAccelY() > 0) {
				ship.moveRight();
			} else {
				ship.moveLeft();
			}
		}

		if (invaderList.isEmpty())
			createInvaders();


		
		//random flank invader every 10 sec
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
							.target(invader.getWorldX() - 100, invader.getWorldY() - 800).ease(Quad.INOUT)
							.delay(0.5f).start(tweenManager);
				}
			} else {
				Invader invader = getRightInvader();

				if (invader != null) {
					invader.fire();
					Tween.to(invader, GameObjectAccessor.POSITION_XY, 6)
							.waypoint(invader.getWorldX() + 100, invader.getWorldY() - 200)
							.waypoint(invader.getWorldX() - 300, invader.getWorldY() - 400)
							.target(invader.getWorldX() + 100, invader.getWorldY() - 800).ease(Quad.INOUT)
							.delay(0.5f).start(tweenManager);
				}
			}
		}

		checkCollisions();
		
		// random fire every 2 sec
		if (Time.time - lastFire > 2) {
			lastFire = Time.time;

			int i = random.nextInt(invaderList.size());

			invaderList.get(i).fire();
		}



		scoreText.setText("Score : " + score);
	}
	
	private void checkCollisions(){
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
					if (Settings.soundOn)
						deathSound.play();
				}

				if (!ship.invulnerable && ship.alive && projectile.direction == Direction.DOWN && projectile.collidesWith(ship)) {
					projShouldDie = true;

					removeLife();
				}

				if (!ship.invulnerable && ship.alive && invader.collidesWith(ship)) {
					invaderShouldDie = true;

					removeLife();
				}

				if (invader.getWorldY() < 0) {
					invaderShouldDie = true;
				}

				if (invaderShouldDie) {
					AnimatedSprite explosion = explosionPool.newObject();
					explosion.x = invader.x;
					explosion.y = invader.y;
					swarmController.addChild(explosion);
					explosion.replay();
					
					invader.removeSelf();
					invaderList.remove(j);
				}

			}
			float y = projectile.getWorldY();
			if (y < 0 || y > Engine.GAME_HEIGHT) {
				projShouldDie = true;
			}

			if (projShouldDie) {
				projectile.removeSelf();
				projectileList.remove(i);

				projectilePool.recycle(projectile);
			}
		}
	}
	private void updatePause(){
	}
	
	
	private void updateOver(){
		if(touchedOver){
			mEngine.setCurrentScene(new MainMenuScene(mEngine));
			touchedOver = false;
		}
	}
	
	private void changeState(GameState newState){
		switch (state) {
		case RUNNING:
			break;
		case PAUSE:
			resumeSprite.removeSelf();
			resumeText.removeSelf();
			break;
		case OVER:
			break;
		default:
			break;
		}
		
		state = newState;
		
		switch (state) {
		case RUNNING:
			break;
		case PAUSE:
			mHud.addChild(resumeSprite);
			mHud.addChild(resumeText);
			break;
		case OVER:
			Time.timeScale = 0;
			Text gameover = new Text(400, 350, "Game Over", bigFont);
			mHud.addChild(gameover);
			break;
		default:
			break;
	}
	}
	boolean touchedOver;
	
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
			if(state == GameState.RUNNING && pauseSprite.containsPoint(event.getX(pointerIndex), 720 - event.getY(pointerIndex))){
				return true;
			}
			if(state != GameState.RUNNING || !Settings.touchOn) break;
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
			if(state == GameState.OVER){
				touchedOver = true;
				return true;
			}
			if(state == GameState.RUNNING && pauseSprite.containsPoint(event.getX(pointerIndex), 720 - event.getY(pointerIndex))){
				changeState(GameState.PAUSE);
				ship.stop();
				return true;
			}
			
			if(state == GameState.PAUSE && resumeSprite.containsPoint(event.getX(pointerIndex), 720 - event.getY(pointerIndex))){
				changeState(GameState.RUNNING);
				return true;
			}
			
			if(state != GameState.RUNNING || !Settings.touchOn) break;
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

	@Override
	public void onAnimationFinished(AnimatedSprite animatedSprite) {
		animatedSprite.removeSelf();
		explosionPool.recycle(animatedSprite);
	}
}
