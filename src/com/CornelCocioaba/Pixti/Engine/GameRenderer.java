package com.CornelCocioaba.Pixti.Engine;

import android.content.Context;
import aurelienribon.tweenengine.TweenManager;

import com.CornelCocioaba.Pixti.AndroidInvaders.InvadersScene;
import com.CornelCocioaba.Pixti.GameObject.Scene;

public class GameRenderer extends BaseGameRenderer {

	public Scene scene;
	private final TweenManager tweenManager = new TweenManager();
	
	public GameRenderer(Context context) {
		scene = new InvadersScene(context);
	}

	@Override
	public void onSurfaceCreated() {
	}

	@Override
	public void onCreate() {
		scene.onLoadResources();
		scene.onCreate();
	}

	@Override
	public void onChangedSurface(int width, int height) {
		scene.onResize(width, height);
	}
	
	@Override
	public void onDrawFrame() {
		scene.Update();
		scene.Draw();
		tweenManager.update(Time.deltaTime);
	}

}
