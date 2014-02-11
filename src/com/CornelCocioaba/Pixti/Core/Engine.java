package com.CornelCocioaba.Pixti.Core;

import java.util.List;

import android.content.Context;

import com.CornelCocioaba.Pixti.Graphics.GameRenderer;
import com.CornelCocioaba.Pixti.Graphics.Texture;
import com.CornelCocioaba.Pixti.Graphics.TextureManager;
import com.CornelCocioaba.Pixti.Input.AccelerometerHandler;
import com.CornelCocioaba.Pixti.Input.KeyboardHandler;
import com.CornelCocioaba.Pixti.Input.MultiTouchHandler;
import com.CornelCocioaba.Pixti.Input.TouchEvent;
import com.CornelCocioaba.Pixti.Input.TouchHandler;

public class Engine implements IEngine {
	
	public static int GAME_WIDTH = 1200;
	public static int GAME_HEIGHT = 720;
	
	private final TextureManager mTextureManager;
	private final TouchHandler mTouchHandler;
	private final AccelerometerHandler mAccelHandler;
	private final KeyboardHandler mKeyboardHandler;
	// the current scene
	private Scene mScene;

	private final GameSurfaceView mGameView;
	private final GameRenderer mRenderer;
	
	private final Context mContext;
	
	private boolean firstTime = true;

	public Engine(Context context) {
		 this.mContext = context;

		mTextureManager = new TextureManager(context);

		mRenderer = new GameRenderer(this);
		mGameView = new GameSurfaceView(context, mRenderer);
		
		mTouchHandler = new MultiTouchHandler(mGameView);
		mAccelHandler = new AccelerometerHandler(context);
		mKeyboardHandler = new KeyboardHandler(mGameView);
	}

	// --------------------
	// Setters and getters
	// --------------------
	@Override
	public GameSurfaceView getSurfaceView() {
		return mGameView;
	}
	
	public Scene getCurrentScene(){
		return mScene;
	}
	
	public Context getContext(){
		return mContext;
	}
	
	public TouchHandler getTouchHandler(){
		return mTouchHandler;
	}
	
	public List<TouchEvent> getTouchEvents(){
		return mTouchHandler.getTouchEvents();
	}
	
	public AccelerometerHandler getAccelerometerHandler(){
		return mAccelHandler;
	}
	
	public KeyboardHandler getKeyboardHandler(){
		return mKeyboardHandler;
	}

	// ------------------
	// Methods from interfaces
	// ------------------
	
	public void setStartScene(Scene scene){
		mScene = scene;
		//mGameView.setOnTouchListener(mScene);
	}
	
	@Override
	public void setCurrentScene(Scene scene) {
		mScene.onDestroy();
		
		mScene = scene;
		mScene.onCreate();
	}

	@Override
	public void onCreated() {
		if(firstTime){
			// load textures
			addTexture("space.png");
			addTexture("atlas.png");
			addTexture("UI.png");
			addTexture("img/logo_small.png");
			addTexture("exp2_0.png");
			mTextureManager.loadAll();

			mScene.onCreate();
			firstTime = false;
		}else{
			mTextureManager.reloadAll();
		}
		Time.Init();
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
	
	public TextureManager getTextureManager(){
		return mTextureManager;
	}
	
	public Texture addTexture(String filename){
		return mTextureManager.add(filename);
	}
	
	public Texture getTexture(String filename){
		return mTextureManager.getTexture(filename);
	}

}
