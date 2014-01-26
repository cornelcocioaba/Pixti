package com.CornelCocioaba.Pixti.Engine;

public interface IEngine {

	public void setCurrentScene(Scene scene);
	
	public void onSurfaceCreated();
	
	public void onSurfaceResized(int width, int height);
	
	public void OnDrawFrame();
	
	public void onPause();

    public void onResume();

    public void onDestroy();
}
