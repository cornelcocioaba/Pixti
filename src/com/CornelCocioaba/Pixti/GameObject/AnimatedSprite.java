package com.CornelCocioaba.Pixti.GameObject;

import com.CornelCocioaba.Pixti.Core.Time;
import com.CornelCocioaba.Pixti.Graphics.TextureRegion;

public class AnimatedSprite extends Sprite {

	protected TextureRegion[] textureRegions;
	protected int currentTexture;
	protected double duration, lastChange;
	private IAnimationListener mAnimationListener;
	private boolean loop;
	private boolean finished;
	private boolean playing;
	
	public AnimatedSprite(float x, float y, double duration, TextureRegion... regions) {
		super( x, y,regions[0]);

		this.textureRegions = regions;
		this.currentTexture = 0;
		this.duration = duration;
		this.loop = false;
		this.finished = false;
		this.loop = false;
	}
	
	public void play(){
		playing = true;
	}
	
	public void pause(){
		playing = false;
	}
	
	public void replay(){
		currentTexture = 0;
		finished = false;
		play();
	}
	
	public void setLoop(boolean l){
		loop = l;
	}
	
	public void setAnimationListener(IAnimationListener listener){
		mAnimationListener = listener;
	}

	@Override
	public void Update() {
		super.Update();
		
		
		if(finished || !playing) return;

		if (Time.time - lastChange >= duration) {
			textureRegion = textureRegions[currentTexture];

			lastChange = Time.time;

			currentTexture++;
			
			if (currentTexture >= textureRegions.length){
				if(loop){
					currentTexture = 0;
				}else{
					finished = true;
					if(mAnimationListener != null){
						mAnimationListener.onAnimationFinished(this);
					}
				}
			}
		}
	}
	
	public interface IAnimationListener{
//		public void onAnimationStarted(AnimatedSprite animatedSprite);
		
//		public void onFrameChanged(AnimatedSprite animatedSprite, int oldFrame, int newFrame);
		
		public void onAnimationFinished(AnimatedSprite animatedSprite);
		
//		public void onAnimationLoopFinished(AnimatedSprite animatedSprite);
	}
}
