package com.CornelCocioaba.AndroidInvaders;

import java.util.List;

import android.view.KeyEvent;

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

public class SettingsScene extends Scene {

	private Sprite soundButton;
	private Sprite musicButton;

	private TextureRegion musicOn;
	private TextureRegion musicOff;

	private TextureRegion soundOn;
	private TextureRegion soundOff;

	private Sprite touch;
	private Sprite accelerometer;

	private TextureRegion buttonUp;
	private TextureRegion buttondown;

	private Font font;
	private Font smallFont;

	public SettingsScene(Engine engine) {
		super(engine);
	}

	@Override
	public void onCreate() {
		mMainCamera = new Camera(1200, 720);

		font = new Font(mEngine.getContext().getAssets());
		font.load("kenvector_future.ttf", 72, 2, 2);
		
		smallFont = new Font(mEngine.getContext().getAssets());
		smallFont.load("kenvector_future.ttf", 20, 2, 2);

		TextureRegion backgroundRegion = new TextureRegion(mEngine.getTexture("space.png"));
		Sprite backgroundSprite = new Sprite(0, 0, 1200, 720, backgroundRegion, Color.WHITE, Anchor.BOTTOM_LEFT);
		this.addChild(backgroundSprite);

		soundOn = new TextureRegion(mEngine.getTexture("UI.png"), 2, 84, 48, 48);
		soundOff = new TextureRegion(mEngine.getTexture("UI.png"), 102, 84, 48, 48);
		musicOn = new TextureRegion(mEngine.getTexture("UI.png"), 152, 84, 48, 48);
		musicOff = new TextureRegion(mEngine.getTexture("UI.png"), 202, 84, 48, 48);
		
		
		Text settingsText = new Text(400, 550, "Settings", font);
		this.addChild(settingsText);

		soundButton = new Sprite(550, 250, soundOn);
		musicButton = new Sprite(650, 250, musicOn);
		this.addChild(soundButton);
		this.addChild(musicButton);
		

		buttonUp = new TextureRegion(mEngine.getTexture("UI.png"), 2, 2, 222, 39);
		buttondown = new TextureRegion(mEngine.getTexture("UI.png"), 2, 43, 222, 39);

		touch = new Sprite(480, 400, buttonUp);
		touch.scaleY = 2;
		
		accelerometer = new Sprite(730, 400, buttonUp);
		accelerometer.scaleX = 1.2f;
		accelerometer.scaleY = 2;
		
		if (Settings.touchOn) {
			accelerometer.setTextureRegion(buttondown);
		} else {
			touch.setTextureRegion(buttondown);
		}
		
		Text touchText = new Text(440, 390, "Touch", Color.RED, smallFont);
		Text accelText = new Text(620, 390, "Accelerometer", Color.RED, smallFont);

		this.addChild(touch);
		this.addChild(accelerometer);
		this.addChild(touchText);
		this.addChild(accelText);
	}

	@Override
	public void Update() {

		List<TouchEvent> events = mEngine.getTouchEvents();

		int count = events.size();
		for (int i = 0; i < count; i++) {
			TouchEvent event = events.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (soundButton.containsPoint(event.x, event.y)) {
					toggleSound();
				}

				if (musicButton.containsPoint(event.x, event.y)) {
					toggleMusic();
				}

				if (touch.containsPoint(event.x, event.y)) {
					setTouch();
				}

				if (accelerometer.containsPoint(event.x, event.y)) {
					setAccel();
				}
			}
		}

		if (mEngine.getKeyboardHandler().isKeyPressed(KeyEvent.KEYCODE_BACK)) {
			mEngine.setCurrentScene(new MainMenuScene(mEngine));
		}

		super.Update();
	}

	private void toggleSound() {
		if (Settings.soundOn) {
			Settings.soundOn = false;
			soundButton.setTextureRegion(soundOff);
		} else {
			Settings.soundOn = true;
			soundButton.setTextureRegion(soundOn);
		}
	}

	private void toggleMusic() {
		if (Settings.musicOn) {
			Settings.musicOn = false;
			musicButton.setTextureRegion(musicOff);
		} else {
			Settings.musicOn = true;
			musicButton.setTextureRegion(musicOn);
		}
	}

	private void setTouch() {
		if (!Settings.touchOn) {
			Settings.touchOn = true;
			touch.setTextureRegion(buttonUp);
			accelerometer.setTextureRegion(buttondown);
		}
	}

	private void setAccel() {
		if (Settings.touchOn) {
			Settings.touchOn = false;
			touch.setTextureRegion(buttondown);
			accelerometer.setTextureRegion(buttonUp);
		}
	}
}
