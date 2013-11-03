package com.CornelCocioaba.Pixti.Engine;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.CornelCocioaba.Pixti.GameObject.GameObject;
import com.CornelCocioaba.Pixti.GameObject.GameObjectAccessor;
import com.CornelCocioaba.Pixti.GameObject.Scene;
import com.CornelCocioaba.Pixti.GameObject.Sprite;

public class CamRendererTest implements Renderer {

	Camera cam;
	Scene scene;
	private final TweenManager tweenManager = new TweenManager();
	Context context;
	

	public CamRendererTest(Context context) {
		this.context = context;
	}

	@Override
	public void onDrawFrame(GL10 unused) {
		scene.Update();
		scene.Draw(cam);
		tweenManager.update(0.016f);
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		cam.setViewport(width, height);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		cam = new Camera(1280f, 720f);
		scene = new Scene(0, 0);
		Tween.registerAccessor(GameObject.class, new GameObjectAccessor());
		
//		Rectangle r1 = new Rectangle(150, 460, 100, 100, Anchor.BOTTOM_LEFT);
//		scene.addChild(r1);
//		Rectangle r2 = new Rectangle(300, 260, 100, 100, Anchor.BOTTOM_CENTER);
//		scene.addChild(r2);
//		Rectangle r3 = new Rectangle(450, 460, 100, 100, Anchor.BOTTOM_RIGHT);
//		scene.addChild(r3);
//		Rectangle r4 = new Rectangle(600, 260, 100, 100, Anchor.MIDDLE_LEFT);
//		scene.addChild(r4);
//		Rectangle r5 = new Rectangle(750, 460, 100, 100, Anchor.MIDDLE_CENTER);
//		scene.addChild(r5);
//		Rectangle r6 = new Rectangle(900, 260, 100, 100, Anchor.MIDDLE_RIGHT);
//		scene.addChild(r6);
//		Rectangle r7 = new Rectangle(1050, 460, 100, 100, Anchor.TOP_LEFT);
//		scene.addChild(r7);
//		Rectangle r8 = new Rectangle(640, 555, 100, 100, Anchor.TOP_CENTER);
//		scene.addChild(r8);
//		Rectangle r9 = new Rectangle(800, 555, 100, 100, Anchor.TOP_RIGHT);
//		scene.addChild(r9);
		
		Sprite s = new Sprite(context, 150, 450,  200, 200, "ic_launcher.png");
		scene.addChild(s);
//		Sprite s2 = new Sprite(context, 300, 250,  200, 200);
//		scene.addChild(s2);
//		Sprite s3 = new Sprite(context, 450, 350,  200, 200);
//		scene.addChild(s3);
//		Sprite s4 = new Sprite(context, 600, 250,  200, 200);
//		scene.addChild(s4);
//		Sprite s5 = new Sprite(context, 750, 350,  200, 200);
//		scene.addChild(s5);
//		Sprite s6 = new Sprite(context, 900, 450,  200, 200);
//		scene.addChild(s6);
//		Sprite s7 = new Sprite(context, 1050, 350,  200, 200);
//		scene.addChild(s7);
		
		Tween.to(s, GameObjectAccessor.POSITION_XY, 1f).target(1000, 450).repeat(-1, 0).start(tweenManager);
		
	}

}
