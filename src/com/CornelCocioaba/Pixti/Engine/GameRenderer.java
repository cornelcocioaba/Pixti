package com.CornelCocioaba.Pixti.Engine;

import android.content.Context;
import android.view.MotionEvent;
import aurelienribon.tweenengine.TweenManager;

import com.CornelCocioaba.Pixti.GameObject.AnimatedSprite;
import com.CornelCocioaba.Pixti.GameObject.Scene;
import com.CornelCocioaba.Pixti.OpenGL.Texture;
import com.CornelCocioaba.Pixti.OpenGL.TextureRegion;

public class GameRenderer extends BaseGameRenderer {

	private Camera cam;
	private Scene scene;
	private final TweenManager tweenManager = new TweenManager();
	private Context context;
	private AnimatedSprite volt;

	public GameRenderer(Context context) {
		this.context = context;
	}

	@Override
	public void onCreate(int width, int height, boolean contextLost) {
		cam = new Camera(width, height);

		scene = new Scene();
		// scene.mBackgroundColor = Color.YELLOW;

		/*
		 * Texture launcher = new Texture(context, "ic_launcher.png"); TextureRegion launcherRegion = new
		 * TextureRegion(launcher); launcher.load();
		 * 
		 * Texture logo = new Texture(context, "logo.png"); TextureRegion logoTextureRegion = new TextureRegion(logo);
		 * logo.load();
		 * 
		 * Sprite s = new Sprite(launcherRegion, 150, 550, 256, 256); s.setScale(0.5f); scene.addChild(s);
		 * 
		 * Sprite s2 = new Sprite(logoTextureRegion, 150, 350, 128, 128); scene.addChild(s2);
		 * 
		 * Rectangle r = new Rectangle(150, 150, 128, 128); r.setColors(Color.RED, Color.BLUE, Color.GREEN,
		 * Color.YELLOW); scene.addChild(r);
		 * 
		 * Tween.to(s, GameObjectAccessor.POSITION_XY, 1f).target(1000, 550).ease(Quad.INOUT).repeatYoyo(-1,
		 * 0.2f).start(tweenManager); Tween.to(s2, GameObjectAccessor.POSITION_XY, 1f).target(1000,
		 * 350).ease(Elastic.OUT).repeat(-1, 0.2f).start(tweenManager); Tween.to(r, GameObjectAccessor.POSITION_XY,
		 * 1f).target(1000, 150).ease(Bounce.OUT).repeat (-1, 0.2f).start(tweenManager);
		 */

		Texture spritesheetVolt = new Texture(context, "spritesheet.png");
		TextureRegion volt1 = new TextureRegion(spritesheetVolt, 0 * 240, 0, 240, 292);
		TextureRegion volt2 = new TextureRegion(spritesheetVolt, 1 * 240, 0, 240, 292);
		TextureRegion volt3 = new TextureRegion(spritesheetVolt, 2 * 240, 0, 240, 292);
		TextureRegion volt4 = new TextureRegion(spritesheetVolt, 3 * 240, 0, 240, 292);
		TextureRegion volt5 = new TextureRegion(spritesheetVolt, 4 * 240, 0, 240, 292);

		TextureRegion volt6 = new TextureRegion(spritesheetVolt, 0 * 240, 292, 240, 292);
		TextureRegion volt7 = new TextureRegion(spritesheetVolt, 1 * 240, 292, 240, 292);
		TextureRegion volt8 = new TextureRegion(spritesheetVolt, 2 * 240, 292, 240, 292);
		TextureRegion volt9 = new TextureRegion(spritesheetVolt, 3 * 240, 292, 240, 292);
		TextureRegion volt0 = new TextureRegion(spritesheetVolt, 4 * 240, 292, 240, 292);
		spritesheetVolt.load();

		volt = new AnimatedSprite(200, 200, 100, volt1, volt2, volt3, volt4, volt5, volt6, volt7, volt8,
				volt9, volt0);
		scene.addChild(volt);
	}

	@Override
	public void onDrawFrame(boolean firstDraw) {
		scene.Update();
		scene.Draw(cam);
		tweenManager.update(0.016f);
	}

	public boolean onTouchEvent(MotionEvent event){
		return volt.onTouchEvent(event);
	}
}
