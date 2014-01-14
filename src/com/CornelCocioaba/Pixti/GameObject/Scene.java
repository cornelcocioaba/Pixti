package com.CornelCocioaba.Pixti.GameObject;

import java.util.ArrayList;

import android.content.Context;
import android.opengl.GLES20;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import aurelienribon.tweenengine.Tween;

import com.CornelCocioaba.Pixti.Engine.Camera;
import com.CornelCocioaba.Pixti.OpenGL.Color;
import com.CornelCocioaba.Pixti.Utils.Debug;

/*
 * The scene is the root of all GameObjects, cameras, sounds, touches etc
 * It has infinite size
 */

public abstract class Scene implements OnTouchListener {
	protected Camera mMainCamera;
	protected HUD mHud;
	
	protected Context mContext;
	
	protected ArrayList<GameObject> children = new ArrayList<GameObject>();
	
	private Color mBackgroundColor;
	
	
	public Scene(Context context) {
		mContext = context;
		Tween.registerAccessor(GameObject.class, new GameObjectAccessor());
		mBackgroundColor = new Color(0.0f, 0.6f, 0.8f, 1.0f);
	}
	
	public void createCamera(float width, float height){
		mMainCamera = new Camera(width, height);
	}
	
	public Context getContext() {
		return mContext;
	}

	public void setContext(Context mContext) {
		this.mContext = mContext;
	}

	public Color getBackgroundColor(){
		return mBackgroundColor;
	}
	
	public void setBackgroundColor(Color color){
		mBackgroundColor = color;
	}
	
	public void setHUD(HUD hud){
		mHud = hud;
	}
	
	public void addChild(GameObject obj) {
		children.add(obj);
	}

	public void removeChild(GameObject obj) {
		children.remove(obj);
	}

	public void Update() {
		final int size = children.size();
		for (int i = 0; i < size; i++) {
			children.get(i).Update();
		}
		
		if(mHud != null){
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
		
		final int size = children.size();
		for (int i = 0; i < size; i++) {
			children.get(i).Draw(mMainCamera);
		}
		
		if(mHud != null){
			mHud.Draw(mMainCamera);
		}
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		
		return true;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}

	public abstract void onLoadResources();
	
	public abstract void onCreate(int width, int height);
}
