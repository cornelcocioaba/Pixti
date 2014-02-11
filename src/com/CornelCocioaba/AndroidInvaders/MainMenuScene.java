package com.CornelCocioaba.AndroidInvaders;

import java.util.List;

import com.CornelCocioaba.Pixti.Core.Engine;
import com.CornelCocioaba.Pixti.Core.Scene;
import com.CornelCocioaba.Pixti.GameObject.RectangleShape.Anchor;
import com.CornelCocioaba.Pixti.GameObject.Sprite;
import com.CornelCocioaba.Pixti.GameObject.Text;
import com.CornelCocioaba.Pixti.Graphics.Camera;
import com.CornelCocioaba.Pixti.Graphics.Color;
import com.CornelCocioaba.Pixti.Graphics.TextureRegion;
import com.CornelCocioaba.Pixti.Graphics.Font.Font;
import com.CornelCocioaba.Pixti.Input.TouchEvent;

public class MainMenuScene extends Scene {

	Sprite startButton;
	Sprite settingsButton;
	Font font;
	Font fontB;
	
	public MainMenuScene(Engine engine) {
		super(engine);
	}

	@Override
	public void onCreate() {
		mEngine.getSurfaceView().setOnTouchListener(mEngine.getTouchHandler());
		// set camera
		mMainCamera = new Camera(1200, 720);

		font = new Font(mEngine.getContext().getAssets());
		font.load("kenvector_future.ttf", 20, 2, 2);
		
		fontB = new Font(mEngine.getContext().getAssets());
		fontB.load("kenvector_future.ttf", 72, 2, 2);

		// add background sprite
		TextureRegion backgroundRegion = new TextureRegion(mEngine.getTexture("space.png"));
		Sprite backgroundSprite = new Sprite(0, 0, 1200, 720, backgroundRegion, Color.WHITE, Anchor.BOTTOM_LEFT);
		this.addChild(backgroundSprite);

		
		Text name = new Text(200, 500, "Android Invaders", fontB);
		this.addChild(name);
		// add Logo
//		Sprite logo = new Sprite(600, 500, mEngine.getTexture("img/logo_small.png"));
//		this.addChild(logo);

		// both start and settings button will share the same texture region
		TextureRegion buttonTextureRegion = new TextureRegion(mEngine.getTexture("UI.png"), 2, 2, 222, 39);

		// add start button
		startButton = new Sprite(600, 350, buttonTextureRegion);
		startButton.scaleY = 2;
		this.addChild(startButton);

		// add settings button
		settingsButton = new Sprite(600, 250, buttonTextureRegion);
		settingsButton.scaleY = 2;
		this.addChild(settingsButton);
		
		
		Text startText = new Text(565, 340, "Play", Color.RED, font);
		this.addChild(startText);
		
		Text settingsText = new Text(545, 235, "Settings", Color.RED, font);
		this.addChild(settingsText);

	}

	@Override
	public void Update() {
		super.Update();

		List<TouchEvent> events = mEngine.getTouchEvents();

		int count = events.size();
		for (int i = 0; i < count; i++) {
			TouchEvent event = events.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (startButton.containsPoint(event.x, event.y)) {
					mEngine.setCurrentScene(new InvadersScene(mEngine));
				}

				if (settingsButton.containsPoint(event.x, event.y)) {
					mEngine.setCurrentScene(new SettingsScene(mEngine));
				}
			}
		}
	}
	
	@Override
	public void onDestroy() {
		font.unload();
	}
}
