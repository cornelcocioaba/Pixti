package com.CornelCocioaba.Pixti.Core;

public interface IEngine {

	public void setCurrentScene(Scene scene);
	
	public GameSurfaceView getSurfaceView();
	
	public void onCreated();
	
	public void onResized(int width, int height);
	
	public void onDrawFrame();
	
	public void onPause();

    public void onResume();

    public void onDestroy();
}
