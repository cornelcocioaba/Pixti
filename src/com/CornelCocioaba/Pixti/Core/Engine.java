package com.CornelCocioaba.Pixti.Core;

import android.content.Context;

import com.CornelCocioaba.Pixti.Audio.MusicManager;
import com.CornelCocioaba.Pixti.Audio.SoundManager;
import com.CornelCocioaba.Pixti.Graphics.GameRenderer;
import com.CornelCocioaba.Pixti.Graphics.TextureManager;

public class Engine implements IEngine {

	private final TextureManager mTextureManager;
	private final SoundManager mSoundManager;
	private final MusicManager mMusicManager;
//	private final FontManager mFontManager;

	// the current scene
	private Scene mScene;

	private GameSurfaceView mGameView;
	private GameRenderer mRenderer;

	 private Context mContext;

	public Engine(Context context) {
		 this.mContext = context;

		mTextureManager = new TextureManager();
		mSoundManager = new SoundManager();
		mMusicManager = new MusicManager();

		mRenderer = new GameRenderer(this);
		mGameView = new GameSurfaceView(context, mRenderer);
		mGameView.setOnTouchListener(mScene);
	}

	// --------------------
	// Setters and getters
	// --------------------
	@Override
	public GameSurfaceView getSurfaceView() {
		return mGameView;
	}

	// ------------------
	// Methods from interfaces
	// ------------------
	@Override
	public void setCurrentScene(Scene scene) {
		mScene = scene;
		mGameView.setOnTouchListener(mScene);
	}

	@Override
	public void onCreated() {
		Time.Init();
		mScene.onCreate();
	}

	@Override
	public void onResized(int width, int height) {
		if (mScene != null) {
			mScene.onResize(width, height);
		}
	}

	@Override
	public void onDrawFrame() {
		Time.Update();
		mScene.Update();
		mScene.Draw();
	}

	@Override
	public void onPause() {
		mGameView.onPause();

		if (mScene != null) {
			mScene.onPause();
		}
	}

	@Override
	public void onResume() {
		mGameView.onResume();
		Time.Init();
		
		if (mScene != null) {
			mScene.onResume();
		}
	}

	@Override
	public void onDestroy() {
		if (mScene != null) {
			mScene.onDestroy();
		}
	}

}
