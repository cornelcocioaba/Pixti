package com.CornelCocioaba.Pixti.Core;

import android.content.Context;
import android.opengl.GLES20;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import aurelienribon.tweenengine.Tween;

import com.CornelCocioaba.AndroidInvaders.GameObjectAccessor;
import com.CornelCocioaba.Pixti.GameObject.GameObject;
import com.CornelCocioaba.Pixti.GameObject.HUD;
import com.CornelCocioaba.Pixti.Graphics.Camera;
import com.CornelCocioaba.Pixti.Graphics.Color;
import com.CornelCocioaba.Pixti.Utils.Debug;

/*
 * The scene contains all GameObjects, cameras, sounds, touches etc
 * It has infinite size
 */

public abstract class Scene {
	protected Camera mMainCamera;
	protected HUD mHud;
	protected GameObject mRoot;

	protected Context mContext;
	protected Engine mEngine;

	protected Color mBackgroundColor = new Color(1, 1, 1, 1);

	public Scene(Engine engine) {
		mEngine = engine;
		mContext = engine.getContext();
		mRoot = new GameObject();
		Tween.registerAccessor(GameObject.class, new GameObjectAccessor());
	}

	public void createCamera(int width, int height) {
		mMainCamera = new Camera(width, height);
	}

	/*
	 * public Context getContext() { return mContext; }
	 */
	public void setContext(Context mContext) {
		this.mContext = mContext;
	}

	public Color getBackgroundColor() {
		return mBackgroundColor;
	}

	public void setBackgroundColor(Color color) {
		mBackgroundColor = color;
	}

	public void setHUD(HUD hud) {
		mHud = hud;
	}

	public GameObject getRoot() {
		return mRoot;
	}

	public void addChild(GameObject go) {
		mRoot.addChild(go);
	}

	public void removeChild(GameObject go) {
		mRoot.removeChild(go);
	}

	public void Update() {
		mRoot.Update();

		if (mHud != null) {
			mHud.Update();
		}
	}

	public void Draw() {

		if (mMainCamera == null) {
			Debug.log("You forgot to set up the camera");
			return;
		}
		GLES20.glClearColor(mBackgroundColor.getRed(), mBackgroundColor.getGreen(), mBackgroundColor.getBlue(),
				mBackgroundColor.getAlpha());
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		mRoot.Draw(mMainCamera);

		if (mHud != null) {
			mHud.Draw(mMainCamera);
		}
	}

	public boolean onTouchEvent(MotionEvent event) {

		return true;
	}

	public void onCreate() {

	}

	public void onResize(int width, int height) {

	}

	public void onPause() {

	}

	public void onResume() {

	}

	public void onDestroy() {

	};
}
